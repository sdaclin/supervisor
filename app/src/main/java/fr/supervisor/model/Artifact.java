package fr.supervisor.model;

import fr.supervisor.model.configuration.ArtifactConf;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:33
 */
public class Artifact {
    
    private transient Path path;
    
    //As path won't be serialized, store the artifact name 
    private String name;
    private List<Requirement> requirements;
    private transient ArtifactConf conf;
    
    public Artifact(Path path, ArtifactConf conf){
        this.path = path;
        this.name=this.path.getFileName().toString();
        this.conf = conf;
        requirements = new ArrayList<Requirement>();
    }
    
    public void setPath(Path path){
        this.path = path;
        this.name=this.path.getFileName().toString();
    }
    
    public Path getPath(){
        return path;
    }
    
    public ArtifactConf getConf(){
        return conf;
    }
    public void addRequirement(Requirement req){
        requirements.add(req);
    }
    
    public void setRequirements(List<Requirement> requirements){
        this.requirements = requirements;
    }
    
    @Override
    public String toString(){
        return toStringTabbed(0);
    }
    
    public String toStringTabbed(int level){
        // 1337 M0D3
        String tabs = new String(new char[level]).replace("\0", "\t");

        StringBuilder sb = new StringBuilder(tabs).append("Artifact : "+name);
        
        if(requirements.isEmpty()){
            sb.append("\n \t"+tabs+"Aucun requirement");
        }
        for(Requirement requirement : requirements){
            sb.append("\n"+requirement.toStringTabbed(level+1));
        }
        return sb.toString();
    }
    
}
