/*
 * The MIT License
 *
 * Copyright 2015 nmca.
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
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author nmca
 */
public class ProfileTest
{
 
    private static Profile _testProfile;
    
    public ProfileTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        Script mockScript = mock(Script.class);
        when(mockScript.friendlyScriptNameProperty()).thenReturn(new SimpleStringProperty("Axe"));
        ArrayList<Script> scripts = new ArrayList<>();
        scripts.add(mockScript);
        scripts.add(mockScript);
        
        Category mockCategory = mock(Category.class);
        when(mockCategory.categoryNameProperty()).thenReturn(new SimpleStringProperty("Test"));
        when(mockCategory.categoryScriptsProperty())
                .thenReturn(new SimpleListProperty<Script>(FXCollections.observableArrayList(scripts)));
        
        _testProfile = new Profile("Test Profile");
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(mockCategory);
        _testProfile.setCategories(null);
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getProfileName method, of class Profile.
     */
    @Test
    public void testGetProfileName()
    {
        String profileName = _testProfile.getProfileName();
        assertEquals(profileName, "Test Profile");
    } 
}
