package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.stage5.Document;

import java.net.URI;
import java.util.*;

public class DocumentImpl implements Document
{
    private URI uri;
    private String txt;
    private byte[] binaryData;
    private HashMap<String, Integer> wordMap;
    private transient long lastUsedTime;
    public DocumentImpl(URI uri, String txt, Map<String, Integer> wordCountMap)
    {
        //System.out.println("Doc txt: " + txt);
        if(uri == null || txt == null || txt.isEmpty() || uri.toString().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.txt = txt;
        if(wordCountMap == null){
            setWordMap(mapWords());
        } else if (wordCountMap != null) {
            setWordMap(wordCountMap);
        }
        //this.wordMap = (HashMap<String, Integer>) getWordMap();
        this.lastUsedTime = 0;
    }
    public DocumentImpl(URI uri, byte[] binaryData)
    {
        if(uri == null || binaryData == null || binaryData.length == 0 || uri.toString().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.binaryData = binaryData;
    }
    /**
     * @return content of text document
     */
    @Override
    public String getDocumentTxt()
    {
        return this.txt;
    }

    /**
     * @return content of binary data document
     */
    @Override
    public byte[] getDocumentBinaryData()
    {
        return this.binaryData;
    }

    /**
     * @return URI which uniquely identifies this document
     */
    @Override
    public URI getKey()
    {
        return this.uri;
    }

    @Override
    public int wordCount(String word) {
        if(this.binaryData != null || getWordMap().get(word) == null){
            return 0;
        }
        return this.wordMap.get(word);
    }

    @Override
    public Set<String> getWords() {
        if(this.binaryData != null || getWordMap() == null){
            return null;
        }
        Set<String> rtn = new HashSet<>(getListOfWords());
        return rtn;
    }

    @Override
    public long getLastUseTime() {
        return this.lastUsedTime;
    }

    @Override
    public void setLastUseTime(long timeInNanoseconds) {
        if(timeInNanoseconds < 0){
            throw new IllegalArgumentException();
        }
        this.lastUsedTime = timeInNanoseconds;
    }

    @Override
    public Map<String, Integer> getWordMap() {
        return this.wordMap;
    }

    @Override
    public void setWordMap(Map<String, Integer> wordMap) {
        this.wordMap = (HashMap<String, Integer>) wordMap;
    }

    private List<String> getListOfWords(){
        List<String> words = new ArrayList<>();
    /*if(getDocumentTxt().length() == 0){
        return words;
    }*/
        if(!getDocumentTxt().contains(" ")){
            words.add(getDocumentTxt());
            return listFormatWords(words);
        }
        String sub = getDocumentTxt();
        int holder = 0; int charSoFar = 0;
        boolean first = true; boolean helper = true;
        for (int i = 0; i < getDocumentTxt().toCharArray().length; i++) {
            if(helper && getDocumentTxt().charAt(i) == ' '){
                continue;
            }
            if(getDocumentTxt().charAt(i) != ' '){
                helper = false;
            }
            if (getDocumentTxt().charAt(i) == ' ' && (getDocumentTxt().charAt(i - 1) != ' ' || getDocumentTxt().charAt(i + 1) != ' ')) {
                holder = i - charSoFar;
                if(first){
                    words.add(sub.substring(0, holder));
                    first = false;
                } else if (!first) {
                    words.add(sub.substring(1, holder));
                }
                charSoFar += sub.substring(0, holder).length();
                sub = sub.substring(holder);
            }
        }
        sub = sub.substring(1);
        words.add(sub);
        return listFormatWords(words);
    }
    private List<String> listFormatWords(List<String> words){
        String temp = "";
        List<String> rtn = new ArrayList<>();
        for(String s: words){
            for(int c = 0; c < s.length(); c++){
                int a = s.charAt(c);
                if((a >= 65 && a <= 95) || (a >= 97 && a <= 122) || (a >= 48 && a <= 57)){
                    temp += s.charAt(c);
                }
            }
            rtn.add(temp);
            temp = "";
        }
        return rtn;
    }
    private HashMap<String, Integer> mapWords(){
        HashMap<String, Integer> map = new HashMap<>();
        for(String s: getListOfWords()){
            map.put(s, map.get(s) == null ? 1: (map.get(s) + 1));
        }
        return map;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(this == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        Document otherDoc = (Document) obj;
        return otherDoc.hashCode() == this.hashCode();
    }
    @Override
    public int hashCode()
    {
        int result = uri.hashCode();
        result = 31 * result + (this.txt != null ? this.txt.hashCode() : 0);
        //may not need the this.text. it was only txt before
        result = 31 * result + (this.binaryData != null ? Arrays.hashCode(this.binaryData) : 0);
        return result;
    }

    @Override
    public int compareTo(Document o) {
        if(o == null){
            throw new NullPointerException();
        }
        if(getLastUseTime() > o.getLastUseTime()){
            return 1;
        }
        if(getLastUseTime() < o.getLastUseTime()){
            return -1;
        }
        return 0;
    }

}
