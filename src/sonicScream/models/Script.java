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
import java.util.List;
import javafx.scene.control.TreeItem;
import sonicScream.utilities.ScriptParser;
import sonicScream.utilities.StringParsing;

public class Script
{

    private final String _internalScriptName;
    private final String _friendlyScriptName;
    private final String _rawScriptName;
    private List<TreeItem<String>> _scriptNodes;

    public Script(File scriptFile)
    {
        _rawScriptName = scriptFile.getName();
        _internalScriptName = StringParsing.GetScriptNameFromFileName(_rawScriptName);
        _friendlyScriptName = StringParsing.PrettyFormatScriptName(_internalScriptName);
        try
        {
            _scriptNodes = ScriptParser.parseScript(scriptFile);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }

    public String getScriptName() { return _internalScriptName; }

    public String getRawScriptName() { return _rawScriptName; }
    
    public List<TreeItem<String>> getScriptTree() { return _scriptNodes; }
    
    public void setScriptTree(List<TreeItem<String>> value) { _scriptNodes = value; }
    
    @Override
    public String toString()
    {
        return _friendlyScriptName;
    }
}
