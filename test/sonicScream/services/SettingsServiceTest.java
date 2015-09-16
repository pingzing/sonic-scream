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

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import sonicScream.models.Profile;

/**
 *
 * @author nmca
 */
public class SettingsServiceTest
{
    
    public SettingsServiceTest()
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
     * Test of getSetting method, of class SettingsService.
     */
    @Test
    public void testGetSetting()
    {
        System.out.println("getSetting");
        String setting = "";
        SettingsService instance = null;
        String expResult = "";
        String result = instance.getSetting(setting);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putSetting method, of class SettingsService.
     */
    @Test
    public void testPutSetting()
    {
        System.out.println("putSetting");
        String setting = "";
        String newValue = "";
        SettingsService instance = null;
        instance.putSetting(setting, newValue);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCrc method, of class SettingsService.
     */
    @Test
    public void testGetCrc()
    {
        System.out.println("getCrc");
        String fileName = "";
        SettingsService instance = null;
        long expResult = 0L;
        long result = instance.getCrc(fileName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putCrc method, of class SettingsService.
     */
    @Test
    public void testPutCrc()
    {
        System.out.println("putCrc");
        String fileName = "";
        long crc = 0L;
        SettingsService instance = null;
        instance.putCrc(fileName, crc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProfile method, of class SettingsService.
     */
    @Test
    public void testGetProfile()
    {
        System.out.println("getProfile");
        String profileName = "";
        SettingsService instance = null;
        Profile expResult = null;
        Profile result = instance.getProfile(profileName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllProfiles method, of class SettingsService.
     */
    @Test
    public void testGetAllProfiles()
    {
        System.out.println("getAllProfiles");
        SettingsService instance = null;
        List<Profile> expResult = null;
        List<Profile> result = instance.getAllProfiles();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addProfile method, of class SettingsService.
     */
    @Test
    public void testAddProfile() throws Exception
    {
        System.out.println("addProfile");
        Profile profileToAdd = null;
        SettingsService instance = null;
        instance.addProfile(profileToAdd);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateProfile method, of class SettingsService.
     */
    @Test
    public void testUpdateProfile()
    {
        System.out.println("updateProfile");
        Profile updated = null;
        SettingsService instance = null;
        instance.updateProfile(updated);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteProfile method, of class SettingsService.
     */
    @Test
    public void testDeleteProfile()
    {
        System.out.println("deleteProfile");
        String profileToDelete = "";
        SettingsService instance = null;
        instance.deleteProfile(profileToDelete);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveSettings method, of class SettingsService.
     */
    @Test
    public void testSaveSettings()
    {
        System.out.println("saveSettings");
        SettingsService instance = null;
        instance.saveSettings();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
