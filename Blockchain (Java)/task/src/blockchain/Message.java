package blockchain;

import java.util.List;
import java.util.Random;

public class Message {

    private static final List<String> MESSAGES = List.of("Hi", "Hello", "Aloha", "Ciao", "Salve", "Hola", "Kon'nichiwa");

    private final Person sender;
    private String message;

    private final int id;

    private final byte[] signature;

    public Message(Person sender, int id) {
        this.sender = sender;
        this.id = id;
        generateMessage();
        signature = sender.signMessage(messageData());
    }

    public int getId() {
        return id;
    }

    public boolean verifySignature() {
        return SignatureUtil.verify(messageData(), signature, sender.getPublicKey());
    }

    private void generateMessage() {
        var r = new Random();
        int messageIndex = r.nextInt(0, MESSAGES.size());
        message = MESSAGES.get(messageIndex);
    }

    private String messageData() {
        return id + ", " + this;
    }

    @Override
    public String toString() {
        return sender.getName() + ": " + message;
    }
}
