package org.mule.extension.jwt.internal.operation;

import org.mule.extension.jwt.api.KeystoreTypes;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Content;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;

import java.io.InputStream;

public class KeystoreStreamParameters {

    @Parameter
    @Optional
    @DisplayName("Type")
    @Summary("Keystore type from stream payload or variable")
    public KeystoreTypes keystoreType;

    @Parameter
    @Optional
    @DisplayName("Stream Variable")
    @Expression(SUPPORTED)
    @Content
    private InputStream keystoreStream;

    @Parameter
    @Optional 
    @DisplayName("Alias")
    @Expression(SUPPORTED)
    public String keyAlias;

    @Parameter
    @Optional 
    @DisplayName("Keystore Password")
    @Expression(SUPPORTED)
    @Password
    public String keystorePassword;

    @Parameter
    @Optional
    @DisplayName("Key Password")
    @Expression(SUPPORTED)
    @Password
    public String keyPassword;

    @Parameter
    @Optional
    @DisplayName("Algorithm")
    @Expression(SUPPORTED)
    private String algorithm;

    public KeystoreTypes getKeystoreType() {
        return keystoreType;
    }

    public InputStream getKeystoreStream() {
        return keystoreStream;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    } 

    public String getKeyPassword() {
        return keyPassword;
    }

    public String getAlgorithm() {
        return algorithm;
    }

}
