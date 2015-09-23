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
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;

import javax.xml.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import sonicScream.services.ServiceLocator;
import sonicScream.utilities.ScriptParser;
import sonicScream.utilities.StringParsing;
import sonicScream.services.VPKFileService;
import sonicScream.utilities.TreeParser;
import static sonicScream.utilities.TreeParser.getWaveStrings;

@XmlRootElement(name="Script")
@XmlAccessorType(XmlAccessType.FIELD)
public class Script implements Comparable
{    
    @XmlElement
    private String _parentCategoryName; //TODO: Don't deserialize this, maybe? Maybe turn it into a string, or UUID?        
    @XmlElement
    private String _internalScriptName; //no file extension, or "game_sounds_etc" prefix      
    @XmlElement
    private String _rawFileName; //full file name    

    @XmlTransient
    private TreeItem<String> _rootNode;
    @XmlTransient
    private TreeItem<String> _simpleRootNode;
    @XmlTransient
    private String _treeAsString = null;    

    @XmlElement
    private Boolean _isCustom ;
    @XmlElement
    private String _vpkPath;
    @XmlElement
    private long _lastKnownCrc;
    
    @XmlElement
    private String _localPath;
    
    @XmlTransient
    private StringProperty friendlyScriptName = new SimpleStringProperty();
    @XmlElement
    public final String getFriendlyScriptName() { return friendlyScriptName.get(); }
    public final void setFriendlyScriptName(String value) { friendlyScriptName.set(value); }  
    public StringProperty friendlyScriptNameProperty() { return friendlyScriptName; }

    /**
     * Only for compatibility with JAXB
     */
    public Script()
    {
        
    }
    
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
    
    @XmlTransient 
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
    @XmlTransient
    public String getScriptAsString()
    {
        if (_treeAsString == null)
        {
            getRootNode();
            _treeAsString = ScriptParser.parseScriptTreeToString(_rootNode);
        }
        return _treeAsString;
    }        
    
        /**
     * Returns the Script's tree with everything removed but each entry's title
     * and its wave file list, and flattens the hierarchy. Does not modify the 
     * input tree. Initializes the rootNode if it has not yet been initialized.
     * @param root The tree to simplify.
     * @return A new copy of the now-simplified tree.
     */
    public TreeItem<String> getSimpleTree()
    {
        if(_simpleRootNode != null)
        {
            return _simpleRootNode;
        }
        if(_rootNode == null)
        {
            _rootNode = getRootNode();
        }
        TreeItem<String> local = new TreeItem<String>("root");
        for(TreeItem<String> child : getRootNode().getChildren())
        {
            List<TreeItem<String>> localWaveStrings = FXCollections.observableArrayList();
            List<TreeItem<String>> waveStrings = getWaveStrings(child).orElse(null);
            if(waveStrings == null) continue;
            for(TreeItem<String> wave : waveStrings)
            {                
                TreeItem<String> sound = new TreeItem<String>();
                //Remove whitespace, quotes, and value# text.
                String waveValue = wave.getValue().trim();
                int thirdQuoteIndex = StringUtils.ordinalIndexOf(waveValue, "\"", 3);
                waveValue = (waveValue.substring(thirdQuoteIndex + 1, waveValue.length() - 1));  
                sound.setValue(waveValue);
                localWaveStrings.add(sound);
            }
            TreeItem<String> localChild = new TreeItem<>(child.getValue());
            localChild.getChildren().setAll(localWaveStrings);    
            local.getChildren().add(localChild);
        }
        _simpleRootNode = local;
        return _simpleRootNode;
    }
            
    /**
     * Takes the currently active simple tree, and uses its values to update the main
     * rootNode tree, then returns the newly-updated tree.
     * @return 
     */
    public TreeItem<String> updateRootNodeWithSimpleTree()
    {
        if(_simpleRootNode == null)
        {
            return getRootNode();
        }
        for(TreeItem<String> entry : _simpleRootNode.getChildren())
        {            
            List<TreeItem<String>> sounds = entry.getChildren();
            for(int i = 0; i < sounds.size(); i++)
            {
                List<TreeItem<String>> rootSounds = TreeParser.getWaveStrings(_rootNode).orElseThrow(null);                
                if(rootSounds != null && rootSounds.size() < i)
                {
                    if(rootSounds.get(i).getValue().contains(sounds.get(i).getValue()))
                    {
                        continue;
                    }
                    String value = "\"value" + i + "\" \"" + sounds.get(i).getValue() + "\"";
                    rootSounds.get(i).setValue(value);
                }
            }
            
        }
        return _rootNode;
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

    @Override
    public int compareTo(Object o)
    {
        final int LESS_THAN = -1;
        final int EQUAL = 0;
        final int MORE_THAN = 1;
        if(o == this)
        {
            return EQUAL;
        }

        Script other = (Script)o;
        return this.getFriendlyScriptName().compareTo(other.getFriendlyScriptName());
    }
}
