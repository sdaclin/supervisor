/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.supervisor.serializer;

import fr.supervisor.analyzer.SupervisorTest;
import fr.supervisor.model.Project;
import org.junit.Test;
import serializer.ProjectSerializer;

/**
 *
 * @author vpoulin
 */
public class SerializerTest {
    @Test
    public void testRun() throws Exception {
        
        SupervisorTest superTest = new SupervisorTest(); 
        superTest.testRun();
        Project project = ProjectSerializer.loadProject("D://test_siclop.json");
        System.out.println("----------------LOADED------------- \n"+project.toString());
    }
}
