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
import java.util.List;
import javafx.scene.control.TreeItem;

/**
 *
 * @author Neil McAlister
 */
public class ScriptParser
{

    private static TreeItem<String> _rootItem = null;
    private static TreeItem<String> _currentNode = null;
    private static TreeItem<String> _currentParent = null;
    
    //These two are used to track how many braces we've seen--the files we get
    //out of the VPKs are actually missing a pair of braces. We add the first one in 
    //parseHeaderAndFirstTwoLines, but tracking where the second one belongs requires 
    //tracking the number of brace-pairs after that.
    private static int _openBraces = 0;
    private static int _closeBraces = 0;

    public static TreeItem<String> parseScript(BufferedReader scriptReader, String fileName) throws IOException
    {
        _rootItem = new TreeItem<>("root");
        _currentParent = _rootItem;

        parseHeaderAndFirstTwoLines(scriptReader, StringParsing.getScriptNameFromFileName(fileName));

        String line;
        while ((line = scriptReader.readLine()) != null)
        {
            if (line.contains("operator_stacks"))
            {
                parseNameAndOperatorStacks(line);
            }
            else
            {
                parseLine(line);
            }

            if (line.contains("{"))
            {
                _openBraces++;
            }
            if (line.contains("}"))
            {
                _closeBraces++;
            }

            if (_openBraces == _closeBraces && (_openBraces != 0) && (_closeBraces != 0))
            {
                parseLine("}");
                _openBraces = 0;
                _closeBraces = 0;
            }
        }
        return _rootItem;
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
                //-1 to account for parseNameAndOperatorStacks name substring starting at index 1 to remove the stray whitespace
                line = line.substring(dataStartIndex - 1, line.length() - 1);
                int separatorIndex = line.indexOf("\"");                               
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
        String name = "\"" + line.substring(1, separatorIndex - 1) + "\"";        
        String operator = line.substring(separatorIndex, line.length());
        if(operator.charAt(operator.length() - 1) != ('"'))
        {
            operator = operator + '"';
        }
        parseLine(name);
        parseLine("{");
        parseLine(operator);
    }

    //Basic recursive-descent parser
    private static void parseLine(String line) throws NullPointerException
    {        
        //System.out.println(line);
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
                TreeItem<String> newNode = new TreeItem<>(line);
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
                _currentParent = _rootItem;                
            }
        }
        else
        {
            try
            {                
                TreeItem<String> newNode = new TreeItem<>(line);
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

    public static String parseScriptTreeToString(TreeItem<String> currentScriptTree)
    {
        TreeItem<String> root = currentScriptTree;
        StringBuilder scriptString = new StringBuilder();
        recursiveBuildScript(scriptString, root, 0);

        //Remove first, final braces, starting newline. We don't need that first layer of nesting the braces would cause
        scriptString.deleteCharAt(0);
        scriptString.deleteCharAt(0);

        //Remove final bracket
        scriptString.deleteCharAt(scriptString.lastIndexOf("}"));
        return scriptString.toString();
    }

    private static StringBuilder recursiveBuildScript(StringBuilder scriptString, TreeItem<String> node, int level)
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