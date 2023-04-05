package blockchain;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final List<String> names = List.of("Alice", "Bob", "Cindy", "David", "Elizabeth");

    public static void main(String[] args) throws InterruptedException {

        Blockchain blockchain = new Blockchain();

        int minerCount = Math.min(Runtime.getRuntime().availableProcessors(), 5);
        if (minerCount < 2) minerCount = 2;
        int personCount = names.size();
        ExecutorService es = Executors.newFixedThreadPool(minerCount + personCount);

        for (int i = 0; i < personCount; i++) {
            es.submit(new MessageSender(blockchain, new Person(names.get(i))));
        }

        for (int i = 0; i < minerCount; i++) {
            es.submit(new Miner(blockchain, i));
        }

        while (blockchain.isAcceptingNewBlocks())
            TimeUnit.SECONDS.sleep(2);

        es.shutdownNow();

        if (!blockchain.isValid())
            System.out.println("Invalid blockchain!");
    }
}
