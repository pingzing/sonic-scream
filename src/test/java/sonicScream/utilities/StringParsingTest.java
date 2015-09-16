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
public class StringParsingTest
{
    
    public StringParsingTest()
    {
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
     * Test of getScriptNameFromFileName method, of class StringParsing.
     */
    @Test
    public void testGetScriptNameFromFileName()
    {        
        String fileName = "game_sounds_vo_announcer_dlc_axe_killing_spree.vsndevts_c";
        String expResult = "announcer_dlc_axe_killing_spree";
        String result = StringParsing.getScriptNameFromFileName(fileName);
        assertEquals(expResult, result);        
    }

    /**
     * Test of prettyFormatScriptName method, of class StringParsing.
     */
    @Test
    public void testPrettyFormatScriptName()
    {        
        String name = "announcer_dlc_axe_killing_spree";
        String expResult = "Announcer Dlc Axe Killing Spree";
        String result = StringParsing.prettyFormatScriptName(name);
        assertEquals(expResult, result);        
    }

    /**
     * Test of handleSpecialCaseName method, of class StringParsing.
     */
    @Test
    public void testHandleSpecialCaseName()
    {
        System.out.println("handleSpecialCaseName");
        String name = "";
        String expResult = "";
        String result = StringParsing.handleSpecialCaseName(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
