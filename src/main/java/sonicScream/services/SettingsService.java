/*
 * The MIT License
 *
 * Copyright 2015 Neil McAlister.
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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.KXml2Driver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import sonicScream.models.Profile;
import sonicScream.utilities.Constants;
import sonicScream.utilities.FilesEx;

public class SettingsService 
{
    private final Map<String, String> _settingsDictionary;
    private final Map<String, Long> _crcDictionary;
    private final List<Profile> _profileList;
    
    public Map<String, String> getReadonlySettings() { return Collections.unmodifiableMap(_settingsDictionary); }
    public Map<String, Long> getReadonlyCRCs() { return Collections.unmodifiableMap(_crcDictionary); }    
    
    public SettingsService(Path settingsFile, Path crcFile, Path profilesDirectory) throws IOException
    {        
        XStream stream = new XStream(new KXml2Driver());
        if(Files.size(settingsFile) == 0)
        {
            _settingsDictionary = new HashMap<>();
        }
        else
        {
            _settingsDictionary = (Map<String, String>)stream.fromXML(Files.newInputStream(settingsFile));
        }
        
        if(Files.size(crcFile) == 0)
        {
            _crcDictionary = new HashMap<>();
        }
        else
        {
            _crcDictionary = (Map<String, Long>)stream.fromXML(Files.newInputStream(crcFile));
        }        
        
        List<Path> profiles = FilesEx.listFiles(profilesDirectory);
        _profileList = new ArrayList();
        if(profiles.isEmpty())
        {            
            _profileList.add(new Profile());
        }
        else
        {
            profiles.stream().forEach(p ->
            {
                try
                {
                    _profileList.add((Profile)stream.fromXML(Files.newInputStream(p)));
                }
                catch(IOException ex)
                {
                    System.err.printf("\nUnable to read profile %s:", p, ex.getMessage());
                }
            });            
        }
    }
    
    public String getSetting(String setting)
    {
        return _settingsDictionary.get(setting);
    }
    
    /**
     * Inserts a new setting with key setting and value newValue. If there is 
     * already a key with the same name, the old value will be replaced.
     * @param setting The key of the setting to add or alter.
     * @param newValue The value of the setting that will be added or altered.
     */
    public void putSetting(String setting, String newValue)
    {
        _settingsDictionary.put(setting, newValue);
    }       
    
    
    public long getCrc(String fileName)
    {
        return _crcDictionary.get(fileName);
    }
    
    /**
     * Inserts a new CRC32 with key fileName and value crc. If there is 
     * already a key with the same name, the old value will be replaced. 
     * @param fileName The key of the CRC32 to add or alter.
     * @param crc The value of the CRC32 that will be added or altered.     
     */
    public void putCrc(String fileName, long crc)
    {
        _crcDictionary.put(fileName, crc);
    }
    
    /**
     * Attempts to get profile from the internal profile list by name. 
     * Returns null if no such profile is found.
     * @param profileName The name of the profile to search for.
     * @return The Profile in question, or null if no profile is found.
     */
    public Profile getProfile(String profileName)
    {
        return _profileList.stream()
                .filter(p -> p.getProfileName().equals(profileName))
                .findFirst()
                .orElse(null);
    }
    
    public List<Profile> getAllProfiles()
    {
        return Collections.unmodifiableList(_profileList);
    }
    
    public void addProfile(Profile profileToAdd) throws ProfileNameExistsException
    {
        if(_profileList.stream().anyMatch(
                p -> p.getProfileName().equals(profileToAdd.getProfileName())))
        {            
            throw new ProfileNameExistsException("A profile named " + profileToAdd.getProfileName() + " already exists.");
        }
        else
        {
            _profileList.add(profileToAdd);            
        }
    }
    
    public void updateProfile(Profile updated)
    {
        Profile currentProfile = getProfile(updated.getProfileName());
        _profileList.remove(currentProfile);
        _profileList.add(updated);
    }
    
    public void deleteProfile(String profileToDelete)
    {
        if(_profileList.stream().anyMatch(p -> p.getProfileName().equals(profileToDelete)))
        {
            int toDelete = _profileList.indexOf(_profileList.stream()
                    .filter(p -> p.getProfileName().equals(profileToDelete)));
            _profileList.remove(toDelete);
        }
    }
    
    /**
     * Serializes all current settings objects out to disk, to the current directory.
     */
    public void saveSettings()
    {
        saveSettings("");
    }
    
    /**
     *Serializes all current settings objects out to disk, to the specified directory, 
     * relative to the current directory. A folder separator is automatically 
     * appended to the passed string.
     * @param pathToWriteTo The name, or relative path of the folder to write to, without a trailing slash.
     */
    public void saveSettings(String pathToWriteTo)
    {
        XStream serializer = new XStream(new KXml2Driver());
        Path settingsFile = Paths.get(pathToWriteTo, File.separator, Constants.SETTINGS_FILE_NAME);
        try(BufferedWriter bw = Files.newBufferedWriter(settingsFile, StandardOpenOption.CREATE))
        {
            serializer.toXML(_settingsDictionary, bw);
        }
        catch(IOException ex)
        {
            System.err.println("Failed to write out " + Constants.SETTINGS_FILE_NAME);
        }
        
        Path crcFile = Paths.get(pathToWriteTo, File.separator, Constants.CRC_CACHE_FILE_NAME);
        try(BufferedWriter bw = Files.newBufferedWriter(crcFile, StandardOpenOption.CREATE))
        {
            serializer.toXML(_crcDictionary, bw);
        }
        catch(IOException ex)
        {
            System.err.println("Failed to write out " + Constants.CRC_CACHE_FILE_NAME);
        }
        
        for(Profile profile : _profileList)
        {
            Path profilePath = Paths.get(pathToWriteTo, File.separator, 
                    Constants.PROFILE_FILES_DIRECTORY, File.separator,
                    profile.getProfileName() + "_" + Constants.PROFILE_FILE_SUFFIX);
            try(BufferedWriter bw = Files.newBufferedWriter(profilePath))
            {
                serializer.toXML(profile, bw);
            }
            catch(IOException ex)
            {
                System.err.printf("\nFailed to write out profile: %s " + ex.getMessage());
            }
        }
               
    }
}
