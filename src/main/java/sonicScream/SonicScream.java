/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonicScream;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sonicScream.controllers.SetVPKLocationController;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import sonicScream.services.VPKFileService;
import sonicScream.utilities.Constants;

/**
 *
 * @author Neil McAlister
 */
public class SonicScream extends Application
{

    @Override
    public void start(Stage stage) throws Exception
    {
        Thread.currentThread().setUncaughtExceptionHandler( (thread, throwable) 
                -> onUnhandledException(thread, throwable));
                
        configureServiceLocator();
        SettingsService settings = (SettingsService) ServiceLocator.getService(SettingsService.class);
        if (settings.getSetting(Constants.SETTING_MAIN_VPK_PATH) == null)
        {
            setVPKPaths(settings);
        }
        
        URL location = getClass().getResource("views/Main.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sonic Scream");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        //Workaround for W10 ComboBox crash. Reference: https://bugs.openjdk.java.net/browse/JDK-8132897
        System.setProperty("glass.accessible.force", "false");
        launch(args);
    }

    private void configureServiceLocator()
    {
        ServiceLocator.initialize();

        try
        {
            Path settingsFile = Paths.get(Constants.SETTINGS_FILE_NAME);
            if(!Files.exists(settingsFile))
            {
                settingsFile = Files.createFile(settingsFile);
            }
            Path crcFile = Paths.get(Constants.CRC_CACHE_FILE_NAME);
            if(!Files.exists(crcFile))
            {
                crcFile = Files.createFile(crcFile);
            }
            Path profileDir = Paths.get(Constants.PROFILE_FILES_DIRECTORY);
            if(!Files.exists(profileDir))
            {
                profileDir = Files.createDirectory(profileDir);
            }
            ServiceLocator.registerService(SettingsService.class, new SettingsService(settingsFile, crcFile, profileDir));

            SettingsService settings = (SettingsService) ServiceLocator.getService(SettingsService.class);
            String mainVPKPath = settings.getSetting(Constants.SETTING_MAIN_VPK_PATH);
            String mainVPKDir = settings.getSetting(Constants.SETTING_MAIN_VPK_DIR);
            ServiceLocator.registerService(VPKFileService.class, new VPKFileService(mainVPKPath, mainVPKDir));
        }
        catch (IOException ex)
        {
            System.err.printf("Unable to register SettingsService: %s", ex.getMessage());
        }

    }

    private void setVPKPaths(SettingsService settings)
    {
        try
        {
            URL location = getClass().getResource("/sonicScream/views/SetVPKLocation.fxml");
            FXMLLoader loader = new FXMLLoader(location);
            Parent root = loader.load();
            SetVPKLocationController controller = loader.<SetVPKLocationController>getController();
            controller.setNavigationSource(Constants.navigationSource.STARTUP);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        }
        catch (IOException ex)
        {
            //well, now we're kinda screwed
            System.err.printf("Unable to open VPKPath dialog: %s", ex.getMessage());
        }
        if(settings.getSetting(Constants.SETTING_MAIN_VPK_PATH) == null)
        {
            Platform.exit();
        }
    }
    
    /**
     * Called when the application is closing by normal means.
     */
    @Override public void stop()
    {
        SettingsService settings = (SettingsService)ServiceLocator.getService(SettingsService.class);
        settings.saveSettings();
    }
    
    private void onUnhandledException(Thread thread, Throwable throwable)
    {
        //TODO: Make this an error dialog
        throwable.printStackTrace();        
    }
}
