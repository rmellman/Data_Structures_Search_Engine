package edu.yu.cs.com1320.project.stage5.impl;
import edu.yu.cs.com1320.project.*;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.lang.Math;

public class DocumentStoreImpl implements DocumentStore {
    private BTree<URI, Document> bTree;
    private PersistenceManager<URI, Document> pm;
    private Stack<Undoable> stack;
    private Trie<URI> trie;
    private int maxDocCount;
    private int maxDocBytes;
    private int dsDocCount;
    private int dsBytes;
    private MinHeap<Node> minHeap;
    private class Node implements Comparable<Node> {
        private URI uri;
        private Document doc;
        private Node(URI uri){
            this.uri = uri;
            this.doc = getbTree().get(this.uri);
        }
        private Document getDoc(){
            return this.doc;
        }
        private URI getURI(){
            return this.uri;
        }

        @Override
        public int compareTo(Node o) {
            if(o == null){
                throw new NullPointerException();
            }
            if(getDoc().getLastUseTime() > o.getDoc().getLastUseTime()){
                return 1;
            }
            if(getDoc().getLastUseTime() < o.getDoc().getLastUseTime()){
                return -1;
            }
            return 0;
        }
    }

    /**
     * the two document formats supported by this document store.
     * Note that TXT means plain text, i.e. a String.
     */
    public DocumentStoreImpl() {
        this.bTree = new BTreeImpl<>();
        this.pm = new DocumentPersistenceManager(null);
        getbTree().setPersistenceManager(this.pm);
        this.stack = new StackImpl<>();
        this.trie = new TrieImpl<>();
        this.minHeap = new MinHeapImpl<>();
        this.maxDocCount = 0;
        this.maxDocBytes = 0;
        this.dsDocCount = 0;
        this.dsBytes = 0;
    }
    public DocumentStoreImpl(File baseDir){
        this.bTree = new BTreeImpl<>();
        this.pm = new DocumentPersistenceManager(baseDir);
        getbTree().setPersistenceManager(this.pm);
        this.stack = new StackImpl<>();
        this.trie = new TrieImpl<>();
        this.minHeap = new MinHeapImpl<>();
        this.maxDocCount = 0;
        this.maxDocBytes = 0;
        this.dsDocCount = 0;
        this.dsBytes = 0;
    }

