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
package sonicScream.services;

import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import info.ata4.vpk.VPKException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

/**
 *
 * @author nmca
 */
public class VPKFileServiceTest
{        
    private VPKFileService _testService;
    private VPKEntry _testVPKEntry;
    private VPKArchive _testVPKArchive;
    List<VPKEntry> _manyTestVPKs = new ArrayList<>();
    
    public VPKFileServiceTest()
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
    public void setUp() throws VPKException, IOException
    {        
        _testVPKEntry = mock(VPKEntry.class);
        _testVPKArchive = mock(VPKArchive.class);
                
        _manyTestVPKs.add(_testVPKEntry);
        _manyTestVPKs.add(_testVPKEntry);
        when(_testVPKArchive.getEntry(Mockito.anyString())).thenReturn(_testVPKEntry);
        when(_testVPKArchive.getEntriesForDir(Mockito.anyString())).thenReturn(_manyTestVPKs); 
                
        _testService = new VPKFileService(_testVPKArchive);           
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getVPKEntry method, of class VPKFileService.
     */
    @Test
    public void testGetVPKEntry() throws Exception
    {
        VPKEntry result = _testService.getVPKEntry("anywhere");
        verify(_testVPKArchive).load(Mockito.any());     
        verify(_testVPKArchive).getEntry("anywhere");
        assertEquals(result, _testVPKEntry);
    }

    /**
     * Test of getVPKEntries method, of class VPKFileService.
     */
    @Test
    public void testGetVPKEntries() throws Exception
    {
        ArrayList<String> vpkPaths = new ArrayList<>();
        vpkPaths.add("anything");
        vpkPaths.add("anything");
                
        List<VPKEntry> result = _testService.getVPKEntries(vpkPaths);
        verify(_testVPKArchive).load(Mockito.any());               
        verify(_testVPKArchive, times(2)).getEntry("anything");
        boolean equal = Arrays.deepEquals(result.toArray(), _manyTestVPKs.toArray());
        assertEquals(equal, true);
    }

    /**
     * Test of getVPKEntriesInDirectory method, of class VPKFileService.
     */
    @Test
    public void testGetVPKEntriesInDirectory() throws Exception
    {
        List<VPKEntry> result = _testService.getVPKEntriesInDirectory("anywhere");
        verify(_testVPKArchive).load(Mockito.any());
        verify(_testVPKArchive).getEntriesForDir("anywhere");
        boolean equals = Arrays.deepEquals(result.toArray(), _manyTestVPKs.toArray());
        assertEquals(equals, true);
        
    }
    
}
