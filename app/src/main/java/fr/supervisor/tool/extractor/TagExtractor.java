/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.supervisor.tool.extractor;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author vpoulin
 */
public abstract class TagExtractor {
    
     
    public static Set<String> extractTags(String requirement){
        Set<String> tags = new HashSet<String>();
        Matcher tagsMatcher = Pattern.compile("\\[(.*)\\]").matcher(requirement);
        while(tagsMatcher.find()){
            String allTags = tagsMatcher.group(1);
           for(String tag : allTags.split("[,;+]")){
               tags.add(tag);
           }
        }
        return tags;
    }
    
}
