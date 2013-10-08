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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

/**
 * @author vpoulin
 */
public class ProjectSerializer {

    //enable Complex Map Key serialization in order to serialize the treemap that stores versions correctly : map keys won't be serialized through their toString value
    //->In fact, Dates will be serialized as defined in setDateFormat
    private static Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").enableComplexMapKeySerialization().create();

    private static final String JSONP_PREFIX ="var supervisorData=";

    /**
     * Save a project as a json file
     * @param filePath path to the destination file
     * @param p project to save
     */
    public static void saveProject(String filePath,Project p) {
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath),Charset.forName("UTF-8").newEncoder())) {
            osw.append(JSONP_PREFIX);
            gson.toJson(p,osw);
        }catch (IOException ioe){
            LoggerFactory.getLogger(ProjectSerializer.class).error("Impossible de sauvegarder le projet {}",p.getName(),ioe);
        }
    }

    /**
     * Load a project from a json file
     * @param filePath path to the json file containing a project
     * @return deserialized project
     */
    public static Project loadProject(String filePath){
        InputStreamReader isr = null;
        Project loadedProject = null;

        try (InputStreamReader isr1 = new InputStreamReader(new FileInputStream(filePath),Charset.forName("UTF-8").newDecoder())){
            isr1.read(new char [JSONP_PREFIX.length()],0, JSONP_PREFIX.length());
            loadedProject = gson.fromJson(isr1,Project.class);
        }catch (IOException ioe){
            LoggerFactory.getLogger(ProjectSerializer.class).error("Impossible de charger l'historique json du projet",ioe);
        }

        return loadedProject;
    }

}
