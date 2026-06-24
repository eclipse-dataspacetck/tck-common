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

import org.gradle.api.provider.Property;

public abstract class TckGeneratorExtension {
    private String imageFormat = "svg";
    private boolean forceConversion = true;

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public void setForceConversion(boolean forceConversion) {
        this.forceConversion = forceConversion;
    }

    /**
     * SemVer string that indicates the version of the Test Plan Generator module (org.eclipse.dataspacetck:test-plan-generator)
     * to use. This should typically be the same version as the other TCK libraries
     * <p>
     * The default is the same version as the plugin
     */
    public abstract Property<String> getGeneratorVersion();

    /**
     * Whether Mermaid/PlantUML diagrams should be converted - and embedded as - images in the Test Plan document.
     * Note that this may involve remote calls to third-party services!
     * <p>
     * The default is {@code true}
     */
    public boolean forceConversion() {
        return forceConversion;
    }

    /**
     * The image format for conversion. Can be "svg" or "png" and is only relevant if {@code forceConversion} is set to {@code true}
     * <p>
     * The default is "svg"
     */
    public String getImageFormat() {
        return imageFormat;
    }

    /**
     * Output directory of the generated Test Plan.
     * <p>
     * The default is {@code {rootProject}/build}
     */
    public abstract Property<String> getOutputDirectory();

}
