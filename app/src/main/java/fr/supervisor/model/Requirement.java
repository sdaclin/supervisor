package fr.supervisor.model;

import static com.google.common.base.Preconditions.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:33
 */
public class Requirement {
    private String id;
    // TODO set nullable
    private List<Requirement> children;
    // TODO set nullable
    private String comment;
    
    private Set<String> tags;
        
    public Requirement(String id){
        checkNotNull(id, "Id is mandatory");
        this.id = id;
        this.children = new ArrayList<Requirement>();
        tags = new HashSet<String>();
    }
    
    public void addTag(String tag){
        tags.add(tag);
    }
    
    public void addAllTag(Set<String> tags){
        this.tags.addAll(tags);
    }
    public void setTags(Set<String> tags){
        this.tags = tags;
    }
    public List<Requirement> getChildren() {
        return children;
    }

    public void setChildren(List<Requirement> children) {
        this.children = children;
    }

    public void addChild(Requirement child){
        children.add(child);
    }

    /**
     * Fonction recursive de recherche de requirements
     * @param id id du requirement a trouver
     * @return le requirement voulu
     */
    public Requirement findById(String id){

        if(this.id.equalsIgnoreCase(id))
            return this;

        for(Requirement requirement : children){
             Requirement req = requirement.findById(id);
             if(req != null)
                 return req;
        }

        return null;
    }

    public List<Requirement> getAllChildren(){
        List<Requirement> requirements = new ArrayList<Requirement>();
        for(Requirement child : children){
            child.getAll(requirements);
        }
        return requirements;
    }

    private void getAll(List<Requirement> listReqs){
        listReqs.add(this);
        for(Requirement child : children){
            child.getAll(listReqs);
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId(){
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return toStringTabbed(0);
    }

    public String toStringTabbed(int level){
       String tabs = new String(new char[level]).replace("\0", "\t");
       StringBuilder sb = new StringBuilder(tabs).append("["+id+"] " + ((comment!=null) ? comment.replaceAll("\\n"," ") : ""));
       for(Requirement requirement : children){
           sb.append("\n"+requirement.toStringTabbed(level+1));
       }
       return sb.toString();
   }

}
