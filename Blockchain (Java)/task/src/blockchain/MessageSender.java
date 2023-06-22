package blockchain;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MessageSender implements Runnable {

    private final Blockchain blockchain;
    private final Person sender;

    public MessageSender(Blockchain blockchain, Person sender) {
        this.blockchain = blockchain;
        this.sender = sender;
    }

    @Override
    public void run() {
        var r = new Random();
        while (blockchain.isAcceptingNewBlocks()) {
            blockchain.queueMessage(new Message(sender, blockchain.getNextMessageId()));
            try {
                TimeUnit.MILLISECONDS.sleep(r.nextInt(100));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
