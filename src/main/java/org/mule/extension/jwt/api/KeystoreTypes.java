package org.mule.extension.jwt.api;

public enum KeystoreTypes {
    JKS("JKS"),
    PKCS12("PKCS12");

    private String value;

    KeystoreTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}