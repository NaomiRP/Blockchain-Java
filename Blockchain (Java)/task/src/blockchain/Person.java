package blockchain;

import java.security.KeyPair;
import java.security.PublicKey;

public class Person {

    private final String name;
    private final KeyPair keyPair;

    public Person(String name) {
        this.name = name;
        keyPair = SignatureUtil.generateKeyPair();
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    byte[] signMessage(String messageData) {
        return SignatureUtil.sign(messageData, keyPair.getPrivate());
    }
}
