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
package sonicScream.utilities;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class ScriptParserTest
{

    private final URL FOLDER_LOCATION = getClass().getResource("/sonicScream/assets/test");
    private final Path _scriptFolder;
    private final List<Path> _scriptFiles;

    public ScriptParserTest() throws URISyntaxException, IOException
    {
        _scriptFolder = Paths.get(FOLDER_LOCATION.toURI());
        _scriptFiles = FilesEx.listFiles(_scriptFolder);
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
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of parseScript method, of class ScriptParser.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testParseScript() throws Exception
    {
        _scriptFiles.stream().forEach((f) ->
        {
            try
            {                
                ScriptParser.parseScript(f, f.getFileName().toString());
            }            
            catch (Exception ex)
            {
                fail("Failed to parse " + f.getFileName() + " due to: " + ex.getMessage());
            }
        });
    }
    
    @Test    
    public void testParseScript_HandlesFirstTwoLines() throws Exception
    {
        Path file = _scriptFiles
                .stream()
                .filter(f -> f.getFileName().toString().equals("game_sounds_vo_abaddon.vsndevts_c"))
                .findFirst().get();        
        TreeItem<String> tree = ScriptParser.parseScript(file, file.getFileName().toString());
        assertEquals("\"abaddon_abad_spawn_01\"", tree.getChildren().get(0).getValue()); //first child
        assertEquals("\"operator_stacks\"", tree.getChildren().get(0).getChildren().get(0).getValue()); //first child's child
    }       

    /**
     * Test of parseScriptTreeToString method, of class ScriptParser.
     */
    @Test
    public void testParseScriptTreeToString() throws IOException
    {
        for(Path file : _scriptFiles)
        {
            TreeItem<String> tree = ScriptParser.parseScript(file, file.getFileName().toString());
            String string = ScriptParser.parseScriptTreeToString(tree);
        }        
    }   
}
