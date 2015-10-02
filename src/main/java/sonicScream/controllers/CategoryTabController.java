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
import java.util.List;

import info.ata4.vpk.VPKEntry;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import sonicScream.models.Category;
import sonicScream.models.Enums.CategoryDisplayMode;
import sonicScream.models.Script;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import static sonicScream.utilities.FileIOUtilities.chooseSoundFile;

import sonicScream.services.VPKFileService;
import sonicScream.utilities.SettingsUtils;
import sonicScream.utilities.StringParsing;
import sonicScream.utilities.TreeUtils;

public final class CategoryTabController extends Tab
{        
    private final Category _category;
    
    @FXML
    private TreeView CategoryTabTreeView;

    @FXML
    private ComboBox CategoryTabComboBox;
    
    @FXML
    private Label CategoryTabScriptValueLabel;
    
    @FXML
    private Button SwapDisplayModeButton;
    
    @FXML
     private Button ExpandAllButton;
            
    @FXML
    private Button CollapseAllButton;
    
    private ObjectProperty<TreeItem<String>> selectedScriptNode = new SimpleObjectProperty();
    public final Object getSelectedScriptNode() { return selectedScriptNode.get(); }
    public ObjectProperty<TreeItem<String>> selectedScriptNodeProperty() { return selectedScriptNode; }
    
    private ObjectProperty<Script> selectedScript = new SimpleObjectProperty();
    public final Object getSelectedScript() { return selectedScript.get(); }
    /**
     * Property for the Script object that is currently selected in the ComboBox (or, if the 
     * Category only has a single script, that single script) or null.
     * @return 
     */
    public ObjectProperty<Script> selectedScriptProperty() { return selectedScript; }
    
    private BooleanProperty selectedScriptNodeIsLeaf = new SimpleBooleanProperty(false);
    public final Object getSelectedScriptNodeIsLeaf() { return selectedScriptNodeIsLeaf.get(); }
    /**
     * Null-safe, bindable property to determine whether or not the currently-selected
     * node in the TreeView is a leaf.
     * @return 
     */
    public BooleanProperty selectedScriptNodeIsLeafProperty(){ return selectedScriptNodeIsLeaf; }
    
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

        selectedScriptNodeProperty().bind(CategoryTabTreeView.getSelectionModel()
                .selectedItemProperty());
        
        selectedScript.bind(CategoryTabComboBox.getSelectionModel().selectedItemProperty());        

        if(_category.getCategoryScripts() != null && !_category.getCategoryScripts().isEmpty())
        {
            CategoryTabComboBox.valueProperty().set(_category.getCategoryScripts().get(0));
            handleComboBoxChanged(null);
        }         
        
        SwapDisplayModeButton.textProperty().bind(Bindings
                .when(displayMode.isEqualTo(CategoryDisplayMode.SIMPLE))
                .then("Advanced >>")
                .otherwise("<< Simple"));

