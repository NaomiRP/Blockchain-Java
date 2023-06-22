package blockchain;

import java.security.*;

public class SignatureUtil {

    private static final KeyPairGenerator keyGen;

    private static final String signatureType = "SHA256withRSA";

    static {
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyGen.initialize(1024);
    }

    public static KeyPair generateKeyPair() {
        return keyGen.generateKeyPair();
    }

    public static byte[] sign(String data, PrivateKey privateKey) {
        try {
            Signature rsa = Signature.getInstance(signatureType);
            rsa.initSign(privateKey);
            rsa.update(data.getBytes());
            return rsa.sign();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String data, byte[] signature, PublicKey publicKey) {
        try {
            Signature rsa = Signature.getInstance(signatureType);
            rsa.initVerify(publicKey);
            rsa.update(data.getBytes());
            return rsa.verify(signature);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
