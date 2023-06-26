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

    byte[] signTransaction(String transactionData) {
        return SignatureUtil.sign(transactionData, keyPair.getPrivate());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!name.equals(person.name)) return false;
        return keyPair.equals(person.keyPair);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + keyPair.hashCode();
        return result;
    }
}
