package fr.supervisor.tool.extractor;


import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.TextContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.List;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 14:58
 */
public class WordExtractor {
    public static void extractSection(File file){
        try (InputStream is = new FileInputStream(file)) {
            ParseContext context = new ParseContext();
            Metadata metadata = new Metadata();
            ContentHandler handler = new BodyContentHandler(System.out);

            Parser parser = new AutoDetectParser();
            parser.parse(is,handler,metadata,context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TikaException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }
}
