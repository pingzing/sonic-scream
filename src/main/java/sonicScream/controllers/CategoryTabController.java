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
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import sonicScream.models.Category;
import sonicScream.models.Script;

/**
 * FXML Controller class
 * 
 */
public final class CategoryTabController extends Tab
{
    @FXML
    private TreeView CategoryTabTreeView;

    @FXML
    private ComboBox CategoryTabComboBox;
    
    @FXML
    private Label CategoryTabLabel;

    private Category _category;
    
    private ObjectProperty selectedScript = new SimpleObjectProperty();
    public final Object getSelectedScript() { return selectedScript.get(); }
    public ObjectProperty selectedScriptProperty() { return selectedScript; }
    
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

        this.textProperty().bind(category.categoryNameProperty());        
        CategoryTabComboBox.itemsProperty().bind(category.categoryScriptsProperty());
        
        //These two bindings handle changing between categories with only a single 
        //script (items) and those with multiple (everything else)
        CategoryTabComboBox.visibleProperty().bind(
                Bindings.greaterThan(category.categoryScriptsProperty().sizeProperty(), 1)
        );

        if( category.categoryScriptsProperty().size() > 1)
        {
            selectedScriptProperty().bind(CategoryTabComboBox.valueProperty());
        }
        else
        {
            selectedScriptProperty().bind(CategoryTabTreeView.getSelectionModel().selectedItemProperty());
        }
        
        ObservableList<Script> test = category.categoryScriptsProperty().get();
        
        if(category.categoryScriptsProperty().get() != null && !category.categoryScriptsProperty().get().isEmpty())
        {
            CategoryTabComboBox.valueProperty().set(category.categoryScriptsProperty().get(0));
            handleComboBoxChanged(null);
        }
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
                CategoryTabTreeView.setRoot(((Script)CategoryTabComboBox.getValue()).getRootNode());
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
}
