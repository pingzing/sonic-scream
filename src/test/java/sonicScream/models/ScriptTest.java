/*
 * The MIT License
 *
 * Copyright 2015 nmca.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package sonicScream.models;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import sonicScream.utilities.Constants;
import sonicScream.utilities.FilesEx;
import sonicScream.utilities.ScriptParser;
import sonicScream.utilities.TreeUtils;

/**
 * By default, most of these tests will run through every single file in sonicScream/assets/test. Feel free to remove some
 * files from there if you don't feel like being ridiculously exhaustive in running Script through the gauntlet.
 * @author nmca
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScriptTest
{
    private static final URL FOLDER_LOCATION = ScriptTest.class.getResource("/sonicScream/assets/test/");
    private static List<Path> allTestScriptFiles = new ArrayList<Path>();
    private static List<Script> allTestScripts = new ArrayList<Script>();
    
    public ScriptTest() throws URISyntaxException
    {
    }
    
    @BeforeClass
    public static void setUpClass() throws URISyntaxException, IOException
    {        
        allTestScriptFiles.addAll(FilesEx.listFiles(Paths.get(FOLDER_LOCATION.toURI())));
        Category mockCategory = mock(Category.class);
        when(mockCategory.categoryNameProperty()).thenReturn(new SimpleStringProperty("Test Category"));
        for(Path p : allTestScriptFiles)
        {
            Script script = new Script(p, mockCategory);
            allTestScripts.add(script);
        }
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {        
    }
    
    @After
    public void tearDown() throws IOException
    {
        Files.deleteIfExists(Paths.get("testProfile.xml"));
        Files.deleteIfExists(Paths.get("testProfiles.xml"));
    }

    /**
     * Test of getScriptName method, of class Script.
     */
    @Test
    public void testGetScriptName()
    {
        allTestScripts.stream().forEach(s -> s.getFriendlyScriptName());
        //not sure how to adeqautely assert that these are all correct, honestly
    }

    /**
     * Test of toString method, of class Script.
     */
    @Test
    public void testToString()
    {        
        allTestScripts.stream().forEach(s -> {
            assertEquals(s.toString(), s.getFriendlyScriptName());
        });
    }

    /**
     * Test of getRootNode method, of class Script.
     */
    @Test
    public void testGetRootNode() throws IOException
    {
        for(int i = 0; i < allTestScripts.size(); i++)
        {
            Script testScript = allTestScripts.get(i);
            Path testScriptFile = allTestScriptFiles.get(i);
            TreeItem<String> expResult = ScriptParser.parseScript(testScriptFile, testScriptFile.getFileName().toString());
            TreeItem<String> result = testScript.getRootNode();
            boolean treesEqual = ScriptParser.areScriptTreesEqual(result, expResult);
            assertEquals(treesEqual, true);
        }

    }

    /**
     * Test of getScriptAsString method, of class Script.
     */
    @Test
    public void testGetScriptAsString() throws IOException
    {
        for(int i = 0; i < allTestScripts.size(); i++)
        {
            Script testScript = allTestScripts.get(i);
            Path testScriptFile = allTestScriptFiles.get(i);
            TreeItem<String> inTree = ScriptParser.parseScript(testScriptFile, testScriptFile.getFileName().toString());
            String expResult = ScriptParser.parseScriptTreeToString(inTree);
            String result = testScript.getScriptAsString();
            assertEquals(expResult, result);
        }
    }
    
    @Test
    public void testScriptSerialization()
    {
        allTestScripts.stream().forEach(s ->
        {
            try
            {
                JAXBContext context = JAXBContext.newInstance(Script.class);
                Marshaller m = context.createMarshaller();
                Unmarshaller um = context.createUnmarshaller();

                Path xmlFile = Paths.get("testProfile.xml");
                m.marshal(s, xmlFile.toFile());

                Script result = (Script) um.unmarshal(xmlFile.toFile());
                assertEquals(result, s);
            } catch (Exception ex)
            {
                fail("Failed to deserialize Profile");
            }
        });
    }

    @Test
    public void testGetSimpleTree()
    {
        allTestScripts.stream().forEach(s ->
        {
            TreeItem<String> simpleTree = s.getSimpleTree();
            TreeItem<String> originalTree = s.updateRootNodeWithSimpleTree();
            TreeItem<String> secondSimpleTree = s.getSimpleTree();
            assertEquals(simpleTree, secondSimpleTree);            
        });
    }

    /**
     * Tested last using the class annotation, because it causes side effects 
     * on the loaded scripts.
     */
    @Test
    public void z_testUpdateRootNameWithSimpleTree()
    {
        allTestScripts.stream().forEach(s -> 
        {
            try
            {
                TreeItem<String> simple = s.getSimpleTree();
                TreeItem<String> simpleNode = simple.getChildren().get(0).getChildren().get(0);
                String simpleSoundString = "some_test_sound.mp3";            
                String value = "some_test_sound.mp3";

                simpleNode.setValue(value);
                s.updateRootNodeWithSimpleTree();

                String rootSoundString = TreeUtils.getWaveStrings(s.getRootNode()).get().get(0).getValue();
                String soundPrefix = StringUtils.substringBetween(rootSoundString, "\"", "\"");
                soundPrefix = soundPrefix.substring(0, soundPrefix.length() - 1); //Remove the number from the prefix
                String expected = "\"" + soundPrefix + "0\" \"" + simpleSoundString + "\"";
                String actual = TreeUtils.getWaveStrings(s.getRootNode()).get().get(0).getValue();
                assertEquals(expected, actual);
            }
            catch(Exception ex)
            {
                System.err.println("testUpdateRootNameWithSimpleTree failed on script " + s.toString() +" : " + ex.getMessage());
            }
        });        
    }
}
