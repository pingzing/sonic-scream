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

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.NotImplementedException;
import sonicScream.models.Profile;
import sonicScream.services.ProfileNameExistsException;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import sonicScream.utilities.Constants;

public class ProfileManagerController implements Initializable
{

    private ObservableList<Profile> _profiles;

    private ObjectProperty<Profile> selectedProfile = new SimpleObjectProperty<>();

    public final Profile getSelectedProfile()
    {
        return selectedProfile.get();
    }

    public final void setSelectedProfile(Profile value)
    {
        selectedProfile.set(value);
    }

    public ObjectProperty<Profile> selectedProfileProperty()
    {
        return selectedProfile;
    }

    @FXML
    private AnchorPane RootPane;

    @FXML
    private ComboBox SelectedProfileComboBox;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        SettingsService settings = (SettingsService) ServiceLocator.getService(SettingsService.class);
        _profiles = FXCollections.observableArrayList(settings.getAllProfiles());
        SelectedProfileComboBox.itemsProperty().set(_profiles); //Don't need to bind here, _profiles should update itself.                
        SelectedProfileComboBox.valueProperty().addListener(
                (ObservableValue ov, Object oldVal, Object newVal) ->
                {
                    if (oldVal instanceof String || !(newVal instanceof String))
                    {
                        return;
                    }
                    SelectedProfileComboBox.editableProperty().set(false);
                    if (settings.getProfile((String) newVal) != null)
                    {
                        //Tell the user NO WAY JOSE
                    }

                    Profile newProfile = new Profile((String) newVal);
                    _profiles.add(newProfile);
                    selectedProfile.set(newProfile);
                });

        SelectedProfileComboBox.valueProperty().bindBidirectional(selectedProfile);

        if (!_profiles.isEmpty())
        {
            String activeSetting = settings.getSetting(Constants.SETTING_ACTIVE_PROFILE);
            if(activeSetting != null)
            {
                selectedProfile.set(_profiles.stream()
                        .filter(p -> p.getProfileName().equals(activeSetting))
                        .findFirst()
                        .orElse(null));                
            }
            else
            {
                selectedProfile.set(_profiles.get(0));
            }
        }
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
        _profiles.remove((Profile) SelectedProfileComboBox.getValue());
    }

    @FXML
    private void handleOkButtonPressed(ActionEvent event)
    {
        SettingsService settings = (SettingsService) ServiceLocator.getService(SettingsService.class);
        //Add newly-added profiles if there are no name conflicts
        _profiles.stream().filter((p) -> (settings.getProfile(p.getProfileName()) == null)).forEach((p) ->
        {
            try
            {
                settings.addProfile(p);
                settings.saveSettings();
            }
            catch (ProfileNameExistsException ex)
            {
                //Collect the bad profile names here
                System.err.println(ex);
            }
        });        
        //TODO: Tell the user there were name conflicts. Ask them "hey dude you 
        //sure bout this", and cancel closing if they say "no"
        
        selectedProfile.set((Profile) SelectedProfileComboBox.getValue());
        Stage currentStage = (Stage) SelectedProfileComboBox.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void handleImportButtonPressed(ActionEvent event)
    {
        SettingsService settings = (SettingsService) ServiceLocator.getService(SettingsService.class);
        
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import profile");       
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Profiles", "*.xml"));
        File result = chooser.showOpenDialog(SelectedProfileComboBox.getScene().getWindow());
        //TODO: Reimplemnt using JAXB
        try
        {
            JAXBContext context = JAXBContext.newInstance(Profile.class);
            Profile importedProfile = (Profile)context.createUnmarshaller().unmarshal(result);
            _profiles.add(importedProfile);
            selectedProfile.set(importedProfile);
            settings.addProfile(importedProfile);
        }
        catch(ProfileNameExistsException ex)
        {
            throw new NotImplementedException("Profile name exists handler not implemented");
            //Tell the user a profile with that name already exists. NO CAN DO
        }
        catch(JAXBException ex)
        {
            throw new NotImplementedException("Invalid profile handler not implemented");
            //Tell the user that the profile is improperly formatted. ALSO NO CAN DO
        }
    }

}
