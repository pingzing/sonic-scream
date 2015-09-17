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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class Profile
{
    private StringProperty profileName = new SimpleStringProperty();
    public final String getProfileName() { return profileName.get(); }
    public final void setProfileName(String value) { profileName.set(value); }
    public StringProperty profileNameProperty() { return profileName; }
    
    private StringProperty profileDescription = new SimpleStringProperty();
    public final String getProfileDescription() {return profileDescription.get(); }
    public final void setProfileDescription(String value) { profileDescription.set(value); }
    public StringProperty profileDescriptionProperty() { return profileName; }
    
    public List<Category> _categories;
    public List<Category> getCategories() { return _categories; }
    public void setCategories(List<Category> value) {_categories = value; }
    
    public Profile()
    {
        _categories = new ArrayList<>();
        profileName.set("Default");
        profileDescription.set("The default profile");
    }    
    
    public Profile(String name)
    {
        //TODO: change this to the default set
        _categories = new ArrayList<>(); 
        profileName.set(name);
        profileDescription.set("");
    }
    
    @Override
    public String toString()
    {
        return profileName.get();
    }
    
    @Override
    public boolean equals(Object p)
    {
        if(!(p instanceof Profile))
        {
            return false;
        }
        if(p == this)
        {
            return true;
        }        
        
        Profile other = (Profile)p;
        boolean equalsBuilder =  new EqualsBuilder()
                .append(profileName.get(), other.profileName.get())                
                .append(profileDescription.get(), other.profileDescription.get())                
                .isEquals();
        return equalsBuilder
                && Arrays.deepEquals(_categories.toArray(), other._categories.toArray());
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.profileName);
        hash = 29 * hash + Objects.hashCode(this.profileDescription);
        hash = 29 * hash + Objects.hashCode(this._categories);
        return hash;
    }

}
