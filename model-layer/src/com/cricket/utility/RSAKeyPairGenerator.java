package com.cricket.utility;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSAKeyPairGenerator {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSAKeyPairGenerator() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        RSAKeyPairGenerator keyPairGenerator = new RSAKeyPairGenerator();
        System.out.println(String.format("Public Key : %s", Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded())));
        System.out.println(String.format("Private Key : %s", Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded())));
    }
}
