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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sonicScream.models.Profile;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;

public class ProfileManagerController implements Initializable
{       
    private ObservableList<Profile> _profiles;
    private Profile _selectedProfile;
    
    @FXML 
    private AnchorPane RootPane;
    
    @FXML
    private ComboBox SelectedProfileComboBox;    
        
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {        
        SettingsService settings = (SettingsService)ServiceLocator.getService(SettingsService.class);
        _profiles = FXCollections.observableArrayList(settings.getAllProfiles());
        SelectedProfileComboBox.itemsProperty().set(_profiles); //Don't need to bind here, _profiles should update itself.        
        SelectedProfileComboBox.valueProperty().addListener((ov, oldVal, newVal) ->
        {
            if(oldVal instanceof String || !(newVal instanceof String))
            {
                return;
            }
            SelectedProfileComboBox.editableProperty().set(false);
            Profile newProfile = new Profile((String)newVal);
             _profiles.add(newProfile);
            SelectedProfileComboBox.setValue(newProfile);
        });
    }
    
    public Profile getSelectedProfile()
    {
        return _selectedProfile;
    }
    
    @FXML
    private void handleComboBoxChanged(ActionEvent event)
    {
        _selectedProfile = (Profile)SelectedProfileComboBox.getValue();
    }
    
    @FXML
    private void handleAddButtonPressed(ActionEvent event)
    {        
        SelectedProfileComboBox.editableProperty().set(true);  
        SelectedProfileComboBox.requestFocus();
    }
    
    @FXML
    private void handleDeleteButtonPressed(ActionEvent event)
    {
        
    }
    
    @FXML
    private void handleOkButtonPressed(ActionEvent event)
    {
        _selectedProfile = (Profile)SelectedProfileComboBox.getValue();
        Stage currentStage = (Stage) SelectedProfileComboBox.getScene().getWindow();                
        currentStage.close();        
    }
    
    @FXML
    private void handleImportButtonPressed(ActionEvent event)
    {
        
    }
    
}
