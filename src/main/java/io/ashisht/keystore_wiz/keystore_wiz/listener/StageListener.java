/**
 * Copyright ©️ 2025 Ashish Thakur <ashish.thakur1110@gmail.com>
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
package io.ashisht.keystore_wiz.keystore_wiz.listener;

import io.ashisht.keystore_wiz.keystore_wiz.events.StageReadyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Slf4j
@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {
    private final String applicationTitle;
    private final Resource fxml;
    private final ApplicationContext context;

    StageListener(@Value("${spring.application.ui.title}") String applicationTitle, @Value("classpath:/keystore_load.fxml") Resource fxml,
                  ApplicationContext context) {
        this.applicationTitle = applicationTitle;
        this.fxml = fxml;
        this.context = context;
    }

    /**
     * This method is called by the Spring context when the application is
     * started and the {@link StageReadyEvent} is published.
     *
     * @param stageReadyEvent The event that triggered this method call.
     */
    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        try {
            Stage stage = stageReadyEvent.getStage();
            URL url = fxml.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(context::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, Color.BLACK);
            stage.setScene(scene);
            stage.setTitle(applicationTitle);
            stage.setOnCloseRequest(event -> {
                event.consume();
                closeAppWindow(stage);
            });
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void closeAppWindow(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Program");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("All unsaved data will be lost!");
        Optional<ButtonType> alertWin = alert.showAndWait();
        if (alertWin.isPresent()) {
            if (alertWin.get() == ButtonType.OK){
                log.atInfo().log("Application closed by user.");
                stage.close();
            }
        }
    }
}
