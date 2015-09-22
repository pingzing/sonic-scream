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
package sonicScream.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import info.ata4.vpk.VPKEntry;
import javafx.beans.property.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import javafx.collections.ObservableList;
import sonicScream.services.ServiceLocator;
import sonicScream.services.VPKFileService;
import sonicScream.utilities.Constants;
import sonicScream.utilities.FilesEx;

@XmlRootElement(name = "Category")
public class Category
{ 
    private StringProperty categoryName = new SimpleStringProperty();    
    public final String getCategoryName() { return categoryName.get(); }
    public final void setCategoryName(String value) { categoryName.set(value); }    
    public StringProperty categoryNameProperty() { return categoryName; }            
    
    private ObservableList<Script> categoryScripts = FXCollections.observableArrayList();
    public ObservableList<Script> getCategoryScripts() { return categoryScripts; }
    public void setCategoryScripts(ObservableList<Script> value) { categoryScripts = value; }

    @XmlElementWrapper(name = "VPKPaths")
    @XmlElement(name = "VPKPath")
    private List<String> _vpkPaths;
    public List<String> getVPKPaths() { return _vpkPaths; }
    public void setVPPKPaths(List<String> value) { _vpkPaths = value; }

    /**
     * Only for compatibility with JAXB
     */
    public Category()
    {        
    }
    
    public Category(String name)
    {
        this(name, new ArrayList<>());
    }

    public Category(String name, List<String> vpkPaths)
    {
        this(name, null, vpkPaths);
    }

    public Category(String name, VPKFileService vpkService)
    {
        this(name, vpkService, new ArrayList<>());
    }

    public Category(String name, VPKFileService vpkService, List<String> vpkPaths)
    {        
        setCategoryName(name);
        _vpkPaths = vpkPaths;
        populateScripts(vpkService);
    }

    public void populateScripts(VPKFileService vpkService)
    {
        categoryScripts.clear(); //So we don't double up on entries.

        if(vpkService == null)
        {
            vpkService = (VPKFileService)ServiceLocator.getService(VPKFileService.class);
        }

        ArrayList<Script> scriptsToInitWith = new ArrayList<>();
        Path addonFolder = Paths.get(Constants.SETTING_ADDON_PATH);
        ArrayList<String> localScripts = new ArrayList<String>();
        for(String p : _vpkPaths)
        {
            Path scriptPath = Paths.get(addonFolder.toString(), p);
            if( Files.exists(scriptPath) && Files.isDirectory(scriptPath))
            {
                try
                {
                    for (Path fp : FilesEx.listFiles(scriptPath))
                    {
                        localScripts.add(p);
                        Script local = new Script(fp, this);
                        scriptsToInitWith.add(local);
                    }
                }
                catch(IOException ex)
                {
                    System.err.printf("\nUnable to open %s: %s", scriptPath.toString(), ex.getMessage());
                }
            }
            else if(Files.exists(scriptPath)) //individual script path
            {
                localScripts.add(p);
                Script local = new Script(scriptPath, this);
                scriptsToInitWith.add(local);
            }
        }

        final VPKFileService finalVpkService = vpkService;
        _vpkPaths.stream()
                .filter(s -> localScripts.contains(s) == false)
                .forEach(str ->
                {
                    if(str.contains(".")) //Individual script file
                    {
                        Script vpkScript = new Script(finalVpkService.getVPKEntry(str), this);
                        scriptsToInitWith.add(vpkScript);
                    }
                    else //Scripts folder, non-recursive
                    {
                        List<VPKEntry> vpkEntries = finalVpkService.getVPKEntriesInDirectory(str);
                        for (VPKEntry entry : vpkEntries)
                        {
                            Script vpkScript = new Script(entry, this);
                            scriptsToInitWith.add(vpkScript);
                        }
                    }
                });

        Collections.sort(scriptsToInitWith);
        setCategoryScripts(FXCollections.observableArrayList(scriptsToInitWith));
    }

    @Override public boolean equals(Object cat)
    {
        if(!(cat instanceof Category))
        {
            return false;
        }
        if(cat == this)
        {
            return true;
        }

        Category other = (Category)cat;
        boolean nameEquals = this.categoryName.get().equals(other.categoryName.get());
        boolean vpkPathsEquals = Arrays.deepEquals(this._vpkPaths.toArray(), other._vpkPaths.toArray());
        boolean categoryScriptsEquals = Arrays.deepEquals(this.categoryScripts.toArray(), other.categoryScripts.toArray());;

        return nameEquals && vpkPathsEquals && categoryScriptsEquals;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.categoryName);
        hash = 97 * hash + Objects.hashCode(this.categoryScripts);
        hash = 97 * hash + Objects.hashCode(this._vpkPaths);
        return hash;
    }

}
