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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import info.ata4.vpk.VPKEntry;
import javafx.application.Platform;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;
import sonicScream.models.Category;
import sonicScream.models.Profile;
import sonicScream.utilities.Constants;
import testHelpers.JavaFXThreadingRule;

/**
 *
 * @author nmca
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingsServiceTest
{
    private static final String EVIL_STRING = "`⁄€‹›ﬁﬂ‡°·‚—±ÅÍÎÏ˝ÓÔFÒÚÆ☃";

    private SettingsService _testService;
    
    private static Path settingsFile;
    private static Path crcFile;
    private static Path profileDir;
    
    Profile defaultProfile;
    Profile testProfile1;
    Profile testProfileZ;
    Profile testProfileWeirdChar;

    VPKFileService vpkFileService = mock(VPKFileService.class);
    List<String> mockPaths = new ArrayList<String>();
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Rule
    public JavaFXThreadingRule jfxRule = new JavaFXThreadingRule();

    public SettingsServiceTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws IOException
    {
        Path testFolder = Paths.get("test");
        Path profilesFolder = Paths.get("test", "profiles");
        if(Files.exists(testFolder))
        {
            FileUtils.deleteDirectory(profilesFolder.toFile());
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
    }

    @AfterClass
    public static void tearDownClass() throws IOException, InterruptedException
    {
        Thread.sleep(2000);        
        Path testFolder = Paths.get("test");
        Path profilesFolder = Paths.get("test", "profiles");
        if(Files.exists(testFolder))
        {
            try{
                FileUtils.deleteDirectory(profilesFolder.toFile());
            FileUtils.deleteDirectory(testFolder.toFile());            
            }
            catch(IOException ex)
            {
                System.out.println(ex.getMessage());
                ex.printStackTrace();                
            }
        }        
    }

    @Before
    public void setUp() throws IOException, ProfileNameExistsException
    {
        mockPaths.add("/somepath/wherever");

        VPKEntry mockVPKEntry = mock(VPKEntry.class);
        when(mockVPKEntry.getName()).thenReturn("Somename");
        when(mockVPKEntry.getType()).thenReturn("vsndevts");
        when(mockVPKEntry.getPath()).thenReturn("/somepath/wherever/Somename.vsndevts");

        List<VPKEntry> mockVPKList = new ArrayList<>();
        mockVPKList.add(mockVPKEntry);
        when(vpkFileService.getVPKEntriesInDirectory(anyString())).thenReturn(mockVPKList);
        when(vpkFileService.getVPKEntry(anyString())).thenReturn(mockVPKEntry);

        ArrayList<Category> categories = new ArrayList<>();

        _testService = new SettingsService(settingsFile, crcFile, profileDir);

        defaultProfile = new Profile("Default", "The default profile", vpkFileService);
        testProfile1 = new Profile("Profile 1", "Profile numbah one!", vpkFileService);
        testProfileZ = new Profile("ZProfile", "Profile with a Z?", vpkFileService);
        testProfileWeirdChar = new Profile(EVIL_STRING, "Profile with badly-behaved chars", vpkFileService);

        _testService.addProfile(defaultProfile);
        _testService.addProfile(testProfile1);
        _testService.addProfile(testProfileZ);
        _testService.addProfile(testProfileWeirdChar);
        
    }

    @After
    public void tearDown() throws IOException
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
        result = _testService.getProfile("ZProfile");
        assertEquals(expResult, result);
        
        expResult = testProfileWeirdChar;
        result = _testService.getProfile(EVIL_STRING);
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
       expResult.add(defaultProfile);
       expResult.add(testProfile1);
       expResult.add(testProfileZ);
       expResult.add(testProfileWeirdChar);
       
       boolean equal = Arrays.deepEquals(expResult.toArray(), _testService.getAllProfiles().toArray());
       assertEquals(equal, true);
    }

    /**
     * Test of addProfile method, of class SettingsService.
     * @throws java.lang.Exception
     */
    @Test
    public void testAddProfile() throws Exception
    {
        _testService.addProfile(new Profile("test", "test description", vpkFileService));
    }
    
    @Test
    public void addProfile_throwsOnDuplicateProfile() throws ProfileNameExistsException
    {
        exception.expect(ProfileNameExistsException.class);
        _testService.addProfile(new Profile("Profile 1", "profile 1", vpkFileService));
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
     * Test of saveSettings method, of class SettingsService. Occurs last, because
     * it causes side effects that affect the rest of the tests.
     * @throws sonicScream.services.ProfileNameExistsException
     * @throws java.io.IOException
     */
    @Test
    public void z_testSaveSettings() throws ProfileNameExistsException, IOException, InterruptedException {
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
        assertEquals(expProfiles, resultProfiles);
    }
}
