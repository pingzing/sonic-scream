/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import sonicScream.models.Script;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import sonicScream.utilities.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author Neil McAlister
 */
public class MainController implements Initializable
{

    private Profile _activeProfile = null;

    @FXML
    private TabPane MainTabPane;

    private List<TreeItem<String>> _treeModel;
    private SingleSelectionModel<Tab> tabSelection;

    @FXML
    private void handleToFileButtonAction(ActionEvent event)
    {
        CategoryTabController tab = (CategoryTabController) tabSelection.getSelectedItem();
        Script script = tab.selectedScriptProperty().get() != null ? (Script) tab.selectedScriptProperty().get() : null;
        if (script != null)
        {
            String outString = script.getScriptAsString();
            File file = new File("out.vsndevts");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file)))
            {
                writer.write(outString);
            }
            catch (IOException ex)
            {
                System.err.println("failed to write out file!");
            }
        }
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

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Profile savedActiveProfile = getActiveProfile();

        for (Category c : savedActiveProfile.getCategories())
        {
            MainTabPane.getTabs().add(new CategoryTabController(c));
        }
        tabSelection = MainTabPane.getSelectionModel();
        tabSelection.selectFirst();
    }

    private Profile getActiveProfile()
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

        return active;
    }

    private Profile getActiveProfileFromDialog()
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
            
            return controller.getSelectedProfile();            
        }
        catch (IOException ex)
        {
            System.err.printf("Unable to open Profile Manager dialog: %s", ex.getMessage());
            return null;
        }        
    }
}
