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

import java.awt.color.ProfileDataException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.rules.ExpectedException;
import sonicScream.models.Profile;
import sonicScream.utilities.Constants;

/**
 *
 * @author nmca
 */
public class SettingsServiceTest
{
    private SettingsService _testService;
    
    Path settingsFile;
    Path crcFile;
    Path profileDir;
    
    Profile testProfile1 = new Profile("Profile 1");
    Profile testProfileZ = new Profile("Profile Z");
    Profile testProfileWeirdChar = new Profile("Profile äãöâö");
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

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
    public void setUp() throws IOException, ProfileNameExistsException
    {
        Path testFolder = Paths.get("test");
        if(Files.exists(testFolder))
        {
            FileUtils.deleteDirectory(testFolder.toFile());            
        }                
        
        testFolder = Files.createDirectory(testFolder);
        
        //TODO: Replace these with test settings files
        settingsFile = Paths.get(testFolder.toString(), Constants.SETTINGS_FILE_NAME);
        if (!Files.exists(settingsFile))
        {
            settingsFile = Files.createFile(settingsFile);
        }
        crcFile = Paths.get(testFolder.toString(), Constants.CRC_CACHE_FILE_NAME);
        if (!Files.exists(crcFile))
        {
            crcFile = Files.createFile(crcFile);
        }
        profileDir = Paths.get(testFolder.toString(), Constants.PROFILE_FILES_DIRECTORY);
        if (!Files.exists(profileDir))
        {
            profileDir = Files.createDirectory(profileDir);
        }
        _testService = new SettingsService(settingsFile, crcFile, profileDir);                
        
        _testService.addProfile(testProfile1);
        _testService.addProfile(testProfileZ);
        _testService.addProfile(testProfileWeirdChar);
    }

    @After
    public void tearDown()
    {
    }

    /**
     * Test of getProfile method, of class SettingsService.
     */
    @Test
    public void testGetProfile()
    {
        Profile expResult = testProfile1;
        Profile result = _testService.getProfile("Profile 1");
        assertEquals(testProfile1, result);
        
        expResult = testProfileZ;
        result = _testService.getProfile("Profile Z");
        assertEquals(expResult, result);
        
        expResult = testProfileWeirdChar;
        result = _testService.getProfile("Profile äãöâö");
        assertEquals(expResult, result);
    }
    
    @Test
    public void testGetProfile_invalidNameReturnsNull()
    {        
        assertEquals(_testService.getProfile("Doesn't Exist"), null);
    }

    /**
     * Test of getAllProfiles method, of class SettingsService.
     */
    @Test
    public void testGetAllProfiles()
    {
       ArrayList<Profile> expResult = new ArrayList<Profile>();
       expResult.add(testProfile1);
       expResult.add(testProfileZ);
       expResult.add(testProfileWeirdChar);
       
        assertEquals(_testService.getAllProfiles(), expResult);
    }

    /**
     * Test of addProfile method, of class SettingsService.
     * @throws java.lang.Exception
     */
    @Test
    public void testAddProfile() throws Exception
    {
        _testService.addProfile(new Profile("test"));
    }
    
    @Test
    public void addProfile_throwsOnDuplicateProfile() throws ProfileNameExistsException
    {
        exception.expect(ProfileDataException.class);
        _testService.addProfile(new Profile("Profile 1"));        
    }
    
    /**
     * Test of deleteProfile method, of class SettingsService.
     */
    @Test
    public void testDeleteProfile()
    {
        _testService.deleteProfile("Profile 1");
        Profile result = _testService.getProfile("Profile 1");
        assertEquals(result, null);
    }
    
    @Test
    public void deleteProfile_handlesInvalidName()
    {
        _testService.deleteProfile("Doesn't Exist");
    }

    /**
     * Test of saveSettings method, of class SettingsService.
     * @throws sonicScream.services.ProfileNameExistsException
     * @throws java.io.IOException
     */
    @Test
    public void testSaveSettings() throws ProfileNameExistsException, IOException
    {                
        _testService.putSetting("testSetting", "1");
        _testService.putCrc("testCrc", 214980124);
        
        _testService.saveSettings("test");               
        
        Map<String, String> expSettings = _testService.getReadonlySettings();
        Map<String, Long> expCrcs = _testService.getReadonlyCRCs();
        List<Profile> expProfiles = _testService.getAllProfiles();
        
        SettingsService result = new SettingsService(settingsFile, crcFile, profileDir);
        Map<String, String> resultSettings = result.getReadonlySettings();
        Map<String, Long> resultCrcs = result.getReadonlyCRCs();
        List<Profile> resultProfiles = result.getAllProfiles();
        
        assertEquals(expSettings, resultSettings);
        assertEquals(expCrcs, resultCrcs);
        assertEquals(expProfiles.size(), resultProfiles.size());
        for(int i = 0; i < expProfiles.size(); i++)
        {
            assertEquals(expProfiles.get(i), resultProfiles.get(i));
        }
    }

}
