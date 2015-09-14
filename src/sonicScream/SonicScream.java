/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonicScream;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        URL location = getClass().getResource("views/Main.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();

        configureServiceLocator();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    private void configureServiceLocator()
    {
        ServiceLocator.initialize();
        
        try
        {
            File settingsFile = new File(Constants.SETTINGS_FILE_NAME);
            settingsFile.createNewFile();
            File crcFile = new File(Constants.CRC_CACHE_FILE_NAME);
            crcFile.createNewFile();
            File profileFile = new File(Constants.PROFILES_FILE_NAME);
            profileFile.createNewFile();
            ServiceLocator.registerService(SettingsService.class, new SettingsService(settingsFile, crcFile, profileFile));

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

}
