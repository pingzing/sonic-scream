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
import javafx.scene.control.TreeItem;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sonicScream.utilities.Constants;
import sonicScream.utilities.ScriptParser;

/**
 *
 * @author nmca
 */
public class ScriptTest
{
    private final URL FOLDER_LOCATION = getClass().getResource("/sonicScream/assets/test/game_sounds_vo_nevermore.vsndevts_c");    
    private final Path _testScriptFile;
    private Script _testScript;
    
    public ScriptTest() throws URISyntaxException
    {
        _testScriptFile = Paths.get(FOLDER_LOCATION.toURI());
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {                
        _testScript = new Script(_testScriptFile, new Category(Constants.CATEGORY_VOICES));
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
        String expResult = "nevermore";
        String result = _testScript.getScriptName();
        assertEquals(expResult, result);        
    }

    /**
     * Test of getRawFileName method, of class Script.
     */
    @Test
    public void testGetRawScriptName()
    {        
        String expResult = "game_sounds_vo_nevermore.vsndevts_c";
        String result = _testScript.getRawFileName();
        assertEquals(expResult, result);        
    }

    /**
     * Test of toString method, of class Script.
     */
    @Test
    public void testToString()
    {        
        String expResult = "Shadow Fiend";
        String result = _testScript.toString();
        assertEquals(expResult, result);        
    }

    /**
     * Test of getRootNode method, of class Script.
     */
    @Test
    public void testGetRootNode() throws IOException
    {                        
        TreeItem<String> expResult = ScriptParser.parseScript(_testScriptFile, _testScriptFile.getFileName().toString());
        
        TreeItem<String> result = _testScript.getRootNode();
        
        boolean treesEqual = ScriptParser.areScriptTreesEqual(result, expResult);
        
        assertEquals(treesEqual, true);        
            
    }

    /**
     * Test of getScriptAsString method, of class Script.
     */
    @Test
    public void testGetScriptAsString() throws IOException
    {        
        TreeItem<String> inTree = ScriptParser.parseScript(_testScriptFile, _testScriptFile.getFileName().toString());
        String expResult = ScriptParser.parseScriptTreeToString(inTree);                                
        
        String result = _testScript.getScriptAsString();
                
        assertEquals(expResult, result);        
    }
    
    @Test
    public void testScriptSerialization()
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(Script.class);
            Marshaller m = context.createMarshaller();
            Unmarshaller um = context.createUnmarshaller();

            Path xmlFile = Paths.get("testProfile.xml");
            m.marshal(_testScript, xmlFile.toFile());

            Script result = (Script)um.unmarshal(xmlFile.toFile());
            assertEquals(result, _testScript);
        }
        catch(Exception ex)
        {
            fail("Failed to deserialize Profile");
        }
    }
    
    @Test
    public void testScriptListSerialization()
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance(ScriptListWrapper.class);
            Marshaller m = context.createMarshaller();
            Unmarshaller um = context.createUnmarshaller();
            
            ArrayList<Script> manyScripts = new ArrayList<>();
            manyScripts.add(_testScript);
            manyScripts.add(_testScript);
            manyScripts.add(_testScript);
            ScriptListWrapper wrapper = new ScriptListWrapper();
            wrapper.setScriptList(manyScripts);
            
            Path xmlFile = Paths.get("testProfiles.xml");
            m.marshal(wrapper, xmlFile.toFile());
            
            ArrayList<Script> result = ((ScriptListWrapper)um.unmarshal(xmlFile.toFile())).getScriptList();
            assertEquals(result, manyScripts);
                        
        }
        catch(Exception ex)
        {
            fail("Failed to deserialize ProfileList");
        }
    }
}
