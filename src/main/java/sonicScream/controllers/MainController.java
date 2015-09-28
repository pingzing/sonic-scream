package sonicScream.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import sonicScream.models.Category;
import sonicScream.models.Profile;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import sonicScream.utilities.Constants;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.*;
import org.apache.commons.lang3.NotImplementedException;

/**
 *
 * @author Neil McAlister
 */
public class MainController implements Initializable
{
    private Profile _activeProfile = null;

    @FXML
    private TabPane MainTabPane;
    
    @FXML
    private Button ReplaceButton;
    
    @FXML 
    private Button RevertButton;

    private List<TreeItem<String>> _treeModel;
    private SingleSelectionModel<Tab> _tabSelection;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        _activeProfile = getActiveProfile().orElse(null);
        if (_activeProfile != null)
        {
            SettingsService settings = (SettingsService) ServiceLocator.getService(SettingsService.class);
            settings.putSetting(Constants.SETTING_ACTIVE_PROFILE, _activeProfile.getProfileName());
            for (Category c : _activeProfile.getCategories())
            {
                MainTabPane.getTabs().add(new CategoryTabController(c));
            }
        }
       
        _tabSelection = MainTabPane.getSelectionModel();
        _tabSelection.selectFirst();   
        
        BooleanBinding enableButtonsBinding = ((CategoryTabController)_tabSelection.selectedItemProperty().get())
                    .selectedScriptNodeProperty().isNull()
                .or(((CategoryTabController)_tabSelection.selectedItemProperty().get())
                    .selectedScriptNodeIsLeafProperty().not());
        
        //Only enable the Replace button if we actually have a sound selected.
        ReplaceButton.disableProperty().bind(enableButtonsBinding);
        RevertButton.disableProperty().bind(enableButtonsBinding);
    }

    private Optional<Profile> getActiveProfile()
    {
        SettingsService settings = (SettingsService) ServiceLocator.getService(SettingsService.class);
        String profileName = settings.getSetting(Constants.SETTING_ACTIVE_PROFILE);
        if (profileName == null) //no active profile saved
        {
            return getActiveProfileFromDialog();                        
        }

        Profile active = settings.getProfile(profileName); //Saved active profile no longer exis
        if (active == null)
        {
            return getActiveProfileFromDialog();
        }

        return Optional.ofNullable(active);
    }

    private Optional<Profile> getActiveProfileFromDialog()
    {         
        try
        {
            URL location = getClass().getResource("/sonicScream/views/ProfileManager.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();                        
            ProfileManagerController controller = loader.<ProfileManagerController>getController();
            
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.minHeightProperty().set(325.0);
            stage.minWidthProperty().set(500.0);
            stage.maxHeightProperty().set(325.0);
            stage.maxWidthProperty().set(500.0);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();    
            
            return Optional.ofNullable(controller.getSelectedProfile());            
        }
        catch (IOException ex)
        {
            System.err.printf("Unable to open Profile Manager dialog: %s", ex.getMessage());
            return Optional.empty();
        }
    }
    
    @FXML
    private void handleToFileButtonAction(ActionEvent event)
    {
        throw new NotImplementedException("Not implemented yet");
    }

    @FXML
    private void handleFindVPKButtonAction(ActionEvent event)
    {
        try
        {
            URL location = getClass().getResource("/sonicScream/views/SetVPKLocation.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException ex)
        {
            System.err.printf("Unable to open VPK dialog: %s", ex.getMessage());
        }
    }
    
    @FXML
    private void handleProfileButtonAction(ActionEvent event)
    {
        _activeProfile = getActiveProfileFromDialog().orElse(null);
        SettingsService settings = (SettingsService)ServiceLocator.getService(SettingsService.class);
        settings.putSetting(Constants.SETTING_ACTIVE_PROFILE, _activeProfile.getProfileName());
    }    
    
    @FXML
    private void handleReplaceButtonAction(ActionEvent event)
    {        
        try
        {            
            CategoryTabController tab = (CategoryTabController) _tabSelection.getSelectedItem();
            tab.replaceSound();
        }
        catch (IOException ex)
        {
            //TODO: Tell user "sorry, couldn't XYZ the file"
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void handleRevertButtonAction(ActionEvent event)
    {
        
            CategoryTabController tab = (CategoryTabController) _tabSelection.getSelectedItem();
            tab.revertSound();
        
    }
}
