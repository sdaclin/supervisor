package fr.supervisor.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:33
 */
public class Artifact {
    
    private Path path;
    private List<Requirement> requirements;
    
    public Artifact(Path path){
        this.path = path;
        requirements = new ArrayList<Requirement>();
    }
    
    public void setPath(Path path){
        this.path = path;
    }
    
    public Path getPath(){
        return path;
    }
    public void addRequirement(Requirement req){
        requirements.add(req);
    }
    
    @Override
    public String toString(){
        return toStringTabbed(0);
    }
    
    public String toStringTabbed(int level){
        // 1337 M0D3
        String tabs = new String(new char[level]).replace("\0", "\t");

        StringBuilder sb = new StringBuilder(tabs).append("Artifact : "+path.getFileName());
        
        if(requirements.isEmpty()){
            sb.append("\n \t"+tabs+"Aucun requirement");
        }
        for(Requirement requirement : requirements){
            sb.append("\n"+requirement.toStringTabbed(level+1));
        }
        return sb.toString();
    }
    
}
