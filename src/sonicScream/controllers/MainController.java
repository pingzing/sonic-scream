/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonicScream.controllers;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import sonicScream.models.Script;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sonicScream.models.Category;
import sonicScream.models.Profile;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import sonicScream.utilities.Constants;

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
        CategoryTab tab = (CategoryTab) tabSelection.getSelectedItem();
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
        catch(IOException ex)
        {
            System.err.printf("Unable to open VPK dialog: %s", ex.getMessage());
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {        
        setActiveProfile();
        
        File folder = new File("src/sonicScream/assets/test");
        List<Script> scripts = Arrays.asList(folder.listFiles())
                .stream()
                .map(f -> new Script((File)f, new Category(Constants.CATEGORY_HEROES)))
                .collect(Collectors.toList());

        Category testCategory = new Category(Constants.CATEGORY_VOICES);
        testCategory.setCategoryScripts(scripts);

        MainTabPane.getTabs().add(new CategoryTab(testCategory));
        tabSelection = MainTabPane.getSelectionModel();
    }

    private void setActiveProfile()
    {
        SettingsService settings = (SettingsService)ServiceLocator.getService(SettingsService.class);
        String profileName = settings.getSetting(Constants.SETTING_ACTIVE_PROFILE); 
        if(profileName == null)
        {                        
            List<Profile> allProfiles = settings.getAllProfiles();
            ChoiceDialog dialog = new ChoiceDialog(allProfiles.get(0), allProfiles);
            dialog.setTitle("Select a profile");            
            dialog.showAndWait()
                .filter(response -> response instanceof Profile)
                .ifPresent(response -> _activeProfile = (Profile)response);
            return;
        }
        
        Profile active = settings.getProfile(profileName);
        if(active == null)
        {
            List<Profile> allProfiles = settings.getAllProfiles();
            ChoiceDialog dialog = new ChoiceDialog(allProfiles.get(0), allProfiles);
            dialog.setTitle("Select a profile");
            dialog.showAndWait()
                .filter(response -> response instanceof Profile)
                .ifPresent(response -> _activeProfile = (Profile)response);                    
        }        
    }
}
