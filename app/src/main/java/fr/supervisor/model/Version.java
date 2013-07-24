package fr.supervisor.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:33
 */
public class Version {
    
    private transient Path path;
    private List<Phase> phases;
    private Requirement rootRequirement;
    
    public Version(Path p){
        path = p;
        phases = new ArrayList<Phase>();
        rootRequirement = new Requirement("ROOT");
    }
    
    public Requirement getRootRequirement(){
        return rootRequirement;
    }
    
    public Path getPath(){
        return path;
    }
    
    public String getVersion(){
        return path.getFileName().toString();
    }
    
    public void setPath(Path p ){
        path = p;
    }
    
    public void addPhase(Phase phase){
        phases.add(phase);
    }
    
    @Override
    public String toString(){ 
       return toStringTabbed(0);
    }
    
     public String toStringTabbed(int level){
        // 1337 M0D3
        String tabs = new String(new char[level]).replace("\0", "\t");

        StringBuilder sb = new StringBuilder(tabs).append("Version : "+path.getFileName());
        if(phases.isEmpty()){
            sb.append("\n \t"+tabs+"Aucune phase");
        }
        for(Phase phase : phases){
            sb.append("\n"+phase.toStringTabbed(level+1));
        }
        return sb.toString();
    }
}
