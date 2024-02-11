package edu.yu.cs.com1320.project;

import edu.yu.cs.com1320.project.stage5.*;
import edu.yu.cs.com1320.project.stage5.impl.*;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;

public class DocPMTest {
    @Test
    void simpleSerialize() throws URISyntaxException, IOException {
        File baseDir = new File("/Users/Reuben/Desktop/CS1320/Reuben_Mellman_800673330/DataStructures/project/stage5");
        DocumentPersistenceManager pm = new DocumentPersistenceManager(baseDir);
        //System.out.println("this absolute path: " + Paths.get("").toFile().getAbsolutePath());
        String str1 = "this";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        Document d1 = new DocumentImpl(uri1, str1, null);
        pm.serialize(uri1, d1);
        //System.out.println("Document was serialized");
        Document rtn = pm.deserialize(uri1);
        //System.out.println("Document was deserialized");
        assert (rtn.getDocumentTxt().equals(str1));
        assert (rtn.getKey().equals(uri1));
        assert (rtn.getLastUseTime() == 0);
    }
    @Test
    void nullDirectory() throws URISyntaxException, IOException {
        DocumentPersistenceManager pm = new DocumentPersistenceManager(null);
        //System.out.println(new File("user.dir"));
        //System.out.println("this absolute path: " + Paths.get("").toFile().getAbsolutePath());
        String str1 = "this";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        Document d1 = new DocumentImpl(uri1, str1, null);
        pm.serialize(uri1, d1);
        //System.out.println("Document was serialized");
        Document rtn = pm.deserialize(uri1);
        //System.out.println("Document was deserialized");
        assert (rtn.getDocumentTxt().equals(str1));
        assert (rtn.getKey().equals(uri1));
        assert (rtn.getLastUseTime() == 0);
    }
    @Test
    void binaryDoc() throws URISyntaxException, IOException {
        DocumentPersistenceManager pm = new DocumentPersistenceManager(null);
        //System.out.println(new File("user.dir"));
        //System.out.println("this absolute path: " + Paths.get("").toFile().getAbsolutePath());
        String str1 = "this";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        Document d1 = new DocumentImpl(uri1, str1.getBytes());
        pm.serialize(uri1, d1);
        //System.out.println("Document was serialized");
        Document rtn = pm.deserialize(uri1);
        //System.out.println("Document was deserialized");
        assert (Arrays.equals(str1.getBytes(), rtn.getDocumentBinaryData()));
        assert (rtn.getKey().equals(uri1));
        assert (rtn.getLastUseTime() == 0);
    }
    @Test
    void uri() throws URISyntaxException {
        DocumentPersistenceManager pm = new DocumentPersistenceManager(null);
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        //System.out.println(pm.getSpecificPath(uri1));
    }
}
