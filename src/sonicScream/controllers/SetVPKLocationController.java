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
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sonicScream.services.ServiceLocator;
import sonicScream.services.SettingsService;
import sonicScream.utilities.Constants;

/**
 * FXML Controller class
 *
 * @author Neil McAlister
 */
public class SetVPKLocationController implements Initializable
{

    @FXML
    private Label StatusLabel;

    @FXML
    private TextField VPKBox;    

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        BooleanBinding vpkFoundBinding = Bindings.createBooleanBinding(
                () ->
                {                   
                    String path = VPKBox.getText();
                    if (path == null || !path.contains(File.separator))
                    {
                        return false;
                    }
                    path = path.substring(path.lastIndexOf(File.separator) + 1, path.length());
                    return path.equals("pak01_dir.vpk");
                }, VPKBox.textProperty());

        StatusLabel.textProperty().bind(Bindings
                .when(vpkFoundBinding)
                .then("Ready!")
                .otherwise("Dota 2 main VPK not found"));

        StatusLabel.textFillProperty().bind(Bindings
                .when(vpkFoundBinding)
                .then(Color.GREEN)
                .otherwise(Color.RED));       
    }

    @FXML
    private void handleBrowseButtonPressed(ActionEvent event)
    {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Find pak01.vpk");
        chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("VPK Files", "*.vpk"));
        
        Stage currentStage = (Stage) StatusLabel.getScene().getWindow();
        File result = chooser.showOpenDialog(currentStage);
        if (result == null)
        {
            return;
        }
        try
        {
            VPKBox.setText(result.getCanonicalPath());
        }
        catch (IOException ex)
        {
            System.err.printf("Unable to open %s: %s", result.getName(), ex.getMessage());
        }
    }

    @FXML
    private void handleSaveButtonPressed(ActionEvent event)
    {
        SettingsService settings = (SettingsService) ServiceLocator.getService(SettingsService.class);
        settings.putSetting(Constants.SETTING_MAIN_VPK_PATH, VPKBox.getText());
        settings.putSetting(Constants.SETTING_MAIN_VPK_DIR, Paths.get(VPKBox.getText()).getParent().toString());
        
        Stage currentStage = (Stage) StatusLabel.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void handleCancelButtonPressed(ActionEvent event)
    {
        Stage currentStage = (Stage) StatusLabel.getScene().getWindow();
        currentStage.close();
    }

}
