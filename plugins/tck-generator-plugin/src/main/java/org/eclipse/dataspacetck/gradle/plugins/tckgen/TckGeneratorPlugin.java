/*
 *  Copyright (c) 2024 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.dataspacetck.gradle.plugins.tckgen;


import org.eclipse.dataspacetck.gradle.tasks.GenerateTestPlanTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.DependencyResolutionListener;
import org.gradle.api.artifacts.ResolvableDependencies;
import org.jetbrains.annotations.NotNull;

import static java.util.Optional.ofNullable;

/**
 * Gradle plugin that adds a task to generate a test plan
 */
public class TckGeneratorPlugin implements Plugin<Project> {

    private static final String GENERATOR_GROUP_ARTIFACT = "org.eclipse.dataspacetck.common:test-plan-generator";

    @Override
    public void apply(@NotNull Project target) {
        var extension = target.getExtensions().create("generatorExtension", TckGeneratorExtension.class);

        target.getGradle().addListener(new DependencyInjector(target, extension));

        target.getTasks()
                .register(GenerateTestPlanTask.NAME, GenerateTestPlanTask.class)
                .configure(generateTestPlanTask -> {
                    generateTestPlanTask.setGroup("documentation");
                    // normally one would use getOrDefault, but the wildcard map prevents that
                    generateTestPlanTask.setImageFormat(extension.getImageFormat());

                    generateTestPlanTask.setForceConversion(extension.forceConversion());

                    if (extension.getOutputDirectory().isPresent()) {
                        generateTestPlanTask.setOutputDirectory(extension.getOutputDirectory().get());
                    }
                });
    }


    /**
     * callback that is invoked before dependency resolution begins. We need this hook to add the "annotationProcessor" version
     */
    private record DependencyInjector(Project target, TckGeneratorExtension extension) implements DependencyResolutionListener {

        @Override
        public void beforeResolve(@NotNull ResolvableDependencies dependencies) {
            // register the annotation processor dependency. This has to happen _after_ the configuration phase is
            // complete, e.g. before dependency resolution
            ofNullable(target.getConfigurations().findByName("annotationProcessor"))
                    .ifPresent(c -> c.getDependencies().add(target.getDependencies().create(GENERATOR_GROUP_ARTIFACT + ":" + getProcessorVersion(extension, target))));
            target.getGradle().removeListener(this);
        }

        @Override
        public void afterResolve(@NotNull ResolvableDependencies dependencies) {
            //noop
        }

        private @NotNull String getProcessorVersion(TckGeneratorExtension extension, Project project) {
            var processorVersion = extension.getGeneratorVersion();

            if (processorVersion.isPresent()) {
                var version = processorVersion.get();
                project.getLogger().debug("{}: use configured version from AutodocExtension (override) [{}]", project.getName(), version);
                return version;
            } else {
                var version = project.getVersion().toString();
                project.getLogger().info("No explicit configuration value for the annotationProcessor version was found. Project version {} will be used", version);
                return version;
            }
        }
    }
}
