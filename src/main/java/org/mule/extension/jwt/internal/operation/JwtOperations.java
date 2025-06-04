package org.mule.extension.jwt.internal.operation;

import org.mule.extension.jwt.internal.config.JwtConfiguration;
import org.mule.extension.jwt.api.KeystoreTypes;
import org.mule.extension.jwt.internal.service.KeystoreService;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.*;
import java.io.InputStream;

import org.mule.runtime.extension.api.annotation.execution.Execution;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.Expression;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;

import java.util.HashMap;
import java.util.Map;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */

public class JwtOperations {
    // Remove the class-level parameter
    // @Parameter
    // @DisplayName("Custom Claims")
    // @Summary("Map of custom claims to add to the token")
    // @Optional
    // @Expression(SUPPORTED)
    // private Map<String, Object> customClaims;

    @MediaType(value = ANY, strict = false)
    @DisplayName("Generate JWT Token")
    @Summary("Generates a JWT token with optional signing capabilities")
    public String generateToken(
            @Config JwtConfiguration configuration,
            
            // Standard JWT Claims
            @Optional @DisplayName("Issuer (iss)") String issuer,
            @Optional @DisplayName("Subject (sub)") String subject,
            @Optional @DisplayName("Audiences (aud)") List<String> audiences,
            @Optional(defaultValue = "300000") @DisplayName("Expiration Time (exp)") Long expirationTime,
            @Optional @DisplayName("Not Before (nbf)") Long notBefore,
            @Optional @DisplayName("JWT ID (jti)") String jwtId,
            
            // Custom Claims
            @Optional
            @DisplayName("Custom Claims")
            @Summary("Map of custom claims to add to the token")
            @Content(primary = true)
            @Expression(SUPPORTED)
            Map<String, Object> customClaims,
            
            // Custom Headers
            @Optional @DisplayName("Custom Headers") 
            @Summary("Map of custom headers to add to the token")
            @Content
			@Expression(SUPPORTED)
            Map<String, Object> customHeaders,

            // Include Certificate in Header
            @Optional(defaultValue = "false")
            @DisplayName("Include Certificate")
            @Summary("Whether to include the certificate in the token header")
            boolean includeCertificate,

            @ParameterGroup(name = "Keystore Configuration")
            @DisplayName("Keystore Stream")
            @Summary("Keystore stream configuration")
            KeystoreStreamParametersGroup keystoreStreamParametersGroup
            
    ) {
        try {

            // -------------------
            // Build Claims and Headers
            // -------------------
            
            // Build claims
            Map<String, Object> claims = new HashMap<>();
            
            // Add standard claims if present
            if (issuer != null) claims.put("iss", issuer);
            if (subject != null) claims.put("sub", subject);
            if (audiences != null) claims.put("aud", audiences);
            claims.put("jti", jwtId != null ? jwtId : UUID.randomUUID().toString());
            
            // Add time-based claims
            long now = System.currentTimeMillis();
            claims.put("iat", now);
            claims.put("exp", new Date(now + (expirationTime * 1000)));
            if (notBefore != null) claims.put("nbf", new Date(notBefore * 1000));
            
            // Add custom claims
            if (customClaims != null) {
                claims.putAll(customClaims);
            }

            // Start building the token
            JwtBuilder builder = Jwts.builder();

            // Add custom headers if present
            if (customHeaders != null) {
                customHeaders.forEach(builder::setHeaderParam);
            }

            // -------------------
            // Keystore Operations
            // -------------------

            // KeystoreService keystoreService = new KeystoreService(configuration.getKeystoreSettings());

            KeystoreService keystoreService;
            if (keystoreStreamParametersGroup.getKeystoreStreamParameters() != null) {
                KeystoreStreamParameters params = keystoreStreamParametersGroup.getKeystoreStreamParameters();
                keystoreService = new KeystoreService(
                    configuration.getKeystoreSettings(),
                    params.getKeystoreType(),
                    params.getKeystoreStream(),
                    params.getKeyAlias(),
                    params.getKeystorePassword(),
                    params.getKeyPassword(),
                    params.getAlgorithm()
                );
            } else {
                keystoreService = new KeystoreService(configuration.getKeystoreSettings());
            }

            if (keystoreService.isSigningRequired()) {
                KeystoreService.SigningCredentials credentials = keystoreService.getSigningCredentials();
                
                // Only add certificate to header if requested
                if (includeCertificate) {
                    builder.setHeaderParam("x5c", new String[]{credentials.getCertificateBase64()});
                }
                builder.setHeaderParam("typ", "JWT");

                // Set claims and sign
                builder.setClaims(claims)
                      .signWith(credentials.getPrivateKey(), credentials.getAlgorithm());
            } else {
                // Build Unsigned token
                builder.setHeaderParam("typ", "JWT")
                       .setClaims(claims);
            }

            return builder.compact();

        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT token: " + e.getMessage(), e);
        }
    }
}
