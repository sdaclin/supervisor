package fr.supervisor.tool.extractor;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 15:04
 */
public class WordExtractorTest {
    @Test
    public void extractWord(){
        WordExtractor.extractSection(new File("c:\\CDS-000532-01-Conception-SICLOP-G4R8C2-DFD.doc"));
    }
}
