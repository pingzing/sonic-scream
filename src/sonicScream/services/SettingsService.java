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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import sonicScream.models.Profile;
import sonicScream.utilities.Constants;

public class SettingsService 
{
    private final Map<String, String> _settingsDictionary;
    private final Map<String, Long> _crcDictionary;
    private final List<Profile> _profileList;
    
    public SettingsService(File settingsFile, File crcFile, File profilesFile)
    {        
        XStream stream = new XStream(new KXml2Driver());
        if(settingsFile.length() == 0)
        {
            _settingsDictionary = new HashMap<>();
        }
        else
        {
            _settingsDictionary = (Map<String, String>)stream.fromXML(settingsFile);
        }
        
        if(crcFile.length() == 0)
        {
            _crcDictionary = new HashMap<>();
        }
        else
        {
            _crcDictionary = (Map<String, Long>)stream.fromXML(crcFile);
        }        
        
        if(profilesFile.length() == 0)
        {
            _profileList = new ArrayList();
            _profileList.add(new Profile());
        }
        else
        {
            _profileList = (List<Profile>)stream.fromXML(profilesFile);
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
    
    public Profile getProfile(String profileName)
    {
        return _profileList.stream()
                .filter(p -> p.getProfileName().equals(profileName))
                .findFirst().get();
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
            _profileList.add(profileToAdd);
        }
        else
        {
            throw new ProfileNameExistsException("A profile named " + profileToAdd.getProfileName() + " already exists.");
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
    
    public void saveSettings()
    {
        XStream serializer = new XStream(new KXml2Driver());
        File settingsFile = new File(Constants.SETTINGS_FILE_NAME);
        try(FileOutputStream fos = new FileOutputStream(settingsFile))
        {
            serializer.toXML(_settingsDictionary, fos);
        }
        catch(IOException ex)
        {
            System.err.println("Failed to write out " + Constants.SETTINGS_FILE_NAME);
        }
        
        File crcFile = new File(Constants.CRC_CACHE_FILE_NAME);
        try(FileOutputStream fos = new FileOutputStream(crcFile))
        {
            serializer.toXML(_crcDictionary, fos);
        }
        catch(IOException ex)
        {
            System.err.println("Failed to write out " + Constants.CRC_CACHE_FILE_NAME);
        }
        
        File profilesFile = new File(Constants.PROFILES_FILE_NAME);
        try(FileOutputStream fos = new FileOutputStream(profilesFile))
        {
            serializer.toXML(_profileList, fos);
        }
        catch(IOException ex)
        {
            System.err.println("Failed to write out " + Constants.CRC_CACHE_FILE_NAME);
        }
    }
}
