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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class Category
{
    private StringProperty categoryName = new SimpleStringProperty();
    public final String getCategoryName() { return categoryName.get(); } 
    public final void setCategoryName(String value) { categoryName.set(value); }
    public StringProperty categoryNameProperty() { return categoryName; }
    
    private ListProperty<Script> categoryScripts = new SimpleListProperty<>();
    public final List<Script> getCategoryScripts() {return categoryScripts.get(); }
    public final void setCategoryScripts(List<Script> value) {categoryScripts.set(FXCollections.observableArrayList(value)); }
    public ListProperty<Script> categoryScriptsProperty() { return categoryScripts; }
    
    private List<String> _vpkPaths;
    
    public Category(String name)
    {
        setCategoryName(name);
       _vpkPaths = new ArrayList<>();       
    }
    
    public Category(String name, List<String> vpkPaths)
    {
        setCategoryName(name);
        _vpkPaths = vpkPaths;
    }
    
}
