# JWT Extension

A Mule 4 connector for generating and handling JWT (JSON Web Token) tokens with support for both static and stream keystore configurations.

## Overview
This connector provides functionality to generate JWT tokens with:

- Configurable standard JWT claims
- Custom claims support
- Custom headers
- Optional certificate header inclusion
- Flexible keystore configuration (static or stream)
- Support for multiple keystore types (JKS, PKCS12)
  
## Installation
1. Add the following dependency to your application's pom.xml :
```
<dependency>
    <groupId>{{groupId}}</groupId>
    <artifactId>jwt-connector</artifactId>
    <version>{{version}}</version>
    <classifier>mule-plugin</classifier>
</dependency>
```

2. Update your project's dependencies in Anypoint Studio.
   
## Configuration
### Keystore Configuration
Configure the connector with a static keystore in your Mule application's configuration:

```
<jwt:config name="JWT_Config">
    <jwt:keystore-config 
        keystorePath="keystore.jks"
        keystorePassword="${keystore.password}"
        keystoreType="JKS"
        alias="${keystore.alias}"
        password="${key.password}"
        algorithm="${algorithm}"/>
</jwt:config>
```

### Parameters Standard JWT Claims
- issuer (iss) : Token issuer identifier
- subject (sub) : Token subject
- audiences (aud) : List of intended token audiences
- expirationTime (exp) : Token expiration time in milliseconds (default: 300000)
- notBefore (nbf) : Token validity start time
- jwtId (jti) : Unique token identifier Custom Parameters
- customClaims : Map of additional claims to include in the token
- customHeaders : Map of additional headers to include in the token
- includeCertificate : Boolean to include the certificate in token header

####Keystore Configuration
Configuration:
- keystorePath : Path to the keystore file
- keystorePassword : Password to access the keystore
- keystoreType : Type of keystore (JKS or PKCS12)
- alias : Alias of the key in the keystore
- password : Password for the specific key
- algorithm : Signing algorithm (e.g., RS256, ES256 - by default the algorithm will be derived from the key)
  
Dynamic Stream Parameters:
- keystoreType : Type of keystore from stream
- keystoreStream : Input stream (payload, vars.keystore, ...) containing the keystore
- keyAlias : Key alias in the stream keystore
- keystorePassword : Password for the stream keystore
- keyPassword : Password for the key in stream keystore
- algorithm : Signing algorithm for stream configuration (e.g., RS256, ES256 - by default the algorithm will be derived from the key)
  
## Usage Examples
### Basic Token Generation
```
<jwt:generate-token 
    issuer="MyApp"
    subject="user123"
    expirationTime="3600000"/>
```

### Token with Custom Claims
```
<jwt:generate-token>
    <jwt:custom-claims>
        #[{
            'role': 'admin',
            'department': 'IT',
            'employeeId': '12345'
        }]
    </jwt:custom-claims>
</jwt:generate-token>
```

### Using Stream Keystore
```
<jwt:generate-token>
    <jwt:keystore-stream-configuration>
        <jwt:keystore-stream-parameters
            keystoreType="PKCS12"
            keystoreStream="#[vars.keystoreStream]"
            keyAlias="myKey"
            keystorePassword="#[vars.keystorePass]"
            keyPassword="#[vars.keyPass]"
            algorithm="${algorithm}"/>
    </jwt:keystore-stream-configuration>
</jwt:generate-token>
```

## Best Practices
1. Security:
   - Store sensitive information (passwords, keys) in secure configuration properties
   - Use secure keystore types (PKCS12 preferred over JKS)
   - Regularly rotate keys and certificates
     
2. Performance:
   - Reuse connector configurations when possible
   - Consider token expiration times carefully
     
3. Error Handling:
   - Implement proper error handling for keystore loading failures
   - Validate all dynamic parameters before token generation
     
## Support
For issues and feature requests, please create an issue in the project's issue tracker.