        CategoryTabTreeView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) ->
            {
                if(newValue != null)
                {
                    TreeItem<String> scriptValue = TreeUtils.getRootMinusOne((TreeItem<String>) newValue);
                    TreeItem<String> selectedValue = (TreeItem<String>) newValue;
                    CategoryTabScriptValueLabel.setText(scriptValue.getValue());
                    selectedScriptNodeIsLeaf.set(selectedValue.isLeaf());
                }
            });
    }
    
    @FXML
    private void handleComboBoxChanged(ActionEvent event)
    {
        Task task = new Task() {
            @Override protected Object call() throws Exception
            {
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

    private void changeDisplayMode(CategoryDisplayMode newMode)
    {
        displayMode.set(newMode);
        handleComboBoxChanged(null);
    }

    /**
     * Forces the CategoryTabTreeView to select the nth leaf in the tree. Primarily used for testing purposes.
     * @param leaf The nth leaf to select, starting at 1.
     */
    public void forceSelectNthLeaf(int leaf)
    {
        int leavesCount = 0;
        TreeItem<String> nodeToSelect = TreeUtils.findNthLeaf(CategoryTabTreeView.getRoot(), 0, 1);
        CategoryTabTreeView.getSelectionModel().select(nodeToSelect);
    }

    /**
     * Replaces the currently-selected node's script value with a generated value based on a user-selected sound file.
     * Updates the selected Script's values, converts it to a local Script, writes the Script out to disk, and copies
     * the selected sound file to the appropriate location in the user's profile folder.
     * Only available to the user in Simple mode.
     * @throws IOException
     */
    public void replaceSound() throws IOException
    {
        TreeItem<String> selectedNode = (TreeItem<String>)CategoryTabTreeView.getSelectionModel().getSelectedItem();
        if(!selectedNode.isLeaf())
        {            
            return; //Shouldn't even be possible, but just in case
        }
        
        Stage currentStage = (Stage)CategoryTabScriptValueLabel.getScene().getWindow();
        Path newSoundFile = chooseSoundFile(currentStage);
        
        replaceSound(newSoundFile);
    }

    /**
     * See replaceSound(). This method is used for testing, so we can avoid needing to invoke a FileChooser.
     * @param newSoundFile The sound file to use for replacement.
     * @throws IOException
     */
    public void replaceSound(Path newSoundFile) throws IOException
    {
        TreeItem<String> selectedNode = (TreeItem<String>)CategoryTabTreeView.getSelectionModel().getSelectedItem();

        SettingsService settings = (SettingsService)ServiceLocator.getService(SettingsService.class);
        Script activeScript = (Script)CategoryTabComboBox.getValue();
        activeScript.getVPKPath();

        String trimmedFileName = newSoundFile
                .getFileName().toString()
                .toLowerCase()
                .replace(" ", "_");
        String profileDir = SettingsUtils.getProfileDirectory(activeScript.getParentCategoryName()).toString();
        Path destPath = Paths.get(profileDir, "/sonic-scream/sounds", trimmedFileName);

        if(!Files.exists(destPath))
        {
            Files.createDirectories(destPath.getParent());
        }
        Files.copy(newSoundFile, destPath, StandardCopyOption.REPLACE_EXISTING);
        selectedNode.setValue("sounds/" + trimmedFileName.replace(".mp3", ".vsnd").replace(".wav", ".vsnd"));

        activeScript.convertToLocalScript();
    }

    public void revertSound()
    {        
        TreeItem<String> selectedNode = (TreeItem<String>)CategoryTabTreeView.getSelectionModel().getSelectedItem();
        if(!selectedNode.isLeaf())
        {
            return; //Shouldn't even be possible, but just in case
        }
        //We use this later to find the correct node to select after a reversion.
        int nodeParentIndex = selectedNode.getParent().getParent().getChildren().indexOf(selectedNode.getParent());
        
        Script activeScript = (Script)CategoryTabComboBox.getValue();

        //get the index of the selected node relative to its parent.
        int selectedWaveIndex = selectedNode.getParent().getChildren().indexOf(selectedNode);
        VPKFileService vpkService = (VPKFileService)ServiceLocator.getService(VPKFileService.class);
        Script vpkScript = new Script(vpkService.getVPKEntry(activeScript.getVPKPath()), _category);
        List<TreeItem<String>> vpkWaves = TreeUtils.getWaveStrings(vpkScript.getRootNode()).orElse(null);
        if(vpkWaves != null && vpkWaves.size() > selectedWaveIndex)
        {
            TreeItem<String> selectedNodeInRoot = TreeUtils.getWaveStrings(activeScript.getRootNode()).get().get(selectedWaveIndex);
            TreeItem<String> selectedNodeInVPKRoot = vpkWaves.get(selectedWaveIndex);
            String vpkNodeString = StringUtils.normalizeSpace((selectedNodeInVPKRoot.getValue()));
            selectedNodeInRoot.setValue(vpkNodeString);
            selectedNode.setValue(StringParsing.rootSoundToSimpleSound(vpkNodeString));
        }
        //if the index does NOT exist, remove the index from the root node, and
        //then do all the updating
        else
        {
            TreeItem<String> selectedNodeInRoot = TreeUtils.getWaveStrings(activeScript.getRootNode()).get().get(selectedWaveIndex);
            selectedNodeInRoot.getParent().getChildren().remove(selectedNodeInRoot);
            selectedNode.getParent().getChildren().remove(selectedNode);
            //TODO: Delete the parent if it no longer has any children.
        }
    }        
}
