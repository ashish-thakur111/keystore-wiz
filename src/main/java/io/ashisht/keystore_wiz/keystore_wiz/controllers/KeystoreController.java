/**
 * Copyright 2025 Ashish Thakur(ashish.thakur1110@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.ashisht.keystore_wiz.keystore_wiz.controllers;

import io.ashisht.keystore_wiz.keystore_wiz.core.KeystoreManager;
import io.ashisht.keystore_wiz.keystore_wiz.events.SceneChangeEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javafx.scene.control.Label;

import java.io.File;

@Slf4j
@Component
public class KeystoreController {

    @FXML
    private TextField keystorePathField;

    @FXML
    private PasswordField keystorePasswordField;

    @FXML
    private Label statusLabel;

    private final KeystoreManager keystoreManager;
    private final KeystoreDetailsController keystoreDetailsController;
    private final ApplicationContext context;
    private final Resource fxmlKeystoreDetails;

    public KeystoreController(KeystoreManager keystoreManager, KeystoreDetailsController sceneService,
                              ApplicationContext context, @Value("classpath:/keystore_details.fxml") Resource fxmlKeystoreDetails) {
        this.keystoreManager = keystoreManager;
        this.keystoreDetailsController = sceneService;
        this.context = context;
        this.fxmlKeystoreDetails = fxmlKeystoreDetails;
    }


    /**
     * Displays a file chooser to select a keystore file and populates the path field when a file is selected.
     */
    @FXML
    public void onBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Keystore File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Keystore Files", "*.jks", "*.p12", "*"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            keystorePathField.setText(file.getAbsolutePath());
            statusLabel.setText(""); // Clear previous status
        }
    }

    /**
     * Loads the keystore based on the selected file and password.
     */
    @FXML
    public void onLoadKeystore(ActionEvent actionEvent) {
        String filePath = keystorePathField.getText();
        String password = keystorePasswordField.getText();

        if (filePath.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please provide both the file and password.");
            return;
        }

        try {
            keystoreManager.loadKeystore(filePath, password);
            if (keystoreManager.isKeystoreLoaded()) {
                this.context.publishEvent(new SceneChangeEvent<>((Stage) keystorePathField.getScene().getWindow(), fxmlKeystoreDetails, false));
                keystoreDetailsController.setKeystoreFilePath(filePath);
            }
        } catch (Exception e) {
            log.atError().log("Failed to load keystore: " + e.getMessage());
            statusLabel.setText("Failed to load keystore: " + e.getMessage());
            statusLabel.setTextFill(javafx.scene.paint.Color.RED);
        }
    }
}
