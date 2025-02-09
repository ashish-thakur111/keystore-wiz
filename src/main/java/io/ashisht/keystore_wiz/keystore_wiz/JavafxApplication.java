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
package io.ashisht.keystore_wiz.keystore_wiz;

import io.ashisht.keystore_wiz.keystore_wiz.events.StageReadyEvent;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavafxApplication extends Application {
    private ConfigurableApplicationContext context;

    /**
     * {@inheritDoc}
     *
     * <p>This implementation registers the following beans into the application context:
     * <ul>
     *     <li>{@link Application} bean with the value of the current {@link JavafxApplication} instance.
     *     <li>{@link Parameters} bean with the value of {@link #getParameters()}.
     *     <li>{@link HostServices} bean with the value of {@link #getHostServices()}.
     * </ul>
     *
     * <p>It also runs the Spring Boot application with the provided command line arguments.
     */
    @Override
    public void init() throws Exception {
        ApplicationContextInitializer<GenericApplicationContext> initializer = applicationContext -> {
            applicationContext.registerBean(Application.class, () -> JavafxApplication.this);
            applicationContext.registerBean(Parameters.class, this::getParameters);
            applicationContext.registerBean(HostServices.class, this::getHostServices);
        };

        this.context = new SpringApplicationBuilder()
                .sources(KeystoreWizApplication.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation sends a {@link StageReadyEvent} to the Spring application context.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.context.publishEvent(new StageReadyEvent(primaryStage));
    }

    /**
     * {@inheritDoc}
     *
     * <p>This implementation stops the Spring application context and then calls {@link Platform#exit()}.
     */
    @Override
    public void stop() throws Exception {
        this.context.close();
        Platform.exit();
    }
}
