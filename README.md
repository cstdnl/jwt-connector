# JWT Extension

![icon](https://github.com/user-attachments/assets/5e55fd3f-8a3a-4ef6-9bdb-98cb0daec190)

---

A Mule 4 custom connector for generating and handling JWT (JSON Web Token) tokens with support for both static and stream keystore configurations.

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
   
## Connector Configuration

Global Configuration - In case the keystore is taken statically from resources, the global configuration is used and uses the following parameters:

![resources/image-31273da1-003b-4e02-9616-eb21cebfd262.png](resources/image-31273da1-003b-4e02-9616-eb21cebfd262.png)

| Parameter| Type       | Required  | Description                                                                   |
|------------------|--------------------|-----------|-------------------------------------------------------------------------------|
| **Type**         | Enum (JKS, PKCS12) | ✅        | The format of the keystore provided via static path configuration.            |
| **Path**         | String             | ✅        | Path to the keystore.                                                         |
| **Alias**        | String             | ✅        | Alias of the private key used to sign JWT.                                    |
| **Key Password** | String             | ✅        | Password to access the keystore.                                              |
| **Password**     | String             | ✅        | Password for the private key.                                                 |
| **Algorithm**    | String             | ❌        | JWT signing algorithm. Default is taken dynamically from the key in keystore. |

## Operations

![resources/image-3cd6d08f-6357-4bce-9fdb-bca2b84f47da.png](resources/image-3cd6d08f-6357-4bce-9fdb-bca2b84f47da.png)

#### 🔹 Generate JWT Token

Description: Generates and signs a JWT token using the provided claims and key from keystore taken from global configuration or input stream.

![resources/image-0eed215c-6a05-4949-bc86-d980a6a93eb5.png](resources/image-0eed215c-6a05-4949-bc86-d980a6a93eb5.png)

#### Parameters

| Parameter                      | Type             | Required  | Description                                                        |
|--------------------------------|------------------|-----------|--------------------------------------------------------------------|
| **Issuer**                     | String           | ❌        | Token issuer identifier                                            |
| **Subject**                    | String           | ❌        | Token subject                                                      |
| **Audiences**                  | String           | ❌        | List of intended token audiences                                   |
| **Expiration Time**            | Number           | ❌        | Token expiration time in milliseconds (default: 300000)            |
| **Not Before**                 | Number           | ❌        | Token validity start time                                          |
| **JWT Id**                     | String           | ❌        | Unique token identifier Custom Parameters                          |
| **Custom Claims**              | Map              | ❌        | Map of additional claims to include in the token                   |
| **Custom Headers**             | Map              | ❌        | Map of additional headers to include in the token                  |
| **Include Certificate**        | Boolean          | ❌        | Boolean to include the certificate in token header (Default false) |
| **Keystore Stream Parameters** | Parameters Group | ❌        | Parameters to handle keystore from input stream                    |

#### Keystore Stream Parameters

![resources/image-00bed2b9-1642-4046-bee0-e0d03346dcba.png](resources/image-00bed2b9-1642-4046-bee0-e0d03346dcba.png)

| Parameter          | Type               | Required  | Description                                                                   |
|--------------------|--------------------|-----------|-------------------------------------------------------------------------------|
| **Type**           | Enum (JKS, PKCS12) | ✅        | The format of the keystore provided via static path configuration.            |
| **Stream Variable**| Stream (Binary)    | ✅        | Variable or payload containing the keystore binary.                           |
| **Alias**          | String             | ✅        | Alias of the private key used to sign JWT.                                    |
| **Key Password**   | String             | ✅        | Password to access the keystore.                                              |
| **Password**       | String             | ✅        | Password for the private key.                                                 |
| **Algorithm**      | String             | ❌        | JWT signing algorithm. Default is taken dynamically from the key in keystore. |

#### Mulesoft Example XML

```
<jwt:generate-token doc:name="Generate JWT Token" doc:id="1aef369d-c1fa-4b4e-915d-294235c3c258"
            includeCertificate="true"
            issuer="#[vars.issuer]"
            audiences='#[["example-aud"]]'
            subject="#[vars.sub]">
            <jwt:custom-claims ><![CDATA[#[{
                "custom-digest": vars.digest
            }]]]></jwt:custom-claims>
            <jwt:keystore-stream-parameters
                keystoreStream="#[vars.cert]"
                keyAlias="#[p('alias')<]"
                keystoreType="PKCS12"
                keystorePassword="#[vars.certificatePassword]"
                keyPassword="#[vars.certificatePassword]"/>
        </jwt:generate-token>
```
