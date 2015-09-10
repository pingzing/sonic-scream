package sonicScream.utilities;

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
//package sonicScream.utilities;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//import javax.swing.tree.DefaultMutableTreeNode;
//import javax.swing.tree.DefaultTreeModel;
//import javax.swing.tree.TreeModel;
//
///**
// *
// * @author
// * Neil
// * McAlister
// */
//public class Script
//{
//    private TreeModel scriptTree = null;
//    private int currentLevel = 0;
//    private int previousLevel = 0;
//    private DefaultMutableTreeNode currentNode = null;
//    private DefaultMutableTreeNode currentParent = null;
//    private File modelFile = null;
//    private StringBuilder outputScriptString = null;
//
//    public ScriptParser(TreeModel currentHeroTreeModel)
//    {
//        StringBuilder scriptString = parseModel(currentHeroTreeModel);
//        outputScriptString = scriptString;
//    }
//
//    public ScriptParser(File script)
//    {
//        //Create the root node
//        scriptTree = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
//        currentNode = (DefaultMutableTreeNode) scriptTree.getRoot();
//        currentParent = currentNode;
//        generateModelFromFile(script);
//    }
//
//    public ScriptParser(String scriptString)
//    {
//        scriptTree = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
//        currentNode = (DefaultMutableTreeNode) scriptTree.getRoot();
//        currentParent = currentNode;
//        generateModelFromString(scriptString);
//    }
//    
//    public static TreeModel parseScript(String scriptString)
//    {
//        
//    }
//
//    private void generateModelFromFile(File script)
//    {
//        InputStream fis;
//        BufferedReader br;
//
//        String line = null;
//
//        try
//        {
//            fis = new FileInputStream(script);
//            br = new BufferedReader(new InputStreamReader(fis));
//
//            while ((line = br.readLine()) != null)
//            {
//                parseLine(line);
//            }
//            br.close();
//            fis.close();
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
//        finally
//        {
//            System.out.println("Done generating tree.");
//        }
//    }
//
//    private void generateModelFromString(String script)
//    {
//        BufferedReader buf = new BufferedReader(new StringReader(script));
//        String line = null;
//        try
//        {
//            while ((line = buf.readLine()) != null)
//            {
//                parseLine(line);
//            }
//        }
//        catch (IOException ex)
//        {
//            ex.printStackTrace();
//        }
//        finally
//        {
//            System.out.println("Done generating tree.");
//        }
//    }
//
//    public void writeModelToFile(String scriptFilePath)
//    {
//        if (outputScriptString != null)
//        {
//            File outputFile = new File(scriptFilePath);
//            FileWriter fw = null;
//
//            try
//            {
//                fw = new FileWriter(outputFile);
//                fw.write(outputScriptString.toString());
//                fw.close();
//            }
//            catch (IOException ex)
//            {
//                ex.printStackTrace();
//            }
//            finally
//            {
//                try
//                {
//                    fw.close();
//                }
//                catch (IOException ex)
//                {
//                    ex.printStackTrace();
//                }
//            }
//
//        }
//    }
//
//    private void parseLine(String line)
//    {                
//        if (line.isEmpty() || line.equals("") || line.contentEquals("\t") || line.trim().length() <= 0)
//        {
//            return;
//        }
//        //Need this if we want braces commented out too
//        if (line.trim().startsWith("//"))
//        {
//            try
//            {
//                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(line);
//                currentNode = newNode;
//                if (currentNode != null)
//                {
//                    currentParent.add(newNode);
//                }
//            }
//            catch (NullPointerException ex)
//            {
//                ex.printStackTrace();
//            }
//        }        
//        else if (line.contains("{"))
//        {
//            currentLevel++;
//            currentParent = currentNode;
//            return;
//        }
//        else if (line.contains("}"))
//        {
//            currentLevel--;
//            currentParent = (DefaultMutableTreeNode) currentParent.getParent();
//            if (currentParent == null)
//            {
//                currentParent = (DefaultMutableTreeNode) scriptTree.getRoot();
//            }
//            return;
//        }
//        else
//        {
//            try
//            {
//                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(line);
//                currentNode = newNode;
//                if (currentNode != null)
//                {
//                    currentParent.add(newNode);
//                }
//            }
//            catch (NullPointerException ex)
//            {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private StringBuilder parseModel(TreeModel currentTreeModel)
//    {
//        DefaultMutableTreeNode root = (DefaultMutableTreeNode) currentTreeModel.getRoot();
//        StringBuilder scriptString = new StringBuilder();
//        recursiveBuildScript(scriptString, root, 0);
//
//        //Remove first, final braces, starting newline. We don't need that first layer of nesting the braces would cause
//        scriptString.deleteCharAt(0);
//        scriptString.deleteCharAt(0);
//
//        //Remove final bracket
//        scriptString.deleteCharAt(scriptString.lastIndexOf("}"));
//        return scriptString;
//    }
//
//    private void recursiveBuildScript(StringBuilder scriptString, DefaultMutableTreeNode node, int level)
//    {
//        if (!node.getUserObject().toString().contentEquals("root"))
//        {
//            scriptString.append(node.getUserObject().toString() + "\n");
//        }
//        /* 
//         * TODO: Figure out a way to track brace placement without just checking too see if a node has children. Maybe 
//         * a custom node object that tracks whether a node is followed by braces?
//         */
//        if (!node.isLeaf())
//        {
//            for (int i = 1; i < level; i++)
//            {
//                scriptString.append("\t");
//            }
//            scriptString.append("{\n");
//            level++;
//            for (int i = 0; i < node.getChildCount(); i++)
//            {
//                recursiveBuildScript(scriptString, (DefaultMutableTreeNode) node.getChildAt(i), level);
//            }
//            level--;
//            for (int i = 1; i < level; i++)
//            {
//                scriptString.append("\t");
//            }
//            scriptString.append("}\n");
//        }
//    }
//}
