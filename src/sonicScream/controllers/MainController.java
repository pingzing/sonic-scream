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
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javax.swing.tree.TreeModel;
import sonicScream.models.Script;
import sonicScream.utilities.ScriptParser;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;

/**
 *
 * @author Neil McAlister
 */
public class MainController implements Initializable
{

    @FXML
    private Label scriptTreeLabel;
    @FXML
    private TreeView scriptTree;
    @FXML
    private ComboBox scriptComboBox;

    private List<TreeItem<String>> _treeModel;

    @FXML
    private void handleButtonAction(ActionEvent event)
    {            
        File folder = new File("src/sonicScream/assets/test");
        List<Script> scripts = Arrays.asList(folder.listFiles())
            .stream()
            .map(f -> new Script(f))
            .collect(Collectors.toList());        
        scriptComboBox.getItems().addAll(scripts);        
        
        System.out.println("Done!");
    }

    @FXML
    private void handleComboBoxAction(ActionEvent event)            
    {
        Script selectedFile = (Script) scriptComboBox.getValue();
        scriptTree.setRoot(selectedFile.getRoodNode());        
    }

    //workaround for W10 Intel driver crash
    @FXML
    private void handleComboBoxMousePressed(MouseEvent event)
    {
        scriptComboBox.requestFocus();
    }
    
    @FXML
    private void handleToFileButtonAction(ActionEvent event)
    {
        Script script = (Script)scriptComboBox.getValue();
        String fullScript = script.getScriptAsString();
        
        File outFile = new File("out.vsndevts");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outFile)))
        {
            writer.write(fullScript);
        }
        catch(IOException ex)
        {
            System.err.println("failed to write out file!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //TODO
    }

}
