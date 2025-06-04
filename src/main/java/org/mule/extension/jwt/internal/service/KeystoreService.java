package org.mule.extension.jwt.internal.service;

import org.mule.extension.jwt.internal.config.KeystoreSettings;
import org.mule.extension.jwt.api.KeystoreTypes;

import io.jsonwebtoken.SignatureAlgorithm;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.io.InputStream;
import java.util.Base64;

public class KeystoreService {
    private final KeystoreSettings keystoreSettings;
    private final KeystoreTypes streamKeystoreType;
    private final InputStream streamKeystoreStream;
    private final String streamAlias;
    private final String streamKeystorePassword;
    private final String streamKeyPassword;
    private final String streamAlgorithm;
    
    // Constructor for configuration-based keystore
    public KeystoreService(KeystoreSettings keystoreSettings) {
        this(keystoreSettings, null, null, null, null, null, null);
    }

    // Constructor for stream keystore
    public KeystoreService(KeystoreSettings keystoreSettings,
                            KeystoreTypes streamKeystoreType,   
                            InputStream streamKeystoreStream,
                            String streamAlias,
                            String streamKeystorePassword,
                            String streamKeyPassword,
                            String streamAlgorithm
                        ) {
        this.keystoreSettings = keystoreSettings;
        this.streamKeystoreType = streamKeystoreType;
        this.streamKeystoreStream = streamKeystoreStream;
        this.streamAlias = streamAlias;
        this.streamKeystorePassword = streamKeystorePassword;
        this.streamKeyPassword = streamKeyPassword;
        this.streamAlgorithm = streamAlgorithm;
    }

    public boolean isSigningRequired() {
        return keystoreSettings != null || streamKeystoreType != null;
    }

    private KeystoreTypes getEffectiveKeystoreType() {
        if (streamKeystoreType != null) {
            return streamKeystoreType;
        }
        return keystoreSettings != null ? keystoreSettings.getKeystoreType() : null;
    }

    public KeyStore loadKeystore() throws Exception {
        KeystoreTypes effectiveType = getEffectiveKeystoreType();
        if (effectiveType == null) {
            throw new Exception("No keystore configuration provided");
        }
        
        KeyStore keyStore = KeyStore.getInstance(effectiveType.getValue());
        
        // Use stream keystore if provided, otherwise fall back to configuration
        if (streamKeystoreStream != null) {
            keyStore.load(streamKeystoreStream, 
                         streamKeystorePassword != null ? 
                         streamKeystorePassword.toCharArray() : 
                         keystoreSettings.getKeystorePassword().toCharArray());
        } else if (keystoreSettings != null) {
            try (InputStream keystoreStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(keystoreSettings.getKeystorePath())) {
                
                if (keystoreStream == null) {
                    throw new Exception("Keystore file not found: " + keystoreSettings.getKeystorePath());
                }
                
                keyStore.load(keystoreStream, keystoreSettings.getKeystorePassword().toCharArray());
            }
        } else {
            throw new Exception("Neither stream nor configuration keystore provided");
        }
        return keyStore;
    }

    public SigningCredentials getSigningCredentials() throws Exception {
        KeyStore keyStore = loadKeystore();
        String alias = streamAlias != null ? streamAlias : keystoreSettings.getKeystoreAlias();
        String keyPassword = streamKeyPassword != null ? streamKeyPassword : keystoreSettings.getPassword();
        
        X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword.toCharArray());

        if (cert == null || privateKey == null) {
            throw new Exception("Certificate or private key not found for alias: " + alias);
        }

        SignatureAlgorithm algorithm = determineAlgorithm(privateKey);
        String certB64 = Base64.getEncoder().encodeToString(cert.getEncoded());

        return new SigningCredentials(privateKey, cert, algorithm, certB64);
    }

    private SignatureAlgorithm determineAlgorithm(PrivateKey privateKey) {
        if (privateKey instanceof RSAPrivateKey) {
            return SignatureAlgorithm.RS256;
        } else if (privateKey instanceof ECPrivateKey) {
            return SignatureAlgorithm.ES256;
        }
        throw new IllegalStateException("Unsupported key algorithm");
    }

    public static class SigningCredentials {
        private final PrivateKey privateKey;
        private final X509Certificate certificate;
        private final SignatureAlgorithm algorithm;
        private final String certificateBase64;

        public SigningCredentials(PrivateKey privateKey, X509Certificate certificate, 
                                SignatureAlgorithm algorithm, String certificateBase64) {
            this.privateKey = privateKey;
            this.certificate = certificate;
            this.algorithm = algorithm;
            this.certificateBase64 = certificateBase64;
        }

        public PrivateKey getPrivateKey() { return privateKey; }
        public X509Certificate getCertificate() { return certificate; }
        public SignatureAlgorithm getAlgorithm() { return algorithm; }
        public String getCertificateBase64() { return certificateBase64; }
    }
}