    /**
     * @param input  the document being put
     * @param uri    unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc,
     * return the hashCode of the previous doc. If InputStream is null, this is a delete,
     * and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     * @throws IOException              if there is an issue reading input
     * @throws IllegalArgumentException if uri or format are null
     */
    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        int docCreator = 0;
        if (uri == null || format == null) {
            throw new IllegalArgumentException();
        }
        if (input == null) {
            //delete ---- do I use this class's delete?
            int rtn = getbTree().get(uri).hashCode();
            if (delete(uri)) {
                return Math.abs(rtn);
            }
            return 0;
        }
        //have to update to accommodate the trie
        Function<URI, Boolean> undo = null;
        if (bTreeContainsKey(uri)) {
            Document oldDoc = getbTree().get(uri);
            removeHelper(oldDoc);
            docCreator = docCreator(input, uri, format);
            undo = (URI u) -> {
                deleteWordsInTrie(getbTree().get(uri));
                removeFromHeap(getbTree().get(uri));
                getbTree().put(u, oldDoc);
                putWordsInTrie(oldDoc);
                heapInsert(oldDoc);
                return true;
            };
        } else if (!bTreeContainsKey(uri)) {
            docCreator = docCreator(input, uri, format);
            undo = (URI u) -> {
                deleteWordsInTrie(getbTree().get(uri));
                removeFromHeap(getbTree().get(uri));
                return getbTree().put(u, null) != null;
            };
        }
        //generic command
        GenericCommand<URI> c = new GenericCommand<>(uri, undo);
        this.stack.push(c);
        return Math.abs(docCreator);
    }

    private int docCreator(InputStream input, URI uri, DocumentFormat format) throws IOException {
        int rtn = 0;
        switch (format) {
            case BINARY:
                //read the input into a byte array
                //throw IOException if needed
                byte[] binaryData;
                try {
                    binaryData = input.readAllBytes(); // needs a size
                } catch (IOException e) {
                    throw new IOException();
                }
                Document binaryDoc = new DocumentImpl(uri, binaryData);
                //if doc is in hashTable, return the previous doc's hashCode
                rtn = (bTreeContainsKey(uri) ? getbTree().put(uri, binaryDoc).hashCode() : 0);
                getbTree().put(uri, binaryDoc);
                heapInsert(binaryDoc);
                break;
            case TXT:
                //read input into a string, throw IOException
                String txt;
                try {
                    txt = new String(input.readAllBytes());
                } catch (IOException e) {
                    throw new IOException();
                }
                //map is null because this is a new doc
                Document stringDoc = new DocumentImpl(uri, txt, null);
                //if doc is in hashTable, return the previous doc's hashCode
                rtn = (bTreeContainsKey(uri) ? getbTree().put(uri, stringDoc).hashCode() : 0);
                getbTree().put(uri, stringDoc);
                putWordsInTrie(stringDoc);
                heapInsert(stringDoc);
                break;
        }
        return Math.abs(rtn);
    }

    private void putWordsInTrie(Document d) {
        for (String s : d.getWords()) {
            for (int i = 0; i < d.wordCount(s); i++) {
                this.trie.put(s, d.getKey());
            }
        }
    }

    private void deleteWordsInTrie(Document d) {
        for (String s : d.getWords()) {
            for (int i = 0; i < d.wordCount(s); i++) {
                this.trie.delete(s, d.getKey());
            }
        }
    }

    private void heapInsert(Document d) {
        d.setLastUseTime(System.nanoTime());
        Node n = new Node(d.getKey());
        this.minHeap.insert(n);
        this.dsDocCount++;
        this.dsBytes = this.dsBytes + (d.getDocumentBinaryData() == null ?
                d.getDocumentTxt().getBytes().length : d.getDocumentBinaryData().length);
        updateTime(d);
    }
    private boolean minHeapContainsKey(URI uri){
        Stack<Node> helper = new StackImpl<>();
        boolean found = false;
        boolean empty = false;
        while(!empty){
            try{
                Node n = this.minHeap.remove();
                if(n.getURI().equals(uri)){
                    found = true;
                }
                helper.push(n);
            }
            catch (NoSuchElementException e){
                empty = true;
            }
        }
        while(helper.peek() != null){
            this.minHeap.insert(helper.pop());
        }
        return found;
    }
    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    @Override
    public Document get(URI uri) {
        Document rtn = getbTree().get(uri);
        if(rtn == null) return null;
        if(!minHeapContainsKey(uri)){
            heapInsert(rtn);
        }
        rtn.setLastUseTime(System.nanoTime());
        return rtn;
    }

    private BTree<URI, Document> getbTree() {
        return this.bTree;
    }
    private boolean bTreeContainsKey(URI uri){
        return getbTree().get(uri) != null;
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    @Override
    public boolean delete(URI uri) {
        Document rtn = getbTree().get(uri);
        removeFromHeap(rtn);
        if (getbTree().put(uri, null) != null) {
            for (String s : rtn.getWords()) {
                this.trie.delete(s, rtn.getKey());
                //if there are no values after this string, delete nodes until there is a node with a non empty val
            }
            Function<URI, Boolean> undo = (URI u) -> {
                getbTree().put(u, rtn);
                //have to update to accommodate the trie
                heapInsert(rtn);
                putWordsInTrie(rtn);
                return true;
            };
            //generic command
            GenericCommand<URI> c = new GenericCommand<>(uri, undo);
            this.stack.push(c);
            return true;
        }
        return false;
    }

    @Override
    public void undo() throws IllegalStateException {
        //System.out.println("size problem");
        if(this.stack.size() == 0){
            throw new IllegalStateException();
        }
        //System.out.println("got here");
        if(this.stack.peek() instanceof CommandSet<?>){
            CommandSet<URI> commandSet = (CommandSet<URI>) this.stack.pop();
            commandSet.undoAll();
            return;
        }
        this.stack.pop().undo();
    }
    @Override
    public void undo(URI uri) throws IllegalStateException {
        boolean found = false;
        //might need to make this a stack of undoables not generic commands
        Stack<Undoable> helper = new StackImpl<>();
        while(this.stack.peek() != null){
            if(this.stack.peek() instanceof GenericCommand<?>) {
                GenericCommand current = (GenericCommand) this.stack.pop();
                if (current.getTarget().equals(uri)) {
                    found = true;
                    current.undo();
                    break;
                }
                helper.push(current);
            } else if (this.stack.peek() instanceof CommandSet<?>) {
                CommandSet commandSet = (CommandSet) this.stack.pop();
                CommandSet newCS = new CommandSet<>();
                if(commandSet.containsTarget(uri)){
                    found = true;
                    commandSet.undo(uri);
                    if(commandSet.size() >= 2){
                        for (Iterator it = commandSet.iterator(); it.hasNext(); ) {
                            GenericCommand genericCommand = (GenericCommand) it.next();
                            if (!genericCommand.getTarget().equals(uri)) {
                                newCS.addCommand(genericCommand);
                            }
                        }
                        helper.push(newCS);
                    }
                    break;
                }
                helper.push(commandSet);
            }
        }
        while(helper.peek() != null) {
            this.stack.push(helper.pop());
        }
        //System.out.println("found: " + found);
        if(this.stack.size() == 0 || !found){
            throw new IllegalStateException();
        }
    }

    @Override
    public List<Document> search(String keyword) {
        Comparator<URI> comparator = new Comparator<>(){

            @Override
            public int compare(URI uri1, URI uri2) {
                int w1 = getbTree().get(uri1).wordCount(keyword);
                int w2 = getbTree().get(uri2).wordCount(keyword);
                //decreasing order
                return w2 - w1;
            }
        };
        for(URI u: this.trie.getAllSorted(keyword, comparator)){
            if(!minHeapContainsKey(u)){
                heapInsert(getbTree().get(u));
            }
        }
        updateTimeList(this.trie.getAllSorted(keyword, comparator));
        return toDocSet(this.trie.getAllSorted(keyword, comparator));
    }
    private List<Document> toDocSet(List<URI> uris){
        List<Document> docs = new ArrayList<>();
        for(URI uri: uris){
            docs.add(getbTree().get(uri));
        }
        return docs;
    }

    @Override
    public List<Document> searchByPrefix(String keywordPrefix) {
        Comparator<URI> comparator = new Comparator<>(){

            @Override
            public int compare(URI uri1, URI uri2) {
                int w1 = prefixHits(keywordPrefix, getbTree().get(uri1));
                int w2 = prefixHits(keywordPrefix, getbTree().get(uri2));
                //decreasing order
                return w2 - w1;
            }
        };
        for(URI u: this.trie.getAllWithPrefixSorted(keywordPrefix, comparator)){
            if(!minHeapContainsKey(u)){
                heapInsert(getbTree().get(u));
            }
        }
        updateTimeList(this.trie.getAllWithPrefixSorted(keywordPrefix, comparator));
        return toDocSet(this.trie.getAllWithPrefixSorted(keywordPrefix, comparator));
    }
    private int prefixHits(String prefix, Document d){
        int hits = 0;
        boolean potential = true;
        for(String s: d.getWords()){
            for(int i = 0; i < prefix.length(); i++){
                if(s.charAt(i) == prefix.charAt(i)){
                    if(potential && (i + 1 == prefix.length())){
                        hits++;
                    }
                    continue;
                }
                potential = false;
                break;
            }
            potential = true;
        }
        return hits;
    }

    @Override
    public Set<URI> deleteAll(String keyword) {
        Set<URI> uriSet = new HashSet<>();
        CommandSet<URI> commandSet = new CommandSet<>();
        Comparator<URI> comparator = new Comparator<>(){

            @Override
            public int compare(URI uri1, URI uri2) {
                int w1 = getbTree().get(uri1).wordCount(keyword);
                int w2 = getbTree().get(uri2).wordCount(keyword);
                //decreasing order
                return w2 - w1;
            }
        };
        List<Document> undoDocs = toDocSet(this.trie.getAllSorted(keyword, comparator));
        for(Document d: undoDocs){
            Function<URI, Boolean> undo = (URI u) -> {
                getbTree().put(u, d);
                //have to update to accommodate the trie
                putWordsInTrie(d);
                heapInsert(d);
                return true;
            };
            GenericCommand<URI> genericCommand = new GenericCommand<>(d.getKey(), undo);
            commandSet.addCommand(genericCommand);
        }
        this.stack.push(commandSet);
        uriSet = this.trie.deleteAll(keyword);
        for(URI u: uriSet){
            removeFromHeap(getbTree().get(u));
            getbTree().put(u, null);
        }
        return uriSet;
    }

    @Override
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
        Set<URI> uriSet = new HashSet<>();
        CommandSet<URI> commandSet = new CommandSet<>();
        Comparator<URI> comparator = new Comparator<>(){

            @Override
            public int compare(URI uri1, URI uri2) {
                //System.out.println("compare method called");
                int w1 = prefixHits(keywordPrefix, getbTree().get(uri1));
                //System.out.println(w1);
                int w2 = prefixHits(keywordPrefix, getbTree().get(uri2));
                //System.out.println(w2);
                //decreasing order
                return w2 - w1;
            }
        };
        List<Document> undoDocs = toDocSet(this.trie.getAllWithPrefixSorted(keywordPrefix, comparator));
        for(Document d: undoDocs){
            Function<URI, Boolean> undo = (URI u) -> {
                getbTree().put(u, d);
                //have to update to accommodate the trie
                putWordsInTrie(d);
                heapInsert(d);
                return true;
            };
            GenericCommand<URI> genericCommand = new GenericCommand<>(d.getKey(), undo);
            commandSet.addCommand(genericCommand);
        }
        this.stack.push(commandSet);
        uriSet = this.trie.deleteAllWithPrefix(keywordPrefix);
        for(URI u: uriSet){
            removeFromHeap(getbTree().get(u));
            getbTree().put(u, null);
        }
        return uriSet;
    }

    @Override
    public void setMaxDocumentCount(int limit) {
        if(limit <= 0){
            throw new IllegalArgumentException();
        }
        this.maxDocCount = limit;
        memorySolution();
    }

    @Override
    public void setMaxDocumentBytes(int limit) {
        if(limit <= 0){
            throw new IllegalArgumentException();
        }
        this.maxDocBytes = limit;
        memorySolution();
    }
    private void updateTime(Document d){
        Node n = new Node(d.getKey());
        d.setLastUseTime(System.nanoTime());
        memorySolution();
        if(minHeapContainsKey(d.getKey())) {
            this.minHeap.reHeapify(n);
        }
    }
    private void memorySolution() {
        while(isMemoryFull()){
            Node toRmv = this.minHeap.remove();
            //deleteWordsInTrie(toRmv.getDoc());
            //removeFromCommandStack(toRmv.getDoc());
            removeHelper(toRmv.getDoc());
            try {
                getbTree().moveToDisk(toRmv.getURI());
            } catch (Exception e) {
                //not sure if this is allowed
                throw new RuntimeException(e);
            }
        }
    }
    private void updateTimeList(List<URI> uris){
        List<Document> docs = new ArrayList<>();
        long currentTime = System.nanoTime();
        for(URI u: uris) {
            if (minHeapContainsKey(u)) {
                docs.add(getbTree().get(u));
            }
        }
        for (Document d : docs) {
            d.setLastUseTime(currentTime);
        }
        for (Document d : docs) {
                Node n = new Node(d.getKey());
                memorySolution();
                this.minHeap.reHeapify(n);
        }
    }
    private boolean isMemoryFull(){
        if(this.maxDocCount != 0){
            if(this.dsDocCount > this.maxDocCount){
                return true;
            }
        }
        if(this.maxDocBytes != 0){
            if(this.dsBytes > this.maxDocBytes){
                return true;
            }
        }
        return false;
    }
    private void removeFromHeap(Document d){
        if(minHeapContainsKey(d.getKey())) {
            d.setLastUseTime(0);
            Node n = new Node(d.getKey());
            this.minHeap.reHeapify(n);
            this.minHeap.remove();
            removeHelper(d);
        }
    }
    private void removeHelper(Document d){
        this.dsBytes = this.dsBytes - (d.getDocumentBinaryData() == null ? d.getDocumentTxt().getBytes().length : d.getDocumentBinaryData().length);
        this.dsDocCount--;
    }
}
