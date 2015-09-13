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
import sonicScream.utilities.ScriptParser;
import sonicScream.utilities.StringParsing;

public class Script
{

    private final String _internalScriptName;
    private final String _friendlyScriptName;    
    private final String _rawScriptName;    
    private TreeItem<String> _rootNode;
    private String _treeAsString = null;
    
    private final Boolean _isVPK ;
    private final VPKEntry _scriptVPK;
    
    private File _scriptFile;

    public Script(VPKEntry scriptFile)
    {
        _rawScriptName = scriptFile.getName() + scriptFile.getType();
        _internalScriptName = StringParsing.GetScriptNameFromFileName(_rawScriptName);
        _friendlyScriptName = StringParsing.PrettyFormatScriptName(_internalScriptName);
        _isVPK = true;
        _scriptVPK = scriptFile;
    }
    
    public Script(File scriptFile)
    {
        _rawScriptName = scriptFile.getName();
        _internalScriptName = StringParsing.GetScriptNameFromFileName(_rawScriptName);
        _friendlyScriptName = StringParsing.PrettyFormatScriptName(_internalScriptName);        
        _scriptFile = scriptFile;
        _isVPK = false;
        _scriptVPK = null;
    }
    
    private BufferedReader getScriptReader(File scriptFile)
    {
        try(FileInputStream fis = new FileInputStream(scriptFile))
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            return br;
        }
        catch(FileNotFoundException ex)
        {
            System.err.println("Unable to read script file " + scriptFile.getName() + ": " + ex.getMessage());
            return null;
        }
        catch(IOException ex)
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
                    _rootNode = ScriptParser.parseScript(getScriptReader(_scriptVPK), _scriptVPK.getName() + _scriptVPK.getType());
                }
                else
                {
                    _rootNode = ScriptParser.parseScript(getScriptReader(_scriptFile), _rawScriptName);
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

    @Override
    public String toString()
    {
        return _friendlyScriptName;
    }
}
