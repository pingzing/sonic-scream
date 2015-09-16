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
import java.nio.file.Path;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import sonicScream.services.ServiceLocator;
import sonicScream.utilities.ScriptParser;
import sonicScream.utilities.StringParsing;
import sonicScream.services.VPKFileService;

public class Script
{        
    private final Category _parentCategory;
    private final String _internalScriptName; //no file extension, or "game_sounds_etc" prefix      
    private final String _rawFileName; //full file name
    private TreeItem<String> _rootNode;
    private String _treeAsString = null;    
    private final Boolean _isCustom ;
    
    private final String _vpkPath;
    private long _lastKnownCrc;
    
    private String _localPath;
    
    private StringProperty friendlyScriptName = new SimpleStringProperty();
    public final String getFriendlyScriptName() { return friendlyScriptName.get(); }
    public final void setFriendlyScriptName(String value) { friendlyScriptName.set(value); }
    public StringProperty friendlyScriptNameProperty() { return friendlyScriptName; }

    public Script(VPKEntry scriptFile, Category category)
    {
        _rawFileName = scriptFile.getName() + scriptFile.getType();
        _internalScriptName = StringParsing.getScriptNameFromFileName(_rawFileName);
        friendlyScriptName.set(StringParsing.prettyFormatScriptName(_internalScriptName));
        _parentCategory = category;
        _isCustom = false;
        _vpkPath = scriptFile.getPath();
    }
    
    /**
     * For internal use only. We never want to load a user's Script files directly from a vsndevts file.
     * We always want to use a serialized Script file instead, so we retain all our metadata.
     * @param scriptFile
     * @param category 
     */
    public Script(Path scriptFile, Category category)
    {
        _rawFileName = scriptFile.getFileName().toString();
        _internalScriptName = StringParsing.getScriptNameFromFileName(_rawFileName);
        friendlyScriptName.set(StringParsing.prettyFormatScriptName(_internalScriptName));
        _parentCategory = category;       
        _localPath = scriptFile.toAbsolutePath().toString();        
        _isCustom = true;
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
    
    public String getRawFileName() { return _rawFileName; }
    
    public TreeItem<String> getRootNode()
    {
        if (_rootNode == null)
        {
            try
            {
                if (_isCustom)
                {
                    File script = new File(_localPath);
                    _rootNode = ScriptParser.parseScript(getScriptReader(script), _rawFileName);
                }
                else
                {
                    VPKFileService fileService = (VPKFileService) ServiceLocator.getService(VPKFileService.class);
                    VPKEntry script = fileService.getVPKEntry(_vpkPath);
                    _rootNode = ScriptParser.parseScript(getScriptReader(script), _rawFileName);
                }
            }
            catch (IOException ex)
            {
                System.err.println("Unable to read file: " + ex.getMessage());
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
        return friendlyScriptName.get();
    }
}
