package com.example.gui_po2;

import data.Cipher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    BufferedReader reader;
    @FXML
    private Label ifBadLogin;
    @FXML
    private PasswordField password;

    @FXML
    public void tryLogin() throws IOException {
        reader = new BufferedReader(new FileReader("src/main/java/data/ciphered.txt"));
        String passwordNeeded = reader.readLine();
        passwordNeeded = Cipher.decipher(passwordNeeded);
        if(password.getText().equals(passwordNeeded)){
            App app = new App();
            app.changeStage();
        }
        else {
            //noinspection SpellCheckingInspection
            ifBadLogin.setText("niepoprawne dane");
        }
    }

}