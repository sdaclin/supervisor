/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import fr.supervisor.model.Project;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vpoulin
 */
public class ProjectSerializer {
    
    //enable Complex Map Key serialization in order to serialize the treemap that stores versions correctly : map keys won't be serialized through their toString value
    //->In fact, Dates will be serialized as defined in setDateFormat
    private static Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").enableComplexMapKeySerialization().create();
    
    /**
     * Save a project as a json file
     * @param filePath path to the destination file
     * @param p project to save
     */
    public static void saveProject(String filePath,Project p) {
        FileWriter fw = null;
        try {
            String jsonProject = gson.toJson(p);
            fw = new FileWriter(filePath);
            fw.append(jsonProject);
        } catch (Exception ex) {
            Logger.getLogger(ProjectSerializer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (Exception ex) {
                Logger.getLogger(ProjectSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /**
     * Load a project from a json file
     * @param filePath path to the json file containing a project
     * @return deserialized project
     */
    public static Project loadProject(String filePath){
        FileReader fr = null;
        Project loadedProject = null;
        try {
            fr = new FileReader(filePath);
            loadedProject = gson.fromJson(fr,Project.class);
        } catch(FileNotFoundException | JsonSyntaxException | JsonIOException ex){
             Logger.getLogger(ProjectSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try{
                fr.close();
            }catch(Exception ex){
                Logger.getLogger(ProjectSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return loadedProject;
        }
    }

}
