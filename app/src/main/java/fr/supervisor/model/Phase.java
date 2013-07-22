package fr.supervisor.model;

import fr.supervisor.model.configuration.PhaseConf;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:33
 */
public class Phase {
    
    private Path path;
    private PhaseConf conf;
    private List<Artifact> artifacts;
    
    public Phase(Path path, PhaseConf conf){
        this.path = path;
        this.conf = conf;
        artifacts = new ArrayList<Artifact>();
    }
    
    public PhaseConf getConf(){
        return conf;
    }
    
    public Path getPath(){
        return path;
    }
    
    public void setPath(Path path){
        this.path = path;
    }
    
    public void addArtifact(Artifact artifact){
        artifacts.add(artifact);
    }
    public String toString(){
        return toStringTabbed(0);
    }
    
     public String toStringTabbed(int level){
        // 1337 M0D3
        String tabs = new String(new char[level]).replace("\0", "\t");
        StringBuilder sb = new StringBuilder(tabs).append("Phase : "+path.getFileName());
        
        if(artifacts.isEmpty()){
             sb.append("\n \t"+tabs+"Aucun artefact");
        }
          
        for(Artifact artifact : artifacts){
            sb.append("\n"+artifact.toStringTabbed(level+1));
        }
        return sb.toString();
    }

}
