package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {
    private File baseDir;
    //private String filePath;
    public DocumentPersistenceManager(File baseDir){
        if(baseDir == null){
            this.baseDir = new File(System.getProperty("user.dir"));
        } else if (baseDir != null) {
            this.baseDir = baseDir;
        }
        //this.filePath = this.baseDir.getAbsolutePath();
    }
    private File getFilePath(){
        return this.baseDir;
    }
    @Override
    public void serialize(URI uri, Document val) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(val);
        File thisPath = new File(getFilePath(), getSpecificPath(uri));
        thisPath = new File(thisPath.getParent());
        thisPath.mkdirs();
        File jsonFile = new File(getFilePath(), getSpecificPath(uri) + ".json");
        FileWriter writer = new FileWriter(jsonFile);
        writer.write(json);
        writer.close();
    }

    @Override
    public Document deserialize(URI uri) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        File thisPath = new File(getFilePath(), getSpecificPath(uri));
        thisPath = new File(thisPath.getParent());
        thisPath.mkdirs();
        File jsonFile = new File(getFilePath(), getSpecificPath(uri) + ".json");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonFile));
        Document document = gson.fromJson(bufferedReader, DocumentImpl.class);
        delete(uri);
        return document;
    }


    @Override
    public boolean delete(URI uri) throws IOException {
        File file = new File(getFilePath(), getSpecificPath(uri) + ".json");
        if(file.delete()){
            return true;
        }
        return false;
    }
    private String getSpecificPath(URI uri){
        String uriToString = uri.getAuthority() + uri.getPath();
        return uriToString;
    }
}
