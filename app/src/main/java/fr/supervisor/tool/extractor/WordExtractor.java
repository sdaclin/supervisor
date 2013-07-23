package fr.supervisor.tool.extractor;


import fr.supervisor.model.Requirement;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

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
            System.out.println("Contenu  : "+handler.toString());
        } catch (IOException |SAXException | TikaException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    
    /**
     * Extracts requirements from a docx file. Maintain a tree-like structure of requirements whose root element is rootRequirement
     * Attempts to find the parent requirement as well
     * @param requirementPattern requirement pattern
     * @param wordStylePattern MS Word style pattern, requirements will only be searched in paragraphs using those styles
     * @param file File in which requirements will be searched
     * @param rootRequirement parent Requirement : it's the root of the tree-like structure
     * @return a List of requirements found in this file
     */
    public static  List<Requirement> extractRequirements(Pattern requirementPattern,Pattern wordStylePattern, File file, Requirement rootRequirement){
        
        List<Requirement> listRequirements = new ArrayList<Requirement>();
        
        try { 
            
            if(requirementPattern == null)
                throw new Exception("Le pattern de requirement est obligatoire");
            
            final XWPFDocument document = new XWPFDocument(new FileInputStream(file));
            Iterator<XWPFParagraph> it = document.getParagraphsIterator();
            XWPFParagraph curParagraph;
            //for each paragraph of this document 
            while(it.hasNext()){   
                curParagraph = it.next();
                
                //Check if it defines a title matching the wordStylePattern
                if(curParagraph.getStyle()!= null && wordStylePattern.matcher(curParagraph.getStyle()).find()){

                    //retrieve the requirement ID (first run block)
                    List<XWPFRun> runBlocks = curParagraph.getRuns();    
                    if(runBlocks!=null && !runBlocks.isEmpty()){
                         //Retrieve every run block that form the requirement
                         String fullRequirement = "";   
                         for(XWPFRun run : runBlocks){
                                fullRequirement+= run.getText(0)==null ? "" : run.getText(0);
                         }
                        
                         
                        Matcher requirementMatcher =  requirementPattern.matcher(fullRequirement);
                        
                        //if the full requirement contains and id that matches the pattern  
                        if(requirementMatcher.find()){
                            
                            String requirementID = requirementMatcher.group();
                            String comment = fullRequirement.substring(requirementID.length());
                            Requirement newRequirement = new Requirement(requirementID);
                            newRequirement.setComment(comment);
                            
                            //get the parent requirement id, if it exists(in the next paragraph)
                            XWPFParagraph nextParagraph = it.next();

                            List<XWPFRun> listRuns = nextParagraph.getRuns();
                            //Retrieve every run block that may contain the parent id
                            String fullParentParagraph = "";   
                            for(XWPFRun run : listRuns){
                                   fullParentParagraph+=run.getText(0);
                            }
                            
                            //Check if a requirement id is in this paragraph by searching for every requiremnt already found 
                            boolean hasParent = false;
                            for(Requirement requirement : rootRequirement.getAllChildren()){
                                
                                if(fullParentParagraph.contains(requirement.getId())){
                                    requirement.addChild(newRequirement);
                                    hasParent = true;
                                }
                            }
                            if(! hasParent){
                                rootRequirement.addChild(newRequirement);
                            }
                            
                            listRequirements.add(newRequirement);
                        }
                    }
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(WordExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            return listRequirements;
        }
    }
}
