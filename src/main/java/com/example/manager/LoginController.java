package com.example.manager;

import data.Cipher;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static com.example.manager.ManagerController.STEP;

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
        passwordNeeded = Cipher.cipher(passwordNeeded, -STEP);

        if (password.getText().equals(passwordNeeded)) { // domyślnie zaszyfrowanym hasłem jest "Haslo"
            App app = new App();
            app.changeStage();
        }
        else {
            ifBadLogin.setText("niepoprawne dane");
        }
    }

}