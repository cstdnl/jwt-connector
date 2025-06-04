package org.mule.extension.jwt.internal.config;

import org.mule.extension.jwt.internal.operation.JwtOperations;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */

@Operations(JwtOperations.class)
public class JwtConfiguration {

    @Parameter
    @Optional
    @DisplayName("Key Store Settings")
    private KeystoreSettings keystoreSettings;

    public KeystoreSettings getKeystoreSettings() {
        return keystoreSettings;
    }
    
}


