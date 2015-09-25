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
package sonicScream.controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sonicScream.models.Category;
import sonicScream.models.Enums.CategoryDisplayMode;
import sonicScream.models.Script;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import sonicScream.utilities.Constants;
import static sonicScream.utilities.FileIOUtilities.chooseSoundFile;
import sonicScream.utilities.SettingsUtils;

public final class CategoryTabController extends Tab
{        
    private final Category _category;
    
    @FXML
    private TreeView CategoryTabTreeView;

    @FXML
    private ComboBox CategoryTabComboBox;
    
    @FXML
    private Label CategoryTabLabel;  
    
    @FXML
    private Button SwapDisplayModeButton;
    
    @FXML
     private Button ExpandAllButton;
            
    @FXML
    private Button CollapseAllButton;
    
    private ObjectProperty selectedScript = new SimpleObjectProperty();
    public final Object getSelectedScript() { return selectedScript.get(); }
    public ObjectProperty selectedScriptProperty() { return selectedScript; }
    
    private ObjectProperty displayMode = new SimpleObjectProperty(CategoryDisplayMode.SIMPLE);        
    
    public CategoryTabController(Category category)
    {
        URL location = getClass().getResource("/sonicScream/views/CategoryTab.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        loader.setRoot(this);
        loader.setController(this);
        try
        {
            loader.load();
        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }

        _category = category;
        
        this.textProperty().bind(_category.categoryNameProperty());                
        CategoryTabComboBox.setItems(_category.getCategoryScripts());
        
        //These two bindings handle changing between categories with only a single 
        //script (items) and those with multiple (everything else)
        SimpleListProperty bindableList = new SimpleListProperty();
        bindableList.bind(new SimpleObjectProperty<>(_category.getCategoryScripts()));
        CategoryTabComboBox.visibleProperty().bind(
                Bindings.greaterThan(bindableList.sizeProperty(), 1)
        );

        // If we're dealing with single-script categories, (i.e. Items) then the 
        // "selected item" should be the currently selected script value, rather 
        // than currently selected script file.
        if( _category.getCategoryScripts().size() > 1)
        {
            selectedScriptProperty().bind(CategoryTabComboBox.valueProperty());
        }
        else
        {
            selectedScriptProperty().bind(CategoryTabTreeView.getSelectionModel()
                    .selectedItemProperty());
        }                
        
        if(_category.getCategoryScripts() != null && !_category.getCategoryScripts().isEmpty())
        {
            CategoryTabComboBox.valueProperty().set(_category.getCategoryScripts().get(0));
            handleComboBoxChanged(null);
        }         
        
        SwapDisplayModeButton.textProperty().bind(Bindings
                .when(displayMode.isEqualTo(CategoryDisplayMode.SIMPLE))
                .then("Advanced >>")
                .otherwise("<< Simple"));                
    }
    
    @FXML
    private void handleComboBoxChanged(ActionEvent event)
    {
        Task task = new Task() {
            @Override protected Object call() throws Exception
            {
                Script selected = (CategoryTabComboBox.getValue() instanceof Script ? (Script)CategoryTabComboBox.getValue() : null);
                if(selected != null)                
                {
                    CategoryTabLabel.textProperty().set(selected.getFriendlyScriptName());
                }                
                try
                {
                    TreeItem<String> simpleTree = 
                              displayMode.get() == CategoryDisplayMode.SIMPLE ? ((Script)CategoryTabComboBox.getValue()).getSimpleTree() 
                            : displayMode.get() == CategoryDisplayMode.ADVANCED ?((Script)CategoryTabComboBox.getValue()).getRootNode() 
                            : null;              
                    CategoryTabTreeView.setRoot(simpleTree);                
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                return null;
            }
        };        
        task.run();
    }    
    
    @FXML
    private void handleComboBoxMousePressed(MouseEvent mouseEvent)
    {
        CategoryTabComboBox.requestFocus();
    }
    
    @FXML 
    private void handleSwapDisplayModePressed(ActionEvent event)
    {
        if(displayMode.get() == CategoryDisplayMode.SIMPLE)
        {
            Script currentScript = (Script)CategoryTabComboBox.getValue();
            currentScript.setRootNode(currentScript.updateRootNodeWithSimpleTree());
            changeDisplayMode(CategoryDisplayMode.ADVANCED);
        }        
        else if(displayMode.get() == CategoryDisplayMode.ADVANCED)changeDisplayMode(CategoryDisplayMode.SIMPLE);
    }
    
    @FXML 
    private void handleExpandAllPressed(ActionEvent event)
    {
        recursiveSetNodeExpanded(CategoryTabTreeView.getRoot(), true);
    }
    
    @FXML
    private void handleCollapseAllPressed(ActionEvent event)
    {
        recursiveSetNodeExpanded(CategoryTabTreeView.getRoot(), false);
    }
    
    private void recursiveSetNodeExpanded(TreeItem<String> root, boolean expanded)
    {        
        if(root.getParent() != null) //Don't want to collapse the root!
        {
            root.setExpanded(expanded);
        }
        for(TreeItem<String> item : root.getChildren())            
        {
            if(item.getChildren().isEmpty())
            {
                item.setExpanded(expanded);
            }
            else
            {
                recursiveSetNodeExpanded(item, expanded);
            }
        }
    }

    /**
     * Resolve VPK scripts from the current category, and do cache checking
     * TODO: cache checking
     */
    public void updateCategoryScripts()
    {
        for(Script s : _category.getCategoryScripts())
        {

        }
    }
    
    public void replaceSound() throws IOException
    {
        TreeItem<String> selectedNode = (TreeItem<String>)CategoryTabTreeView.getSelectionModel().getSelectedItem();        
        if(!selectedNode.isLeaf())
        {
            //TODO: Optionally, display an error? probably not necessary...
            //Or better, have a bindable property that the buttons on Main can hook into?
            //We probably only want to allow this functionality in Simple mode
            return;
        }
        
        Stage currentStage = (Stage)CategoryTabLabel.getScene().getWindow();
        Path newSoundFile = chooseSoundFile(currentStage);
        
        SettingsService settings = (SettingsService)ServiceLocator.getService(SettingsService.class);        
        Script activeScript = (Script)selectedScript.get();
        activeScript.getVPKPath();
        
        String trimmedFileName = newSoundFile
                .getFileName().toString()
                .toLowerCase()
                .replace(" ", "_");
        String profileDir = SettingsUtils.getProfileDirectory(settings.getSetting(Constants.SETTING_ACTIVE_PROFILE)).toString();
        Path destPath = Paths.get(profileDir, "/sonic-scream/sounds", trimmedFileName);
        if(!Files.exists(destPath))
        {
            Files.createDirectories(destPath.getParent());
        }
        Files.copy(newSoundFile, destPath, StandardCopyOption.REPLACE_EXISTING);
        selectedNode.setValue("sounds/" + trimmedFileName.replace(".mp3", ".vsnd").replace(".wav", ".vsnd"));
    }
    
    private void changeDisplayMode(CategoryDisplayMode newMode)
    {
        displayMode.set(newMode);
        handleComboBoxChanged(null);
    }
}
