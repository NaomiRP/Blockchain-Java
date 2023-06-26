package blockchain;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TransactionGenerator implements Runnable {

    private final Blockchain blockchain;
    private final Person sender;

    public TransactionGenerator(Blockchain blockchain, Person sender) {
        this.blockchain = blockchain;
        this.sender = sender;
    }

    @Override
    public void run() {
        var r = new Random();
        while (blockchain.isAcceptingNewBlocks()) {
            blockchain.queueTransaction(new Transaction(sender, blockchain.getAvailableBalance(sender), blockchain.getRandomParticipant(sender), blockchain.getNextTransactionId()));
            try {
                TimeUnit.MILLISECONDS.sleep(r.nextInt(10));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
