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
package sonicScream.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Neil McAlister
 */
public class ScriptParser
{

    private static List<TreeItem<String>> _scriptTree = null;
    private static TreeItem<String> _currentNode = null;
    private static TreeItem<String> _currentParent = null;

    public static List<TreeItem<String>> parseScript(File scriptFile) throws IOException
    {
        _scriptTree = new ArrayList<TreeItem<String>>();
        _currentNode = new TreeItem<>();
        _scriptTree.add(_currentNode);
        _currentParent = _currentNode;

        try (
                FileInputStream fis = new FileInputStream(scriptFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));)
        {
            parseHeaderAndFirstTwoLines(br, StringParsing.GetScriptNameFromFileName(scriptFile.getName()));

            Boolean bracketAdded = false; 
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.contains("operator_stacks"))
                {
                    parseNameAndOperatorStacks(line);
                    bracketAdded = false;
                }
                else
                {
                    parseLine(line);
                }
                
                if(line.contains("}") && !bracketAdded)
                {
                    parseLine("}");
                    bracketAdded = true;
                }
            }
            return _scriptTree;
        }
    }

    //This strips out all the header junk and correctly separates and parses the first two lines of the actual script.
    private static void parseHeaderAndFirstTwoLines(BufferedReader br, String scriptSubject) throws IOException
    {
        int seenCount = 0;
        //Seek to the end of the header                                                  
        while (seenCount < 2)
        {
            String line = br.readLine();
            int dataStartIndex = line.indexOf(scriptSubject);
            if (dataStartIndex != -1)
            {
                seenCount++;
            }

            //Sometimes the entire header is a single line. Check for a second occurrence immediately.
            if (seenCount < 2 && line.lastIndexOf(scriptSubject) != dataStartIndex)
            {
                dataStartIndex = line.lastIndexOf(scriptSubject);
                seenCount++;
            }

            if (seenCount == 2)
            {
                line = line.substring(dataStartIndex, line.length() - 1);
                int separatorIndex = line.indexOf("\"");
                //String firstLine = "\"" + line.substring(0, separatorIndex-1) + "\"";
                //String secondLine = line.substring(separatorIndex, line.length()) + "\"";                
                //now we have the sound name and "operator stacks" mashed together 
                //like so: heroname"operator stacks" we need to add a quote to 
                //the beginning, then separate these two.                
                parseNameAndOperatorStacks(line);
            }
        }
    }

    private static void parseNameAndOperatorStacks(String line)
    {
        int separatorIndex = line.indexOf("\"");
        String name = "\"" + line.substring(0, separatorIndex);
        String operator = "\"" + line.substring(separatorIndex, line.length());
        parseLine(name);
        parseLine("{");
        parseLine(operator);
    }

    private static void parseLine(String line) throws NullPointerException
    {
        if (line.isEmpty() || line.equals("") || line.contentEquals("\t")
                || line.trim().length() <= 0)
        {
            return;
        }
        //Need this if we want braces commented out too
        if (line.trim().startsWith("//"))
        {
            try
            {
                TreeItem<String> newNode = new TreeItem<String>(line);
                _currentNode = newNode;
                if (_currentNode != null)
                {
                    _currentParent.getChildren().add(newNode);
                }
            }
            catch (NullPointerException ex)
            {
                ex.printStackTrace();
            }
        }
        else if (line.contains("{"))
        {
            _currentParent = _currentNode;
        }
        else if (line.contains("}"))
        {
            _currentParent = _currentParent.getParent();
            if (_currentParent == null)
            {
                _currentParent = _scriptTree.get(0);
                if (_currentParent.getParent() != null)
                {
                    throw new AssertionError("_scriptTree[0] is NOT the root element!");
                }
            }
        }
        else
        {
            try
            {
                //line = CleanNodeRoot(line);
                TreeItem<String> newNode = new TreeItem<String>(line);
                _currentNode = newNode;
                if (_currentNode != null)
                {
                    _currentParent.getChildren().add(newNode);
                }
            }
            catch (NullPointerException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    //Cleans up some of the garbage at the top of each node
//    private static String CleanNodeRoot(String line)
//    {
//        //if(line.startsWith(line))
//    }
    private StringBuilder parseModel(List<TreeItem<String>> currentTreeModel)
    {
        TreeItem<String> root = currentTreeModel.get(0);
        StringBuilder scriptString = new StringBuilder();
        recursiveBuildScript(scriptString, root, 0);

        //Remove first, final braces, starting newline. We don't need that first layer of nesting the braces would cause
        scriptString.deleteCharAt(0);
        scriptString.deleteCharAt(0);

        //Remove final bracket
        scriptString.deleteCharAt(scriptString.lastIndexOf("}"));
        return scriptString;
    }

    private StringBuilder recursiveBuildScript(StringBuilder scriptString, TreeItem<String> node, int level)
    {
        if (node.getParent() != null)
        {
            scriptString.append(node.getValue() + "\n");
        }
        /* 
         * TODO: Figure out a way to track brace placement without just checking to see if a node has children. Maybe 
         * a custom node object that tracks whether a node is followed by braces?
         */
        if (!node.isLeaf())
        {
            for (int i = 1; i < level; i++)
            {
                scriptString.append("\t");
            }
            scriptString.append("{\n");
            level++;
            for (int i = 0; i < node.getChildren().size(); i++)
            {
                scriptString = recursiveBuildScript(scriptString, node.getChildren().get(i), level);
            }
            level--;
            for (int i = 1; i < level; i++)
            {
                scriptString.append("\t");
            }
            scriptString.append("}\n");
        }
        return scriptString;
    }
}
