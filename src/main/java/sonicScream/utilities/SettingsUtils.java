/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sonicScream.utilities;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class used for retrieving derived settings, or settings that don't need to be
 * saved, and thus don't belong in the SettingsService.
 * @author Neil McAlister
 */
public class SettingsUtils 
{
    public static Path getProfileDirectory(String profileName)
    {
        return Paths.get(Constants.PROFILES_DIRECTORY, File.separator, profileName);
    }
}
