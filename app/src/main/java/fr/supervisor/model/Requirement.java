package fr.supervisor.model;

import java.nio.file.Path;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:33
 */
public class Requirement {
    
    private Path path;
    
    public String toString(){
        return toStringTabbed(0);
    }
    
     public String toStringTabbed(int level){
        // 1337 M0D3
        String tabs = new String(new char[level]).replace("\0", "\t");
        StringBuilder sb = new StringBuilder(tabs).append("Requirement : "+path.getFileName());
        return sb.toString();
    }
    
}
