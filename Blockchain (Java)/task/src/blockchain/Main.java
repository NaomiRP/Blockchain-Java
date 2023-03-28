package blockchain;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Blockchain blockchain = new Blockchain();

        int threadCount = Math.min(Runtime.getRuntime().availableProcessors(), 10);
        ExecutorService es = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            es.submit(new Miner(blockchain, i));
        }

        while (blockchain.isAcceptingNewBlocks())
            TimeUnit.SECONDS.sleep(8);

        es.shutdownNow();

        if (!blockchain.isValid())
            System.out.println("Invalid blockchain!");
    }
}
