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
package io.ashisht.keystore_wiz.keystore_wiz.listener;

import io.ashisht.keystore_wiz.keystore_wiz.events.SceneChangeEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class SceneChangeListener implements ApplicationListener<SceneChangeEvent<Resource>> {
    private final ApplicationContext context;

    SceneChangeListener(ApplicationContext context) {
        this.context = context;
    }


    /**
     * Handles the {@link SceneChangeEvent} by loading a new FXML scene and setting it on the stage.
     *
     * @param sceneChangeEvent The event that triggered this method call, containing the stage to update.
     * @throws RuntimeException if there is an error loading the FXML resource.
     */
    @Override
    public void onApplicationEvent(SceneChangeEvent sceneChangeEvent) {
        try {
            Stage stage = sceneChangeEvent.getStage();
            URL url = sceneChangeEvent.getFxml().getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(context::getBean);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, Color.BLACK);
            stage.setScene(scene);
            stage.setResizable(sceneChangeEvent.getSetResizable());
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
