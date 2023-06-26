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
        ExecutorService es = Executors.newFixedThreadPool((minerCount * 2) + personCount);

        for (int i = 0; i < personCount; i++) {
            Person p = new Person(names.get(i));
            es.submit(new TransactionGenerator(blockchain, p));
            blockchain.addParticipant(p);
        }

        for (int i = 1; i <= minerCount; i++) {
            Person miner = new Person("miner" + i);
            es.submit(new TransactionGenerator(blockchain, miner));
            es.submit(new Miner(blockchain, miner));
        }

        while (blockchain.isAcceptingNewBlocks())
            TimeUnit.SECONDS.sleep(1);

        es.shutdownNow();

        if (!blockchain.isValid())
            System.out.println("Invalid blockchain!");
    }
}
