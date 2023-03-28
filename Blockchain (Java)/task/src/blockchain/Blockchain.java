package blockchain;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

public class Blockchain {

    private final LinkedList<Block> blockchain = new LinkedList<>();

    private int nLeadingZeros = 0;

    private Instant lastBlockAccepted = Instant.now();

    private void adjustGenerationComplexity(long genTime) {
        System.out.println("Block was generating for " + genTime + " seconds");
        if (genTime > 60 && nLeadingZeros > 0) {
            nLeadingZeros--;
            System.out.println("N was decreased to " + nLeadingZeros + "\n");
        } else if (genTime < 15) {
            nLeadingZeros++;
            System.out.println("N was increased to " + nLeadingZeros + "\n");
        } else {
            System.out.println("N stays the same\n");
        }
    }

    public synchronized boolean isAcceptingNewBlocks() {
        return blockchain.size() < 5;
    }

    public synchronized Block lastBlock() {
        return blockchain.peekLast();
    }

    public synchronized int requiredZeros() {
        return nLeadingZeros;
    }

    public synchronized void addBlockToChain(Block block) {
        Instant blockSubmitted = Instant.now();
        Block prev = blockchain.peekLast();
        if (!blockValid(block, prev) || !acceptableHash(block) || !isAcceptingNewBlocks())
            return;
        blockchain.add(block);
        System.out.println(block);
        long genTime = Duration.between(lastBlockAccepted, blockSubmitted).getSeconds();
        adjustGenerationComplexity(genTime);
        lastBlockAccepted = Instant.now();
    }

    public synchronized boolean isValid() {
        Block cur = blockchain.get(0);
        if (!firstBlockValid(cur))
            return false;
        Block prev;
        for (int i = 1; i < blockchain.size(); i++) {
            prev = cur;
            cur = blockchain.get(i);
            if (!blockValid(cur, prev))
                return false;
        }
        return true;
    }

    private boolean blockValid(Block block, Block prevBlock) {
        if (prevBlock == null)
            return firstBlockValid(block);
        return block.getId() == (prevBlock.getId() + 1) && block.isValidHash()
                && block.getPreviousHash().equals(prevBlock.getHash());
    }

    private boolean firstBlockValid(Block initialBlock) {
            return initialBlock.getId() == 1 && initialBlock.isValidHash()
                    && "0".equals(initialBlock.getPreviousHash());
    }

    private boolean acceptableHash(Block block) {
        return block.getHash().startsWith("0".repeat(nLeadingZeros));
    }
}
