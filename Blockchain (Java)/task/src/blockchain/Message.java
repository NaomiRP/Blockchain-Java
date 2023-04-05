package blockchain;

import java.util.List;
import java.util.Random;

public class Message {

    private static final List<String> MESSAGES = List.of("Hi", "Hello", "Aloha", "Ciao", "Salve", "Hola", "Kon'nichiwa");

    private final Person sender;
    private String message;

    public Message(Person sender) {
        this.sender = sender;
        generateMessage();
    }

    private void generateMessage() {
        var r = new Random();
        int messageIndex = r.nextInt(0, MESSAGES.size());
        message = MESSAGES.get(messageIndex);
    }

    @Override
    public String toString() {
        return sender.getName() + ": " + message;
    }
}
