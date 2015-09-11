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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final File _scriptFolder;
    private final ArrayList<File> _scriptFiles;

    public ScriptParserTest() throws URISyntaxException
    {
        _scriptFolder = new File(FOLDER_LOCATION.toURI());
        _scriptFiles = new ArrayList<>(Arrays.asList(_scriptFolder.listFiles()));
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
                ScriptParser.parseScript(f);
            }
            catch (IOException ex)
            {
                fail("Failed to parse " + f.getName() + " due to: " + ex.getMessage());
            }
            catch (Exception ex)
            {
                fail("Failed to parse " + f.getName() + " due to: " + ex.getMessage());
            }
        });
    }

}
