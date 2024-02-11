package edu.yu.cs.com1320.project;

import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocStoreTest {

    /*@BeforeEach
    void beforeEach() throws URISyntaxException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "ShouldBeTheFirst";
        String str2 = "secondIsTheBest";
        String str3 = "thirdIsTheWorst";
        URI uri1 = new URI(str1);
        URI uri2 = new URI(str2);
        URI uri3 = new URI(str3);
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
    }*/
    @Test
    void aboveMemory() throws IOException, URISyntaxException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "ShouldBeTheFirst";
        String str2 = "secondIsTheBest";
        String str3 = "thirdIsTheWorst";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        assert (ds.get(uri3) != null);
        assert (ds.get(uri2) != null);
        //checked that only one and two are left and doc3 is on disc
        assert (ds.get(uri1) != null);
    }

    @Test
    void gettingOneDocToChange() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "ShouldBeTheFirst";
        String str2 = "secondIsTheBest";
        String str3 = "thirdIsTheWorst";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.get(uri1);
        ds.put(input3, uri3, strFormat);
        //assert (ds.get(uri1) == null);
        //doc1 was written to disk
        assert (ds.get(uri2) != null);
    }

    @Test
    void searchOneDocToChange() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "ShouldBeTheFirst";
        String str2 = "secondIsTheBest";
        String str3 = "thirdIsTheWorst";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.search(str1);
        ds.put(input3, uri3, strFormat);
        //assert (ds.get(uri1) == null);
        //checked that only two and three are left and doc1 is written to disk
        assert (ds.get(uri2) != null);
    }

    @Test
    void searchPrefixOneDocToChange() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "ShouldBeTheFirst";
        String str2 = "secondIsTheBest";
        String str3 = "thirdIsTheWorst";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.searchByPrefix("Sho");
        ds.put(input3, uri3, strFormat);
        //assert (ds.get(uri1) == null);
        //check that doc1 is on disk
        assert (ds.get(uri2) != null);
    }

    @Test
    void maxBytesFull() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "ShouldBeTheFirst";
        String str2 = "secondIsTheBest";
        String str3 = "thirdIsTheWorst";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentBytes(20);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.search(str1);
        //ds.put(input3, uri3, strFormat);
        //assert (ds.get(uri1) == null);
        //d2 written to disk
        assert (ds.get(uri1) != null);
    }

    @Test
    void undoPut1() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.undo();
        assert (ds.get(uri3) == null);
        //also removed uri3 from minHeap. checked thru debugger.
    }

    @Test
    void undoDelete1() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.delete(uri3);
        assert (ds.get(uri3) == null);
        ds.undo();
        assert (ds.get(uri3) != null);
        //also removed uri3 from minHeap. checked thru debugger.
    }

    @Test
    void simpleSearchTest() throws URISyntaxException, IOException {
        DocumentStore ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        String str = "this";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        InputStream input1 = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        List<Document> list = new ArrayList<>();
        Document d1 = new DocumentImpl(uri1, str, null);
        Document d2 = new DocumentImpl(uri2, str, null);
        list.add(d1);
        list.add(d2);
        assert (ds.search("this").containsAll(list));
    }

    @Test
    void simpleSearchByPrefixTest() throws URISyntaxException, IOException {
        DocumentStore ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        String str1 = "this";
        String str2 = "thing";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        List<Document> list = new ArrayList<>();
        Document d1 = new DocumentImpl(uri1, str1, null);
        Document d2 = new DocumentImpl(uri2, str2, null);
        list.add(d1);
        list.add(d2);
        //System.out.println(ds.searchByPrefix("th").size());
        assert (ds.searchByPrefix("th").containsAll(list));
    }

    @Test
    void undoPut() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        //System.out.println("got to undo in test");
        ds.undo();
        ds.undo();
        assertThrows(IllegalStateException.class, () -> {
            ds.undo();
        });
        //System.out.println("successful undo in test");
        //System.out.println(ds.get(uri2));
        assert (ds.get(uri2) == null);
    }

    @Test
    void undoDelete() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.delete(uri2);
        //System.out.println("got to undo in test");
        ds.undo();
        //System.out.println("successful undo in test");
        //System.out.println(ds.get(uri2));
        assert (ds.get(uri2) != null);
    }

    @Test
    void undoURI() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        //System.out.println("got to undo in test");
        ds.undo(uri1);
        //System.out.println("successful undo in test");
        //System.out.println(ds.get(uri2));
        assert (ds.get(uri1) == null);
        assert (ds.get(uri2) != null);
    }

    @Test
    void undoPrevDoc() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri2, strFormat);
        //System.out.println("got to undo in test");
        ds.undo();
        assert (ds.get(uri2).getDocumentTxt().equals(str2));
    }

    @Test
    void checkSecondUndoOnStack() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        //System.out.println("got to undo in test");
        ds.undo(uri1);
        ds.undo();
        //System.out.println("successful undo in test");
        //System.out.println(ds.get(uri2));
        assert (ds.get(uri1) == null);
        assert (ds.get(uri2) != null);
        assert (ds.get(uri3) == null);
    }

    @Test
    void undoNotInStack() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        //System.out.println("got to undo in test");
        assertThrows(IllegalStateException.class, () -> {
            ds.undo(uri3);
        });
    }

    @Test
    void deleteAllTest() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth";
        String str2 = "second first";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.deleteAll("first");
        assert (ds.search("first").isEmpty());
    }

    @Test
    void deleteAllWithPrefixTest() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth";
        String str2 = "second fit";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.deleteAllWithPrefix("fi");
        assert (ds.searchByPrefix("fi").isEmpty());
    }

    @Test
    void undoDeleteAll() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth";
        String str2 = "second first";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.deleteAll("first");
        ds.undo();
        assert (!ds.search("first").isEmpty());
        assert (ds.search("first").size() == 2);
    }

    @Test
    void undoDeleteAllWithPrefix() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth";
        String str2 = "second fit";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.deleteAllWithPrefix("fi");
        ds.undo();
        assert (!ds.searchByPrefix("fi").isEmpty());
    }

    @Test
        //might not be a good test
    void undoURIDeleteAll() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth";
        String str2 = "second first";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.deleteAll("first");
        ds.undo(uri2);
        //ds.undo(uri1);
        assert (!ds.search("first").isEmpty());
    }

    @Test
    void sortedReturnSearch() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth first";
        String str2 = "second first first first";
        String str3 = "third first";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        //System.out.println(ds.search("first").get(0).getDocumentTxt());
        //System.out.println(ds.search("first").get(1).getDocumentTxt());
        //System.out.println(ds.search("first").get(2).getDocumentTxt());
        assert (ds.search("first").get(0).getDocumentTxt().equals(str2));
        assert (ds.search("first").get(1).getDocumentTxt().equals(str1));
        assert (ds.search("first").get(2).getDocumentTxt().equals(str3));
    }

    @Test
    void sortedReturnSearchWithBinary() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth first";
        String str2 = "second first first first";
        String str3 = "third first";
        String str4 = "four";
        byte[] bytes = str4.getBytes();
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        URI uri4 = new URI("https://www.yu.edu/docs/d4");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        InputStream input4 = new ByteArrayInputStream(str4.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.put(input4, uri4, byteFormat);
        //System.out.println(ds.search("first").get(0).getDocumentTxt());
        //System.out.println(ds.search("first").get(1).getDocumentTxt());
        //System.out.println(ds.search("first").get(2).getDocumentTxt());
        assert (ds.search("first").get(0).getDocumentTxt().equals(str2));
        assert (ds.search("first").get(1).getDocumentTxt().equals(str1));
        assert (ds.search("first").get(2).getDocumentTxt().equals(str3));
    }

    @Test
    void searchWithPrefixSortCheck() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "figure find eighth";
        String str2 = "second fit fig fine";
        String str3 = "third fifth fire fin fizz";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        //System.out.println(ds.searchByPrefix("fi").get(0).getDocumentTxt());
        //System.out.println(ds.searchByPrefix("fi").get(1).getDocumentTxt());
        //System.out.println(ds.searchByPrefix("fi").get(2).getDocumentTxt());
        assert (ds.searchByPrefix("fi").size() == 3);
        assert (ds.searchByPrefix("fi").get(0).getDocumentTxt().equals(str3));
        assert (ds.searchByPrefix("fi").get(1).getDocumentTxt().equals(str2));
        assert (ds.searchByPrefix("fi").get(2).getDocumentTxt().equals(str1));
    }

    @Test
    void putUndoFromTrie() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "figure find eighth";
        String str2 = "second fit fig fine";
        String str3 = "third fifth fire fin fizz";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.undo();
        assert (ds.searchByPrefix("fi").size() == 2);
    }

    @Test
    void undoURIRemovedFromStack() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(2);
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        ds.undo(uri1);
        assert (ds.get(uri1) == null);
    }

    /*@Test
    void removeTwoDocsFromHeap() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(1);
        //System.out.println("problem with the put in test");
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        assert (ds.get(uri1) == null);
        assert (ds.get(uri2) == null);
        assertThrows(IllegalStateException.class, () -> {
            ds.undo(uri1);
        });
        assertThrows(IllegalStateException.class, () -> {
            ds.undo(uri2);
        });
    }*/

    @Test
    void settingError() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "figure fit find eighth";
        String str2 = "second fit fig fine";
        String str3 = "third fire fin fizz";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        assertThrows(IllegalArgumentException.class, () -> {
            ds.setMaxDocumentCount(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ds.setMaxDocumentCount(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ds.setMaxDocumentBytes(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ds.setMaxDocumentBytes(0);
        });
    }

    @Test
    void deleteAllHeap() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth";
        String str2 = "second first";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        //ds.put(input3, uri3, strFormat);
        ds.deleteAll("first");
        assert (ds.search("first").isEmpty());
        ds.undo();
        assert (ds.search("first").size() == 2);
        ds.put(input3, uri3, strFormat);
        //check that d2 is written to disk
        //done
        assert (ds.get(uri1) != null);
        assertEquals (2,ds.search("first").size());
    }

    @Test
    void deleteAllPrefixHeap() throws URISyntaxException, IOException {
        DocumentStoreImpl ds = new DocumentStoreImpl();
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first eighth";
        String str2 = "second first";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        //System.out.println("problem with the put in test");
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        //ds.put(input3, uri3, strFormat);
        ds.deleteAllWithPrefix("fi");
        assert (ds.searchByPrefix("fi").isEmpty());
        ds.undo();
        assert (ds.searchByPrefix("fi").size() == 2);
        ds.put(input3, uri3, strFormat);
        //check that d2 is written to disk
        //done
        assert (ds.get(uri1) != null);
        //even though d2 was moved to disk, it still counts in the trie
        assertEquals (2, ds.searchByPrefix("fi").size());
    }
    @Test
    void aboveMemoryPM() throws IOException, URISyntaxException {
        File dir = new File("/Users/Reuben/Desktop/CS1320/Reuben_Mellman_800673330/DataStructures/project/stage5");
        DocumentStoreImpl ds = new DocumentStoreImpl(dir);
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        File temp = new File(dir, uri1.getAuthority() + uri1.getPath() + ".json");
        assert(temp.exists());
        //System.out.println(Files.readString(temp.toPath()));
        assert (Files.readString(temp.toPath()) != null);
        ds.get(uri1);
        assert(!temp.exists());
        //System.out.println(Files.readString(temp.toPath()));
    }
    @Test
    void aboveMemorySearch() throws IOException, URISyntaxException {
        File dir = new File("/Users/Reuben/Desktop/CS1320/Reuben_Mellman_800673330/DataStructures/project/stage5");
        DocumentStoreImpl ds = new DocumentStoreImpl(dir);
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        File temp = new File(dir, uri1.getAuthority() + uri1.getPath() + ".json");
        assert(temp.exists());
        //System.out.println(Files.readString(temp.toPath()));
        assert (Files.readString(temp.toPath()) != null);
        ds.search("first");
        File temp2 = new File(dir, uri2.getAuthority() + uri2.getPath() + ".json");
        assert(!temp.exists());
        assert(temp2.exists());
        assert (Files.readString(temp2.toPath()) != null);
        //System.out.println(Files.readString(temp.toPath()));
    }
    @Test
    void aboveMemoryPMDelete() throws IOException, URISyntaxException {
        File dir = new File("/Users/Reuben/Desktop/CS1320/Reuben_Mellman_800673330/DataStructures/project/stage5");
        DocumentStoreImpl ds = new DocumentStoreImpl(dir);
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(2);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        File temp = new File(dir, uri1.getAuthority() + uri1.getPath() + ".json");
        assert(temp.exists());
        assert (Files.readString(temp.toPath()) != null);
        ds.delete(uri2);
        ds.search("first");
        assert(!temp.exists());
        //assert (Files.readString(temp.toPath()) == null);
    }
    @Test
    void aboveMemoryPMUndo() throws IOException, URISyntaxException {
        File dir = new File("/Users/Reuben/Desktop/CS1320/Reuben_Mellman_800673330/DataStructures/project/stage5");
        DocumentStoreImpl ds = new DocumentStoreImpl(dir);
        DocumentStore.DocumentFormat strFormat = DocumentStore.DocumentFormat.TXT;
        //DocumentStore.DocumentFormat byteFormat = DocumentStore.DocumentFormat.BINARY;
        String str1 = "first";
        String str2 = "second";
        String str3 = "third";
        URI uri1 = new URI("https://www.yu.edu/docs/d1");
        URI uri2 = new URI("https://www.yu.edu/docs/d2");
        URI uri3 = new URI("https://www.yu.edu/docs/d3");
        InputStream input1 = new ByteArrayInputStream(str1.getBytes(StandardCharsets.UTF_8));
        InputStream input2 = new ByteArrayInputStream(str2.getBytes(StandardCharsets.UTF_8));
        InputStream input3 = new ByteArrayInputStream(str3.getBytes(StandardCharsets.UTF_8));
        ds.setMaxDocumentCount(1);
        ds.put(input1, uri1, strFormat);
        ds.put(input2, uri2, strFormat);
        ds.put(input3, uri3, strFormat);
        File temp = new File(dir, uri1.getAuthority() + uri1.getPath() + ".json");
        File temp2 = new File(dir, uri2.getAuthority() + uri2.getPath() + ".json");
        assert(temp.exists());
        assert (Files.readString(temp.toPath()) != null);
        ds.delete(uri3);
        ds.undo();
        //ds.search("first");
        assert(temp2.exists());
        //assert(getFileContents(temp2) != null);
        assert (Files.readString(temp2.toPath()) != null);
    }

}
