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
    private String _mainVPKPath = "";
    private String _mainVPKParentDirectory = "";
    
    public VPKFileService(String mainVPKPath, String mainVPKParentDir)
    {
        _mainVPKPath = mainVPKPath;
        _mainVPKParentDirectory = mainVPKParentDir;
    }

    public VPKEntry getVPKEntry(String _vpkPath) throws VPKException, IOException
    {
        VPKArchive archive = new VPKArchive();
        archive.load(new File(_mainVPKPath));
        return archive.getEntry(_vpkPath);        
    }
    
    public List<VPKEntry> getVPKEntries(List<String> _vpkPaths) throws VPKException, IOException
    {
        VPKArchive archive = new VPKArchive();
        archive.load(new File(_mainVPKPath));
        ArrayList<VPKEntry> entryList = new ArrayList<>();
        for(String path : _vpkPaths)
        {
          entryList.add(archive.getEntry(path));
        }
        return entryList;
    }
    
    public List<VPKEntry> getVPKEntriesInDirectory(String _vpkDirectory) throws VPKException, IOException
    {
        VPKArchive archive = new VPKArchive();
        archive.load(new File(_mainVPKPath));
        return archive.getEntriesForDir(_vpkDirectory);
    }

}
