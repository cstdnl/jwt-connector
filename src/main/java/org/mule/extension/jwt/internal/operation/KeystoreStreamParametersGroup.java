package org.mule.extension.jwt.internal.operation;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class KeystoreStreamParametersGroup {

    @Parameter
    @Optional
    @DisplayName("Keystore Stream Parameters")
    @Summary("Parameters for keystore loading from stream")
    private KeystoreStreamParameters keystoreStreamParameters;

    public KeystoreStreamParameters getKeystoreStreamParameters() {
        return keystoreStreamParameters;
    }
}