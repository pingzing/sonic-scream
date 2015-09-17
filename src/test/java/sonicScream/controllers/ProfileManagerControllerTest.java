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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import sonicScream.models.Profile;

/**
 *
 * @author nmca
 */
public class ProfileManagerControllerTest
{
    
    
    public ProfileManagerControllerTest()
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
     * Test of getSelectedProfile method, of class ProfileManagerController.
     */
    @Test
    public void testGetSelectedProfile()
    {        
        ProfileManagerController controller = new ProfileManagerController();
        Profile defaultProfile = new Profile();
        controller.setSelectedProfile(defaultProfile);
        assertEquals(controller.getSelectedProfile(), defaultProfile);                
    }

    /**
     * Test of setSelectedProfile method, of class ProfileManagerController.
     */
    @Test
    public void testSetSelectedProfile()
    {
        ProfileManagerController controller = new ProfileManagerController();
        Profile defaultProfile = new Profile();
        assertEquals(controller.getSelectedProfile(), null);
        controller.setSelectedProfile(defaultProfile);
        assertEquals(controller.getSelectedProfile(), defaultProfile);
    }    
}
