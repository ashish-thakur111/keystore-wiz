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

import io.ashisht.keystore_wiz.keystore_wiz.events.SceneChangeEvent;
import io.ashisht.keystore_wiz.keystore_wiz.models.KeystoreEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class ListController {
    private final ApplicationContext context;
    private final Resource detailFxml;

    @FXML
    private Label aliasLabel;

    @FXML
    private Label certName;

    @FXML
    private Label issuedTo;

    @FXML
    private Label issuedBy;

    @FXML
    private Label validFrom;

    @FXML
    private Label validUntil;

    public ListController(ApplicationContext context, @Value("classpath:/detail.fxml") Resource detailFxml) {
        this.context = context;
        this.detailFxml = detailFxml;
    }

    public void addSecondaryStage(KeystoreEntry rowData) {
        this.context.publishEvent(new SceneChangeEvent<>(new Stage(), detailFxml, true));
        this.aliasLabel.setText(rowData.getAlias());
        this.certName.setText(rowData.getAlias());
        this.issuedTo.setText(rowData.getSubject());
        this.issuedBy.setText(rowData.getIssuer());
        this.validFrom.setText(rowData.getStartDate());
        this.validUntil.setText(rowData.getEndDate());
    }
}
