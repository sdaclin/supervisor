package fr.supervisor.model;

import static com.google.common.base.Preconditions.*;

/**
 * User: sdaclin
 * Date: 20/07/13
 * Time: 09:33
 */
public class Requirement {
    private String id;
    // TODO set nullable
    private String parent;
    // TODO set nullable
    private String comment;

    public Requirement(String id){
        checkNotNull(id, "Id is mandatory");
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id+"\n"
                + ((parent!=null) ? "["+parent+"]\n" : "")
                + ((comment!=null) ? comment : "");
    }
}
