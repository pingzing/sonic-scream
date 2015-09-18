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

import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import info.ata4.vpk.VPKException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VPKFileService 
{
    private String _vpkPath = "";
    private String _vpkParentDirectory = "";
    private boolean _vpkLoaded = false;
    private VPKArchive _vpk;
    
    private void loadVPK()
    {
        if(!_vpkLoaded)
        {
            try
            {
                _vpk.load(new File(_vpkPath));
            }            
            catch(IOException ex)
            {
                //TODO: Display error message
                ex.printStackTrace();
            }
            _vpkLoaded = true;
        }
    }
    
    /**
     * Primarily used for testing. We don't really want the caller to be
     * responsible for finding the VPKs.
     * @param vpk 
     */
    public VPKFileService(VPKArchive vpk)
    {
        _vpk = vpk;
    }
    
    /**
     * Constructs a new VPKFileService, but does NOT load the VPK.
     * @param vpkPath
     * @param vpkParentDir 
     */
    public VPKFileService(String vpkPath, String vpkParentDir)
    {
        _vpkPath = vpkPath;
        _vpkParentDirectory = vpkParentDir;
    }

    /**
     * Attempts to the get the VPK entry at the provided path. Lazily-loads the 
     * VPK Archive. Returns null if the VPKArchive fails to load, or no entry is found.
     * @param _vpkPath The internal VPK path to search.
     * @return The entry in question, or null of nothing is found, or the VPKArchive fails to load.
     */
    public VPKEntry getVPKEntry(String _vpkPath)
    {
        loadVPK();
        if(!_vpkLoaded)
        {
            return null;
        }
                            
        return _vpk.getEntry(_vpkPath);                                
    }
    
    /**
     * Attempts to get all VPKEntries at the provided paths. Lazily-loads the VPK Archive.
     * Returns null if the VPKArchive fails to load, or no entries are found.
     * @param _vpkPaths A list of fully-qualified internal VPK paths.
     * @return a List of entries, or null if nothing is found, or the VPKArchive fails to load.
     */
    public List<VPKEntry> getVPKEntries(List<String> _vpkPaths)
    {
        loadVPK();
        if(!_vpkLoaded)
        {
            return null;
        }
        
        ArrayList<VPKEntry> entryList = new ArrayList<>();
        for(String path : _vpkPaths)
        {
          entryList.add(_vpk.getEntry(path));
        }
        return entryList;
    }
    
    /**
     * Attempts to return all inside the provided internal directory. Lazily-loads 
     * the VPKArchive. Returns null if the VPKArchive fails to load, the 
     * directory doesn't exist, or contains nothing.
     * @param _vpkDirectory The internal VPK directory to search.
     * @return The list of found VPKEntries, or null if the VPKArchive fails to load, or if nothing is found.
     */
    public List<VPKEntry> getVPKEntriesInDirectory(String _vpkDirectory)
    {
        loadVPK();
        if(!_vpkLoaded)
        {
            return null;
        }
        
        return _vpk.getEntriesForDir(_vpkDirectory);
    }

}
