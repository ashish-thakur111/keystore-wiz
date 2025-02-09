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
import io.ashisht.keystore_wiz.keystore_wiz.models.KeystoreEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.Optional;

@Component
public class KeystoreDetailsController {
    @FXML
    public Label keystorePathLabel;

    @FXML
    private TableView<KeystoreEntry> tableView;

    @FXML
    private TableColumn<KeystoreEntry, String> aliasColumn;

    @FXML
    private TableColumn<KeystoreEntry, String> issuerColumn;

    @FXML
    private TableColumn<KeystoreEntry, String> subjectColumn;

    @FXML
    private TableColumn<KeystoreEntry, String> validFromColumn;

    @FXML
    private TableColumn<KeystoreEntry, String> validUntilColumn;

    @FXML
    private TableColumn<KeystoreEntry, Void> actionsColumn;

    @FXML
    private Button exportToFile;

    @FXML
    private Button addCert;

    private final KeystoreManager keystoreManager;
    private final ListController listController;

    public KeystoreDetailsController(KeystoreManager keystoreManager, ListController listController) {
        this.keystoreManager = keystoreManager;
        this.listController = listController;
    }

    public void initialize() throws KeyStoreException {
        List<KeystoreEntry> keystoreEntries = keystoreManager.getKeystoreEntries();
        ObservableList<KeystoreEntry> data = FXCollections.observableArrayList(
                keystoreEntries
        );

        // Configure the columns
        aliasColumn.setCellValueFactory(new PropertyValueFactory<>("alias"));
        issuerColumn.setCellValueFactory(new PropertyValueFactory<>("issuer"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        validFromColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        validUntilColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        tableView.setItems(data);
        configureActionsColumn();

        // Add action handler for exportToFile button
        exportToFile.setOnAction(event -> exportKeystoreToFile());

        // Add action handler for addCert button
        addCert.setOnAction(event -> addCertificate());
    }

    public void setKeystoreFilePath(String filePath) {
        keystorePathLabel.setText(filePath);
    }

    private void configureActionsColumn() {
        actionsColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<KeystoreEntry, Void> call(TableColumn<KeystoreEntry, Void> param) {
                return new TableCell<>() {
                    private final Button detailsButton = new Button("Details");
                    private final Button removeButton = new Button("Remove");
                    private final Button testButton = new Button("Test");
                    private final HBox buttonBox = new HBox(10, detailsButton, removeButton, testButton);

                    {
                        // Action for details Button
                        detailsButton.setOnAction(event -> {
                            KeystoreEntry rowData = getTableView().getItems().get(getIndex());
                            listController.addSecondaryStage(rowData);
                        });

                        // Action for Delete Button
                        removeButton.setOnAction(event -> {
                            KeystoreEntry rowData = getTableView().getItems().get(getIndex());
                            showDeleteConfirmationDialog(rowData);
                        });

                        // Action for Test Button
                        testButton.setOnAction(event -> {
                            KeystoreEntry rowData = getTableView().getItems().get(getIndex());
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        });
    }

    private void showDeleteConfirmationDialog(KeystoreEntry entry) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this keystore entry?");
        alert.setContentText("Alias: " + entry.getAlias());

        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            try {
                keystoreManager.deleteKeystoreEntry(entry.getAlias());
                tableView.getItems().remove(entry);
            } catch (KeyStoreException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void exportKeystoreToFile() {
        // Prompt the user for the keystore password
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Keystore Password");
        passwordDialog.setHeaderText("Enter the keystore password:");
        passwordDialog.setContentText("Password:");

        Optional<String> passwordResult = passwordDialog.showAndWait();
        if (passwordResult.isPresent()) {
            String password = passwordResult.get();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Keystore");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Keystore Files", "*.p12"));
            File file = fileChooser.showSaveDialog(exportToFile.getScene().getWindow());

            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    keystoreManager.getKeystore().store(fos, password.toCharArray());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Export Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Keystore exported successfully to " + file.getAbsolutePath());
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Export Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to export keystore: " + e.getMessage());
                    alert.showAndWait();
                }
            }
        }
    }

    private void addCertificate() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Certificate");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Certificate Files", "*.cer", "*.crt", "*.pem"));
        File file = fileChooser.showOpenDialog(addCert.getScene().getWindow());

        if (file != null) {
            try (FileInputStream fis = new FileInputStream(file)) {
                CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                Certificate cert = certFactory.generateCertificate(fis);

                TextInputDialog aliasDialog = new TextInputDialog();
                aliasDialog.setTitle("Certificate Alias");
                aliasDialog.setHeaderText("Enter the alias for the certificate:");
                aliasDialog.setContentText("Alias:");

                Optional<String> aliasResult = aliasDialog.showAndWait();
                if (aliasResult.isPresent()) {
                    String alias = aliasResult.get();
                    keystoreManager.getKeystore().setCertificateEntry(alias, cert);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Add Certificate");
                    alert.setHeaderText(null);
                    alert.setContentText("Certificate added successfully.");
                    alert.showAndWait();

                    // Refresh the table view
                    tableView.getItems().setAll(keystoreManager.getKeystoreEntries());
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Certificate Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to add certificate: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
