/*
 *  Copyright (c) 2026 Think-it GmbH
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Think-it GmbH - initial API and implementation
 *
 */

package org.eclipse.dataspacetck.runtime.filter;

import org.junit.platform.launcher.PostDiscoveryFilter;

import java.util.function.Predicate;

import static org.junit.platform.engine.FilterResult.includedIf;

public class DisplayNameFilter {

    /**
     * Includes tests which display name matched the passing predicate.
     *
     * @param predicate the predicate
     * @return the filter.
     */
    public static PostDiscoveryFilter includeName(Predicate<String> predicate) {
        return descriptor -> includedIf(
                predicate.test(descriptor.getDisplayName()),
                () -> "Matches display name",
                () -> "Does not match display name"
        );
    }

}
