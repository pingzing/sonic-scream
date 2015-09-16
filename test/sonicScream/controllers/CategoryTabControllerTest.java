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
package sonicScream.controllers;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import sonicScream.models.Category;
import sonicScream.models.Profile;
import sonicScream.models.Script;
import sonicScream.utilities.Constants;
import testHelpers.JavaFXThreadingRule;

/**
 *
 * @author nmca
 */
public class CategoryTabControllerTest
{
    @Rule public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    Profile profile = new Profile();
    private CategoryTabController controller;    
    
    public CategoryTabControllerTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {        
        File folder = new File("src/sonicScream/assets/test");
        List<Script> scripts = Arrays.asList(folder.listFiles())
                .stream()
                .map(f -> new Script((File) f, new Category(Constants.CATEGORY_HEROES)))
                .collect(Collectors.toList());
        Category category = new Category(Constants.CATEGORY_VOICES);
        category.setCategoryScripts(scripts);
        profile._categories.add(category);
        
        controller = new CategoryTabController(profile.getCategories().get(0));
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of getSelectedScript method, of class CategoryTabController.
     */
    @Test
    public void testGetSelectedScript()
    {
         Script selected = (Script)controller.getSelectedScript();
         assertEquals(selected, profile.getCategories().get(0).getCategoryScripts().get(0));         
    }    
}
