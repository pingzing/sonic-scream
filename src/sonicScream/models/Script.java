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

import java.io.File;
import java.io.IOException;
import javafx.scene.control.TreeItem;
import sonicScream.utilities.ScriptParser;
import sonicScream.utilities.StringParsing;

public class Script
{

    private final String _internalScriptName;
    private final String _friendlyScriptName;
    private final String _rawScriptName;
    private final File _scriptFile;
    private TreeItem<String> _rootNode;
    private String _treeAsString = null;

    public Script(File scriptFile)
    {
        _rawScriptName = scriptFile.getName();
        _internalScriptName = StringParsing.GetScriptNameFromFileName(_rawScriptName);
        _friendlyScriptName = StringParsing.PrettyFormatScriptName(_internalScriptName);
        _scriptFile = scriptFile;
    }

    public String getScriptName()
    {
        return _internalScriptName;
    }

    public String getRawScriptName()
    {
        return _rawScriptName;
    }

    public TreeItem<String> getRoodNode()
    {
        if (_rootNode == null)
        {
            try
            {
                _rootNode = ScriptParser.parseScript(_scriptFile);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return _rootNode;
    }

    public void setRootNode(TreeItem<String> value)
    {
        _rootNode = value;
    }

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
