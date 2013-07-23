package fr.supervisor.tool.extractor;

import fr.supervisor.model.Requirement;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 15:04
 */
public class WordExtractorTest {
    @Test
    public void extractWord(){
       //WordExtractor.extractSection(new File("C:\\\\siclop\\\\01 - Versions\\\\G4R9\\\\05 - Conception\\\\CDS-002157-03-Conception-SICLOP-G4R9C0-DFD.docx"));
        Requirement rootRequirement = new Requirement("root");
        WordExtractor.extractRequirements(
                //ES Pattern
                Pattern.compile("ES-FT-SICLOP-G\\d+R\\d+C\\d-\\d\\d\\d.*"),
                //Word style pattern
                Pattern.compile("exigence"),
                //Docx file to extract requirement from
                new File("C:\\siclop\\01 - Versions\\G4R9C1\\05 - Conception\\CDS-002365-01-Conception-SICLOP-G4R9C1-DFD.docx"),
                //root element for the requirements tree
                rootRequirement);
        
        System.out.println("REQUIREMENTS TREE : \n"+rootRequirement);
    }
}
