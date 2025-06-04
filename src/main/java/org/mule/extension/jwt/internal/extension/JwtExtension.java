package org.mule.extension.jwt.internal.extension;

import org.mule.extension.jwt.internal.config.JwtConfiguration;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;
import org.mule.sdk.api.annotation.JavaVersionSupport;
import org.mule.sdk.api.meta.JavaVersion;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */

@Xml(prefix = "jwt")
@Extension(name = "JWT Connector")
@JavaVersionSupport({JavaVersion.JAVA_17, JavaVersion.JAVA_11, JavaVersion.JAVA_8})
@Configurations(JwtConfiguration.class)
public class JwtExtension {

}
