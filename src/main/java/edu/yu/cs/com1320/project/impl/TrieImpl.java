package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.Trie;

import java.util.*;

public class TrieImpl<Value> implements Trie<Value>
{
    private static final int alphabetSize = 256; // used to be 62 i think, need to switch back
    private Node root; // root of trie

    public static class Node<Value>
    {
        protected Value val;
        protected Node[] links = new Node[TrieImpl.alphabetSize];
        protected List<Value> hits = new ArrayList<>(); // might need to switch from arraylist
    }
    public TrieImpl()
    {
        //constructor with no args
        this.root = null; //might have to make this not null
    }

    @Override
    public void put(String key, Value val)
    {
        if(key == null)
        {
            throw new IllegalArgumentException();
        }
        //deleteAll the value from this key
        if (val == null)
        {
            this.deleteAll(key);
        }
        else
        {
            this.root = put(this.root, key, val, 0); //but isn't this.root null?
        }
    }
    private Node put(Node x, String key, Value val, int d)
    {
        //create a new node
        if (x == null)
        {
            x = new Node();
        }
        //we've reached the last node in the key,
        //set the value for the key and return the node
        if (d == key.length())
        {
            //x.val = val;
            x.hits.add(val);
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        x.links[c] = this.put(x.links[c], key, val, d + 1);
        return x;
    }
    @Override
    public List<Value> getAllSorted(String key, Comparator<Value> comparator)
    {
        if(key == null)
        {
            throw new IllegalArgumentException();
        }
        List<Value> rtn = new ArrayList<>();
        Node x = this.get(this.root, key, 0); // but isn't this.root null?
        if (x == null)
        {
            return rtn; // the instructions say to rtn an empty list
        }
        Collections.sort(x.hits, comparator);
        //Set<Value> hits = new HashSet<>(x.hits);
        //List<Value> listOfSetHits = new ArrayList<>(hits);
        return distinctList(x.hits);
    }
    private List<Value> distinctList(List<Value> list){
        List<Value> distinct = new ArrayList<>();
        for(Value v: list){
            if(!distinct.contains(v)){
                distinct.add(v);
            }
        }
        return distinct;
    }
    private List<Value> getAllSorted(String key)
    {
        List<Value> rtn = new ArrayList<>();
        Node x = this.get(this.root, key, 0); // but isn't this.root null?
        if (x == null)
        {
            return rtn; // the instructions say to rtn an empty list
        }
        return distinctList(x.hits);
    }
    private Node get(Node x, String key, int d)
    {
        //link was null - return null, indicating a miss
        if (x == null)
        {
            return null;
        }
        //we've reached the last node in the key,
        //return the node
        if (d == key.length())
        {
            return x;
        }
        //proceed to the next node in the chain of nodes that
        //forms the desired key
        char c = key.charAt(d);
        return this.get(x.links[c], key, d + 1);
    }
    @Override
    public List<Value> getAllWithPrefixSorted(String prefix, Comparator<Value> comparator) {
        if(prefix == null)
        {
            throw new IllegalArgumentException();
        }
        List<Value> prefixHits = new ArrayList<>();
        Node x = this.get(this.root, prefix, 0); // but isn't this.root null?
        if (x == null)
        {
            return prefixHits;
        }
        //traverse the trie and add all nodes with nonempty lists/values to prefixHits
        prefixHits = getAllWithPrefixSorted(x);
        //System.out.println("before sorting\n" + prefixHits);
        Collections.sort(prefixHits, comparator); //might need to sort without importing
        //System.out.println("list was sorted\n" + prefixHits);
        //Set<Value> hits = new HashSet<>(prefixHits);
        //List<Value> listOfSetHits = new ArrayList<>(hits);
        return distinctList(prefixHits);
    }
    private List<Value> getAllWithPrefixSorted(Node x)
    {
        List<Value> prefixHits = new ArrayList<>();
        if(x != null)
        {
            for(int c = 0; c <TrieImpl.alphabetSize; c++)
            {
                //System.out.println("got to before the null check");
                if(x.links[c] != null) {
                    //System.out.println("got to post null check");
                    if (!x.links[c].hits.isEmpty()) {
                        //System.out.println("hit");
                        prefixHits.addAll(x.links[c].hits);
                        //System.out.println("in the hit " + prefixHits);
                    }
                    //System.out.println("got to the recursive call");
                    //recursive call on the all next nodes. Hope it works!!
                    prefixHits.addAll(getAllWithPrefixSorted(x.links[c]));
                }
            }
        }
        //System.out.println("before rtn " + prefixHits);
        return prefixHits;
    }
    @Override
    public Set<Value> deleteAllWithPrefix(String prefix) {
        if(prefix == null)
        {
            throw new IllegalArgumentException();
        }
        Set<Value> rtn = new HashSet<>();
        Node x = this.get(this.root, prefix, 0); // but isn't this.root null?
        if (x == null)
        {
            return rtn;
        }
        //save the set of values to be returned
        rtn = new HashSet<>(getAllWithPrefixSorted(x));
        //delete all subtries of the prefix
        for(int c = 0; c <TrieImpl.alphabetSize; c++)
        {
            if(x.links[c] != null) {
                x.links[c] = null;
            }
        }
        return rtn;
    }
    @Override
    public Set<Value> deleteAll(String key)
    {
        if(key == null)
        {
            throw new IllegalArgumentException();
        }
        Set<Value> rtn = new HashSet<>(getAllSorted(key));
        Node n = deleteAll(this.root, key, 0);
        //this.root = n;
        return rtn;
    }
    private Node deleteAll(Node x, String key, int d)
    {
        if (x == null)
        {
            return null;
        }
        //we're at the node to del - set the val to null
        if (d == key.length())
        {
            x.val = null;
        }
        //continue down the trie to the target node
        else
        {
            char c = key.charAt(d);
            x.links[c] = this.deleteAll(x.links[c], key, d + 1);
        }
        //this node has a val – do nothing, return the node
        if (x.val != null)
        {
            return x;
        }
        //remove subtrie rooted at x if it is completely empty
        for (int c = 0; c <TrieImpl.alphabetSize; c++)
        {
            if (x.links[c] != null)
            {
                return x; //not empty
            }
        }
        //empty - set this link to null in the parent
        return null;
    }
    @Override
    public Value delete(String key, Value val)
    {
        if(key == null)
        {
            throw new IllegalArgumentException();
        }
        Node x = this.get(this.root, key, 0);
        if (x == null)
        {
            return null;
        }
        //remove val from the list associated with the node
        x.hits.remove(val);
        return val;
    }
}

