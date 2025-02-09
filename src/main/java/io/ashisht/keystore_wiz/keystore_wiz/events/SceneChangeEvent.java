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
package io.ashisht.keystore_wiz.keystore_wiz.events;

import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;

@Getter
public class SceneChangeEvent<R extends Resource> extends ApplicationEvent {
    private final R fxml;
    private final Boolean setResizable;

    public Stage getStage() {
        return (Stage) this.source;
    }
    public SceneChangeEvent(Stage source, R fxml, Boolean setResizable) {
        super(source);
        this.fxml = fxml;
        this.setResizable = setResizable;
    }
}
