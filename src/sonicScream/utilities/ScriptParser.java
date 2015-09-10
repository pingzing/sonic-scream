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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author
 * Neil
 * McAlister
 */
public class ScriptParser
{
    private static TreeModel _scriptTree = null;    
    private static DefaultMutableTreeNode _currentNode = null;
    private static DefaultMutableTreeNode _currentParent = null;            
    
    public static TreeModel parseScript(File scriptFile) throws IOException
    {
        _scriptTree = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
        _currentNode = (DefaultMutableTreeNode) _scriptTree.getRoot();
        _currentParent = _currentNode;               
        
        try(
            FileInputStream fis = new FileInputStream(scriptFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            )   
        {            
            int seenCount = 0;
            //Seek to the end of the header
            String line;                   
            String scriptSubject = StringParsing.GetScriptNameFromFileName(scriptFile.getName());
            
            while(seenCount < 2)
            {
                line = br.readLine();
                int dataStartIndex = line.indexOf(scriptSubject);
                if(dataStartIndex != -1)
                {
                    seenCount++;
                }
                if(seenCount == 2)
                {                    
                    line = line.substring(dataStartIndex, line.length() - 1);
                    int separatorIndex = line.indexOf("\"");
                    //now we have the sound name and "operator stacks" mashed together like so: heroname"operator stacks"
                    //we need to add a quote to the beginning, then separate these two.
                    parseLine(line);
                }
            }            
            
            while((line = br.readLine()) != null)
            {
                System.out.println(line);
                parseLine(line);            
            }
            return _scriptTree;                     
        }
    }    

    private static void parseLine(String line) throws NullPointerException
    {                
        if (line.isEmpty() || line.equals("") || line.contentEquals("\t") || line.trim().length() <= 0)
        {
            return;
        }
        //Need this if we want braces commented out too
        if (line.trim().startsWith("//"))
        {
            try
            {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(line);
                _currentNode = newNode;
                if (_currentNode != null)
                {
                    _currentParent.add(newNode);
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
            _currentParent = (DefaultMutableTreeNode) _currentParent.getParent();
            if (_currentParent == null)
            {
                _currentParent = (DefaultMutableTreeNode) _scriptTree.getRoot();
            }            
        }
        else
        {
            try
            {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(line);
                _currentNode = newNode;
                if (_currentNode != null)
                {
                    _currentParent.add(newNode);
                }
            }
            catch (NullPointerException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private StringBuilder parseModel(TreeModel currentTreeModel)
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) currentTreeModel.getRoot();
        StringBuilder scriptString = new StringBuilder();
        recursiveBuildScript(scriptString, root, 0);

        //Remove first, final braces, starting newline. We don't need that first layer of nesting the braces would cause
        scriptString.deleteCharAt(0);
        scriptString.deleteCharAt(0);

        //Remove final bracket
        scriptString.deleteCharAt(scriptString.lastIndexOf("}"));
        return scriptString;
    }

    private void recursiveBuildScript(StringBuilder scriptString, DefaultMutableTreeNode node, int level)
    {
        if (!node.getUserObject().toString().contentEquals("root"))
        {
            scriptString.append(node.getUserObject().toString() + "\n");
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
            for (int i = 0; i < node.getChildCount(); i++)
            {
                recursiveBuildScript(scriptString, (DefaultMutableTreeNode) node.getChildAt(i), level);
            }
            level--;
            for (int i = 1; i < level; i++)
            {
                scriptString.append("\t");
            }
            scriptString.append("}\n");
        }
    }
}
