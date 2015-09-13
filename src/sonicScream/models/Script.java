///* 
// * The MIT License
// *
// * Copyright 2015 Neil McAlister.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
package sonicScream.models;

import info.ata4.vpk.VPKEntry;
import java.io.*;
import javafx.scene.control.TreeItem;
import sonicScream.services.ServiceLocator;
import sonicScream.utilities.ScriptParser;
import sonicScream.utilities.StringParsing;
import sonicScream.services.VPKFileService;

public class Script
{
    private final Category _parentCategory;
    private final String _internalScriptName;
    private final String _friendlyScriptName;    
    private final String _rawScriptName;    
    private TreeItem<String> _rootNode;
    private String _treeAsString = null;
    
    private final Boolean _isVPK ;
    private final String _vpkPath;
    private long _lastKnownCrc;
    
    private String _localPath;

    public Script(VPKEntry scriptFile, Category category)
    {
        _rawScriptName = scriptFile.getName() + scriptFile.getType();
        _internalScriptName = StringParsing.getScriptNameFromFileName(_rawScriptName);
        _friendlyScriptName = StringParsing.prettyFormatScriptName(_internalScriptName);
        _parentCategory = category;
        _isVPK = true;
        _vpkPath = scriptFile.getPath();
    }
    
    public Script(File scriptFile, Category category)
    {
        _rawScriptName = scriptFile.getName();
        _internalScriptName = StringParsing.getScriptNameFromFileName(_rawScriptName);
        _friendlyScriptName = StringParsing.prettyFormatScriptName(_internalScriptName);
        _parentCategory = category;
        try
        {
            _localPath = scriptFile.getCanonicalPath();
        }
        catch(IOException ex)
        {
            System.err.printf("\nFailed to get canonical path for %s: %s", _rawScriptName, ex.getMessage());
        }
        _isVPK = false;
        _vpkPath = null;
    }
    
    private BufferedReader getScriptReader(File scriptFile)
    {
        try
        {
            FileInputStream fis = new FileInputStream(scriptFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            return br;
        }
        catch(FileNotFoundException ex)
        {
            System.err.println("Unable to read script file " + scriptFile.getName() + ": " + ex.getMessage());
            return null;
        }        
    }
    
    private BufferedReader getScriptReader(VPKEntry entry)
    {
        byte[] buf = new byte[entry.getDataSize()];
        try
        {
            entry.getData().get(buf);
        }
        catch(IOException ex)
        {
            System.err.println("Unable to read VPKEntry " + entry.getName());
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(buf);
        BufferedReader br = new BufferedReader(new InputStreamReader(bis));
        return br;
    }

    public String getScriptName() { return _internalScriptName; }
    
    public String getRawScriptName() { return _rawScriptName; }
    
    public TreeItem<String> getRoodNode()
    {
        if (_rootNode == null)
        {
            try
            {
                if(_isVPK)
                {                
                    VPKFileService fileService = (VPKFileService)ServiceLocator.getService(VPKFileService.class);
                    VPKEntry script = fileService.getVPKEntry(_vpkPath);
                    _rootNode = ScriptParser.parseScript(getScriptReader(script), _rawScriptName);
                }
                else
                {
                    File script = new File(_localPath);
                    _rootNode = ScriptParser.parseScript(getScriptReader(script), _rawScriptName);
                }
            }
            catch(IOException ex)
            {
                
            }
        }
        return _rootNode;
    }
    public void setRootNode(TreeItem<String> value) { _rootNode = value; }

    public String getScriptAsString()
    {
        if (_treeAsString == null)
        {
            _treeAsString = ScriptParser.parseScriptTreeToString(_rootNode);
        }
        return _treeAsString;
    }
    
    public Category getParentCategory() { return _parentCategory; }    

    @Override
    public String toString()
    {
        return _friendlyScriptName;
    }
}
