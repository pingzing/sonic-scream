/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonicScream.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sonicScream.utilities.ScriptParser;

/**
 *
 * @author Neil McAlister
 */
public class MainController implements Initializable
{
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        File folder = new File("C:/Users/mcali/Desktop/Dota 2 Reborn example files/voscripts");
        for(File f  : folder.listFiles())
        {            
            try
            {
                ScriptParser.parseScript(f);
            }
            catch(IOException ex)
            {
                ex.printStackTrace();
            }
        }
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
}
