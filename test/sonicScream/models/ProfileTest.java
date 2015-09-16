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

import java.util.List;
import javafx.beans.property.StringProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nmca
 */
public class ProfileTest
{
    
    public ProfileTest()
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
        System.out.println("getProfileName");
        Profile instance = new Profile();
        String expResult = "";
        String result = instance.getProfileName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setProfileName method, of class Profile.
     */
    @Test
    public void testSetProfileName()
    {
        System.out.println("setProfileName");
        String value = "";
        Profile instance = new Profile();
        instance.setProfileName(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of profileNameProperty method, of class Profile.
     */
    @Test
    public void testProfileNameProperty()
    {
        System.out.println("profileNameProperty");
        Profile instance = new Profile();
        StringProperty expResult = null;
        StringProperty result = instance.profileNameProperty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProfileDescription method, of class Profile.
     */
    @Test
    public void testGetProfileDescription()
    {
        System.out.println("getProfileDescription");
        Profile instance = new Profile();
        String expResult = "";
        String result = instance.getProfileDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setProfileDescription method, of class Profile.
     */
    @Test
    public void testSetProfileDescription()
    {
        System.out.println("setProfileDescription");
        String value = "";
        Profile instance = new Profile();
        instance.setProfileDescription(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of profileDescriptionProperty method, of class Profile.
     */
    @Test
    public void testProfileDescriptionProperty()
    {
        System.out.println("profileDescriptionProperty");
        Profile instance = new Profile();
        StringProperty expResult = null;
        StringProperty result = instance.profileDescriptionProperty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCategories method, of class Profile.
     */
    @Test
    public void testGetCategories()
    {
        System.out.println("getCategories");
        Profile instance = new Profile();
        List<Category> expResult = null;
        List<Category> result = instance.getCategories();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCategories method, of class Profile.
     */
    @Test
    public void testSetCategories()
    {
        System.out.println("setCategories");
        List<Category> value = null;
        Profile instance = new Profile();
        instance.setCategories(value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Profile.
     */
    @Test
    public void testToString()
    {
        System.out.println("toString");
        Profile instance = new Profile();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
