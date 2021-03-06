package com.example.manager;

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


import static com.example.manager.App.stg;

public class ManagerController
        implements Initializable  {

    FileChooser fileChooser = new FileChooser();
    BufferedWriter writer;
    BufferedReader reader;
    String source;
    static final int STEP = 1;


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


    public File chooseFile() {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null)
            file = new File("src/main/java/data/ciphered.txt"); //domyślne zachowanie w razie braku inputu

        return file;
    }

    @FXML
    public void readFromFile() throws IOException {
    // po odszyfrowaniu zapisu z pliku tekstowego dane są dodawane do struktur danych
        File file = chooseFile();
        source = file.getAbsolutePath();

        if(file.getName().contains(".txt")) {

            reader = new BufferedReader(new FileReader(source));
            data.Group currentGroup = null;
            boolean groupNotNull = false;

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {

                String[] elements = line.split(" ");

                if (elements.length == 1) {
                    if(groupNotNull)
                        groups.getItems().add(currentGroup);
                    currentGroup = new data.Group(Cipher.cipher(elements[0], -STEP));
                    groupNotNull = true;
                }
                else {
                     Entry entryTemp = new Entry(
                             Cipher.cipher(elements[0], -STEP),
                             Cipher.cipher(elements[1], -STEP),
                             Cipher.cipher(elements[2], -STEP));
                     currentGroup.addEntry(entryTemp);
                }
            }

            groups.getItems().add(currentGroup);
            reader.close();
            groups.refresh();

        }
    }

    @FXML
    public void changeMode() {
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

        for (Group a : saveGroups) {
            if(a == null) break;
            writer.write(Cipher.cipher(a.getName(), STEP) + '\n');

            for(Entry b : a.getEntries() ){
                StringBuilder tempSave = new StringBuilder();
                tempSave.append(Cipher.cipher(b.getTitle(), STEP)).append(" ");
                tempSave.append(Cipher.cipher(b.getUsername(), STEP)).append(" ");
                tempSave.append(Cipher.cipher(b.getPassword(), STEP));
                writer.write(tempSave.toString() + '\n');
            }

        }
        writer.close();

    }
    @FXML
    public void titleEdit(TableColumn.CellEditEvent<Entry,String> event) {
        Entry entry = tableView.getSelectionModel().getSelectedItem();
        if (event.getNewValue().matches(("\\S+")))
            entry.setTitle(event.getNewValue());
    }
    @FXML
    public void usernameEdit(TableColumn.CellEditEvent<Entry,String> event) {
        Entry entry = tableView.getSelectionModel().getSelectedItem();
        if (event.getNewValue().matches(("\\S+")))
            entry.setUsername(event.getNewValue());
    }
    @FXML
    public void passwordEdit(TableColumn.CellEditEvent<Entry,String> event) {
        Entry entry = tableView.getSelectionModel().getSelectedItem();
        if (event.getNewValue().matches(("\\S+")))
            entry.setPassword(event.getNewValue());
    }
    @FXML
    public void addNewEntry() {
        if (
                newTitle.getText().matches(("\\S+"))
                && newUsername.getText().matches(("\\S+"))
                && newPassword.getText().matches(("\\S+"))
                && groups.getSelectionModel().getSelectedIndex() != -1
        )
        {
            Entry data = new Entry(newTitle.getText(), newUsername.getText(), newPassword.getText());
            groups.getSelectionModel().getSelectedItem().addEntry(data);
            tableView.getItems().add(data); //poprawka wizualna

            newTitle.clear();
            newUsername.clear();
            newPassword.clear();

        }
    }
    @FXML
    public void removeEntry() {
        if(tableView.getSelectionModel().getSelectedIndex() != -1) {
            Entry toDel = tableView.getSelectionModel().getSelectedItem();
            tableView.getItems().remove(toDel); //poprawka wizualna
            remove(toDel);
        }
    }

    @FXML
    public void addCategory() {
        if(newCtg.getText().matches(("\\S+"))) {
            groups.getItems().add(new data.Group(newCtg.getText()));
            groups.refresh();
        }
        newCtg.clear();
    }

    @FXML
    public void removeCategory() {
        if(groups.getSelectionModel().getSelectedIndex() != -1) {
            groups.getItems().remove(groups.getSelectionModel().getSelectedItem());
        }
    }

    public void remove(Entry e) {
        Group group = groups.getSelectionModel().getSelectedItem();
        group.getEntries().remove(e);
    }

    public void updateTimestamp() throws IOException {
        reader = new BufferedReader(new FileReader("src/main/java/data/loginInfo.txt"));
        String firstLine = reader.readLine();
        if (firstLine != null)
            timeStamp.setText(firstLine);

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
            if(a != null && a.getValue() != null) {
                tableView.getItems().clear();
                tableView.getItems().addAll(a.getValue().getEntries());
            }
        });
    }

    @FXML
    public void close() {
        Platform.exit();
    }
}
