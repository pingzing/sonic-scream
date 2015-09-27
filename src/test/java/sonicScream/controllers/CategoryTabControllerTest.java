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
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.sun.scenario.Settings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Rule;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import sonicScream.models.Category;
import sonicScream.models.Profile;
import sonicScream.models.Script;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import sonicScream.utilities.Constants;
import sonicScream.utilities.FilesEx;
import testHelpers.JavaFXThreadingRule;

/**
 * @author nmca
 */
public class CategoryTabControllerTest
{
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    Profile profile = mock(Profile.class);
    private CategoryTabController controller;
    private static final String profileName = "testProfile" + UUID.randomUUID().toString();

    public CategoryTabControllerTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws IOException
    {
    }

    @AfterClass
    public static void tearDownClass() throws IOException
    {
        FilesEx.deleteDirectoryRecursive(Paths.get(Constants.PROFILES_DIRECTORY, profileName));
    }

    @Before
    public void setUp() throws URISyntaxException, IOException
    {
        URL location = getClass().getResource("/sonicScream/assets/test");
        Path folder = Paths.get(location.toURI());

        ObservableList<Script> scriptList = FXCollections.observableArrayList();

        Category category = mock(Category.class);
        when(category.getCategoryScripts()).thenReturn(scriptList);
        when(category.categoryNameProperty()).thenReturn(new SimpleStringProperty(Constants.CATEGORY_HEROES));

        List<Path> paths = FilesEx.listFiles(folder);
        Script realScript = new Script(paths.get(0), category);
        Script mockScript = mock(Script.class);//new Script(paths.get(0), category);
        when(mockScript.getRootNode()).thenReturn(realScript.getRootNode());
        when(mockScript.getSimpleTree()).thenReturn(realScript.getSimpleTree());
        when(mockScript.getParentCategoryName()).thenReturn(profileName);
        scriptList.add(mockScript);
        scriptList.add(mockScript);

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(category);
        when(profile.getCategories()).thenReturn(categories);

        profile.getCategories().add(category);

        controller = new CategoryTabController(profile.getCategories().get(0));
    }

    @After
    public void tearDown() throws IOException
    {
    }

    /**
     * Test of getSelectedScript method, of class CategoryTabController.
     */
    @Test
    public void testInitialization()
    {
        assert(true);
    }

    @Test
    /**
     * Test that replaceSound calls "convertToLocal" on the script, and copies the sound file to its new home.
     * Currently doesn't test that it writes out our newly-custom script. Need a more extensive Script mock for that.
     */
    public void testReplaceSound() throws Exception
    {
        SettingsService settingsMock = mock(SettingsService.class);
        when(settingsMock.getSetting(Constants.SETTING_ACTIVE_PROFILE)).thenReturn(profileName);

        ServiceLocator.initialize();
        ServiceLocator.registerService(SettingsService.class, settingsMock);

        URL soundLocation = getClass().getResource("/sonicScream/assets/test/test_sound.mp3");
        Path replacementSound = Paths.get(soundLocation.toURI());
        controller.forceSelectNthLeaf(1);
        controller.replaceSound(replacementSound);

        verify((Script) controller.getSelectedScript()).convertToLocalScript();
        Path testSoundPath = Paths.get(Constants.PROFILES_DIRECTORY, profileName, "sonic-scream", "sounds", "test_sound.mp3");
        assertEquals(Files.exists(testSoundPath), true);
    }
}
