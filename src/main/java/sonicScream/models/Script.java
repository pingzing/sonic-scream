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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang3.builder.EqualsBuilder;
import sonicScream.services.ServiceLocator;
import sonicScream.utilities.ScriptParser;
import sonicScream.utilities.StringParsing;
import sonicScream.services.VPKFileService;

public class Script
{
    private final String _parentCategoryName; //TODO: Don't deserialize this, maybe? Maybe turn it into a string, or UUID?
    public String getParentCategoryName() { return _parentCategoryName; }    
    
    private final String _internalScriptName; //no file extension, or "game_sounds_etc" prefix  
    public String getInternalScriptName() { return _internalScriptName; }
    
    private final String _rawFileName; //full file name
    public string
    
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
        _rawFileName = scriptFile.getName() + "." +  scriptFile.getType();
        _internalScriptName = StringParsing.getScriptNameFromFileName(_rawFileName);
        friendlyScriptName.set(StringParsing.prettyFormatScriptName(_internalScriptName));
        _parentCategoryName = category.getCategoryName();
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
        _parentCategoryName = category.getCategoryName();       
        _localPath = scriptFile.toAbsolutePath().toString();        
        _isCustom = true;
        _vpkPath = null;
    }       
    
    public Script(Path)
    
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
                    Path script = Paths.get(_localPath);
                    _rootNode = ScriptParser.parseScript(script, _rawFileName);
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

    /**
     * Returns the current script's Tree as a single string, formatted for output.
     * If the current script has not yet been transformed into a Tree, this will do so.
     * @return A string containing the entire script, formatted for human-readability.
     */
    public String getScriptAsString()
    {
        if (_treeAsString == null)
        {
            getRootNode();
            _treeAsString = ScriptParser.parseScriptTreeToString(_rootNode);
        }
        return _treeAsString;
    }        

    @Override
    public String toString()
    {
        return friendlyScriptName.get();
    }
    
    @Override
    public boolean equals(Object scr)
    {
        if(!(scr instanceof Script))
        {
            return false;
        }
        if(scr == this)
        {
            return true;
        }
        
        Script other = (Script)scr;
        return new EqualsBuilder()
                .append(this._internalScriptName, other._internalScriptName)
                .append(this._isCustom, other._isCustom)
                .append(this._lastKnownCrc, other._lastKnownCrc)
                .append(this._localPath, other._localPath)
                .append(this._rawFileName, other._rawFileName)
                .append(this._vpkPath, other._vpkPath)
                .isEquals();
    }
    
}
