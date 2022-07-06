package com.example.gui_po2;

import data.Cipher;
import data.Entry;
import data.Group;
import javafx.application.Platform;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.TextFieldTableCell;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;

import java.util.ResourceBundle;


import static com.example.gui_po2.App.stg;

public class ManagerController implements Initializable  {

    FileChooser fileChooser = new FileChooser();
    BufferedWriter writer;
    BufferedReader reader;
    String source;


    @FXML
    TableColumn<Entry,String> title;
    @FXML
    TableColumn<Entry,String> username;
    @FXML
    TableColumn<Entry,String> password;
    @FXML
    TableView<Entry> tableView;
    @FXML
    ListView<data.Group> groups;
    @FXML
    Label timeStamp;
    @FXML
    TextField newCtg;
    @FXML
    Button addCtg;
    @FXML
    Button remCtg;
    @FXML
    TextField newTitle;
    @FXML
    TextField newUsername;
    @FXML
    TextField newPassword;


    public File chooseFile(){
        File file;
        file = fileChooser.showOpenDialog(new Stage());
        if(file == null){
            file = new File("src/main/java/data/ciphered.txt");
        }
        return file;
    }

    @FXML
    public void readFromFile() throws IOException {
        File file = chooseFile();
        source = file.getAbsolutePath();
        if(file.getName().contains(".txt")){
            reader = new BufferedReader(new FileReader(source));
            data.Group currentGroup = null;
            boolean groupNotNull = false;

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] elements = line.split(" ");
                if(elements.length == 1){
                    if(groupNotNull) {
                        groups.getItems().add(currentGroup);
                    }
                    currentGroup = new data.Group(Cipher.decipher(elements[0]));
                    groupNotNull = true;
                }
                else {
                     Entry entryTemp = new Entry(
                             Cipher.decipher(elements[0]),
                             Cipher.decipher(elements[1]),
                             Cipher.decipher(elements[2]));
                     currentGroup.addEntry(entryTemp);
                }
            }

            groups.getItems().add(currentGroup);
            reader.close();
            groups.refresh();

        }
    }
    @FXML
    public void changeMode(){
        ObservableList<String> sheets = stg.getScene().getStylesheets();
        String current = sheets.get(0);
        String opposite = current.contains("lightTheme") ? "darkTheme" : "lightTheme";
        sheets.clear();
        sheets.add(getClass().getResource(opposite + ".css").toExternalForm());
    }
    @FXML
    public void saveAll() throws IOException {
        File file = new File(source);
        writer = new BufferedWriter(new FileWriter(file));
        Group[] saveGroups = groups.getItems().toArray(new Group[20]);

        for(Group a : saveGroups){
            if(a == null){
                break;
            }
            writer.write(Cipher.cipher(a.getName()) + '\n');

            for(Entry b : a.getEntries() ){
                StringBuilder tempSave = new StringBuilder();
                tempSave.append(Cipher.cipher(b.getTitle())).append(" ");
                tempSave.append(Cipher.cipher(b.getUsername())).append(" ");
                tempSave.append(Cipher.cipher(b.getPassword()));
                writer.write(tempSave.toString() + '\n');
            }

        }
        writer.close();

    }
    @FXML
    public void titleEdit(TableColumn.CellEditEvent<Entry,String> event){
        Entry entry = tableView.getSelectionModel().getSelectedItem();
        if(event.getNewValue().matches(("\\S+"))){
            entry.setTitle(event.getNewValue());
        }
    }
    @FXML
    public void usernameEdit(TableColumn.CellEditEvent<Entry,String> event){
        Entry entry = tableView.getSelectionModel().getSelectedItem();
        if(event.getNewValue().matches(("\\S+"))) {
            entry.setUsername(event.getNewValue());
        }

    }
    @FXML
    public void addNewEntry(){
        if(newTitle.getText().matches(("\\S+")) && newUsername.getText().matches(("\\S+"))
                && newPassword.getText().matches(("\\S+"))
                && groups.getSelectionModel().getSelectedIndex() !=-1)
        {
            groups.getSelectionModel().getSelectedItem().addEntry(
                    new Entry(newTitle.getText(), newUsername.getText(), newPassword.getText()));
            tableView.refresh();

            newTitle.clear();
            newUsername.clear();
            newPassword.clear();

        }
    }
    @FXML
    public void removeEntry(){
        if(tableView.getSelectionModel().getSelectedIndex() != -1) {
            remove(tableView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    public void passwordEdit(TableColumn.CellEditEvent<Entry,String> event){
        Entry entry = tableView.getSelectionModel().getSelectedItem();
        if(event.getNewValue().matches(("\\S+"))) {
            entry.setPassword(event.getNewValue());
        }
    }

    @FXML
    public void addCategory(){
        if(newCtg.getText().matches(("\\S+"))) {
            groups.getItems().add(new data.Group(newCtg.getText()));
            groups.refresh();
        }

        newCtg.clear();
    }

    @FXML
    public void removeCategory(){
        if(groups.getSelectionModel().getSelectedIndex() != -1) {
            groups.getItems().remove(groups.getSelectionModel().getSelectedItem());
        }
    }

    public void remove(Entry e){
        Group group = groups.getSelectionModel().getSelectedItem();
        group.getEntries().remove(e);
    }

    public void updateTimestamp() throws IOException {
        reader=new BufferedReader(new FileReader("src/main/java/data/loginInfo.txt"));
        String firstLine=reader.readLine();
        if(firstLine != null){
            timeStamp.setText(firstLine);
        }

        writer = new BufferedWriter(new FileWriter("src/main/java/data/loginInfo.txt"));
        writer.write(App.dateSave());
        writer.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            updateTimestamp();
        } catch (IOException e) {
            e.printStackTrace();
        }

        title.setCellValueFactory(data -> data.getValue().titleProperty());
        username.setCellValueFactory(data -> data.getValue().usernameProperty());
        password.setCellValueFactory(data -> data.getValue().passwordProperty());

        tableView.setEditable(true);
        groups.setEditable(true);

        title.setCellFactory(TextFieldTableCell.forTableColumn());
        username.setCellFactory(TextFieldTableCell.forTableColumn());
        password.setCellFactory(TextFieldTableCell.forTableColumn());

        tableView.prefHeightProperty().bind(stg.widthProperty());
        title.prefWidthProperty().bind(tableView.widthProperty().divide(3));
        username.prefWidthProperty().bind(tableView.widthProperty().divide(3));
        password.prefWidthProperty().bind(tableView.widthProperty().divide(3));
        groups.prefHeightProperty().bind(stg.heightProperty());




        groups.getSelectionModel().selectedItemProperty().addListener((a,oldValue,newValue) -> {
            if(a != null && a.getValue() != null){
                tableView.getItems().clear();
                tableView.getItems().addAll(a.getValue().getEntries());
            }
        });
    }

    @FXML
    public void close(){
        Platform.exit();
    }
}
