/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sonicScream.controllers;

import java.io.File;
import java.io.IOException;
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
        File folder = new File("C:/Users/nmca/Downloads/voscripts");
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
        scriptTree.setRoot(selectedFile.getScriptTree().get(0));        
    }

    //workaround for W10 Intel driver crash
    @FXML
    private void handleComboBoxMousePressed(MouseEvent event)
    {
        scriptComboBox.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //TODO
    }

}
