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
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.apache.commons.lang3.builder.EqualsBuilder;
import sonicScream.services.ServiceLocator;
import sonicScream.services.VPKFileService;
import sonicScream.utilities.Constants;

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
    
    private ListProperty<Category> categories = new SimpleListProperty();    
    public List<Category> getCategories() { return categories.get(); }
    public void setCategories(List<Category> value) {categories.set(FXCollections.observableArrayList(value)); }
    public ListProperty<Category> categoriesProperty() { return categories; }

    /**
     * Constructs a Profile with the name "Default", the description "The default profile", and with
     * the four default Categories.
     */
    public Profile()
    {        
        this("Default");
    }

    /**
     * Constructs a Profile with the passed name, an empty description, and with the four default Categories.
     * @param name The Profile's name.
     */
    public Profile(String name)
    {        
        this(name, "");
    }

    /**
     * Constructs a Profile with the passed name and description, and with the four default Categories.
     * @param name The Profile's name.
     * @param description The Profile's description as it will appear on the Profile selection screen.
     */
    public Profile(String name, String description)
    {
        profileName.set(name);
        profileDescription.set(description);
        categories.set(FXCollections.observableArrayList(getDefaultCategories()));
    }

    /**
     * Constructs a Profile with the passed name and description, and with the four default Categories.
     * Constructs the default Categories using the passed VPKFileService.
     * This method is primarily for testing. Generally, we want profile to find its own VPKFileService.
     * @param name The Profile's name.
     * @param description The profile's description as it will appear on the Profile selection screen.
     * @param vpkService The VPKFileService to use in construction the default categories.
     */
    public Profile(String name, String description, VPKFileService vpkService)
    {
        profileName.set(name);
        profileDescription.set(description);
        categories.set(FXCollections.observableArrayList(getDefaultCategories(vpkService)));
    }

    /**
     * Constructs a Profile with the passed name and description and Categories.
     * @param name The Profile's name.
     * @param description The Profile's description as it will appear on the Profile selection screen.
     * @param newCats The Profile's Categories, which are represented as tabs on the main screen.
     */
    public Profile(String name, String description, List<Category> newCats)
    {
        profileName.set(name);
        profileDescription.set(description);
        categories.set(FXCollections.observableArrayList(newCats));
    }

    private List<Category> getDefaultCategories()
    {
        return getDefaultCategories(null);
    }
    
    private List<Category> getDefaultCategories(VPKFileService vpkService)
    {
        ArrayList<Category> defaultCategories = new ArrayList<>();
        
        ArrayList<String> vpkPaths = new ArrayList<>();
        vpkPaths.add(Constants.HERO_SPELLS_SCRIPTS_VPK_PATH);        
        Category heroSpells = new Category("Hero Spells", vpkService, vpkPaths);
        defaultCategories.add(heroSpells);
        
        vpkPaths.clear();
        vpkPaths.add(Constants.ITEMS_SCRIPTS_VPK_PATH);
        Category items = new Category("Items", vpkService, vpkPaths);
        defaultCategories.add(items);        
        
        vpkPaths.clear();
        vpkPaths.add(Constants.VOICE_SCRIPTS_VPK_PATH);
        Category voices = new Category("Voices", vpkService, vpkPaths);
        defaultCategories.add(voices);
        
        vpkPaths.clear();
        vpkPaths.add(Constants.MUSIC_DEFAULT_SCRIPTS_VPK_PATH);
        vpkPaths.add(Constants.MUSIC_TI4_SCRIPTS_VPK_PATH);
        vpkPaths.add(Constants.MUSIC_TI5_SCRIPTS_VPK_PATH);
        Category music = new Category("Music", vpkService, vpkPaths);
        defaultCategories.add(music);
        
        return defaultCategories;
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

        boolean listsEqual = categories.equals(other.categories);
        return equalsBuilder && listsEqual;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.profileName);
        hash = 83 * hash + Objects.hashCode(this.profileDescription);
        hash = 83 * hash + Objects.hashCode(this.categories);
        return hash;
    }
}
