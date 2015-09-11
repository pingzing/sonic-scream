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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import javafx.scene.control.TreeItem;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nmca
 */
public class ScriptTest
{
    private final URL FOLDER_LOCATION = getClass().getResource("/sonicScream/assets/test/game_sounds_vo_nevermore.vsndevts_c");    
    private final File _testScriptFile;
    private Script _testScript;
    
    public ScriptTest() throws URISyntaxException
    {
        _testScriptFile = new File(FOLDER_LOCATION.toURI());        
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
        _testScript = new Script(_testScriptFile);
    }
    
    @After
    public void tearDown()
    {
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
     * Test of getRawScriptName method, of class Script.
     */
    @Test
    public void testGetRawScriptName()
    {        
        String expResult = "game_sounds_vo_nevermore.vsndevts_c";
        String result = _testScript.getRawScriptName();
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
    
}
