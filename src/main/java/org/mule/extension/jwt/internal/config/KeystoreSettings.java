package org.mule.extension.jwt.internal.config;

import org.mule.extension.jwt.api.KeystoreTypes;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Password;

public class KeystoreSettings {

    @Parameter
    @Optional
    @DisplayName("Type")
    private KeystoreTypes keystoreType;

    @Parameter
    @Optional
    @DisplayName("Path")
    private String keystorePath;
    
    @Parameter
    @Optional
    @DisplayName("Alias")
    private String keystoreAlias;

    @Parameter
    @Optional
    @Password
    @DisplayName("Key Password")
    private String keystorePassword;

    @Parameter
    @Optional
    @Password
    @DisplayName("Password")
    private String password;

    @Parameter
    @Optional
    @DisplayName("Algorithm")
    private String algorithm;

    public KeystoreTypes getKeystoreType() {
        return keystoreType;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public String getKeystoreAlias() {
        return keystoreAlias;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getPassword() {
        return password;
    }

    public String getAlgorithm() {
        return algorithm;
    }
    
}

