package com.cricket.utility;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtil {

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNokj65NYc9LdYZshBi6I1BUVu8NdhcafSkzSugFVwUydw7t2DPaZcewxkko3G2R/0OS8s7ceSV/p4zljtgCNtls5A6TT2Ehsoxhqh6PHRRuK4gvhPn8gYtBXjQHkj0VWkr9VoPdEt3NQIr0MkBmwAgt5YkTCV1EZPOAnsLSnQrwIDAQAB";
    private static String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI2iSPrk1hz0t1hmyEGLojUFRW7w12Fxp9KTNK6AVXBTJ3Du3YM9plx7DGSSjcbZH/Q5Lyztx5JX+njOWO2AI22WzkDpNPYSGyjGGqHo8dFG4riC+E+fyBi0FeNAeSPRVaSv1Wg90S3c1AivQyQGbACC3liRMJXURk84CewtKdCvAgMBAAECgYBP1q5K99oWgoAjzfSOtjaeqrUEH7FZlabjleV5hJAk/ErcZ1no1ETGFvraap6O++/+TIniSrtYLE3d8XvBjZzqH95pVIuzqJG0KBBYyIrQNQlDsLTsmHJvRYCa5RXDSwdC+sCy/u9pVSDlTeCDiHGnlZ4MjdJJgeBFCS0gx5u80QJBAMaDdhgviOYkoT71uK9g3E5wxp/RWMwCgZKhEu6Ci1TRp9ldJL3blNGwM3/fhK+tqXmSAONb6Kzy4XKI+/ugEskCQQC2piE+IJ+qckXoAKgNhIGUdi5FoZ9xRj1/L9V1B4Wjq74f9dm3FqEc3UI+oOs0Tx6rSzuJ8YkuU4pPuGAUn8u3AkEAoQOeNQ3RR4KseynJjA7Pgpw8rwKZuJfLy0f02BpwsvJ+53KCS7CgK9h0XSLKN8hvLxk6uS8iMa2rKUBsynLS6QJBAKkZR5+gTpJcN7JioTPaoKlVfOOEjfwuXpMZMxOlQinUwDIQyF9EuSbjBgi7rOaB9c/62AWPNEVKnLvP5ExT4SMCQFQT64R/2dWxRvqyuGIVybRej9PMUFF1TKYI9sAk4NjTzE3lqjqyVkU2N33K7Hk1S0Hs6QyUaYto9U8nvFpXUNI=";

    public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(Base64.getDecoder().decode(data.getBytes()), getPrivateKey(base64PrivateKey));
    }
    
    public static String decrypt(String checksum) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return decrypt(checksum, privateKey);
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        try {
            //String encryptedString = Base64.getEncoder().encodeToString(encrypt("38", publicKey));
        	String encryptedString = Base64.getEncoder().encodeToString(encrypt("38", publicKey));
            System.out.println(encryptedString);
            String decryptedString = RSAUtil.decrypt(encryptedString, privateKey);
            System.out.println(decryptedString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        }

    }
}
