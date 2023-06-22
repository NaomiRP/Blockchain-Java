package blockchain;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Blockchain {

    private final LinkedList<Block> blockchain = new LinkedList<>();

    private int nLeadingZeros = 0;
    private Instant lastBlockAccepted = Instant.now();
    private final List<Message> messageQueue = new ArrayList<>();

    private void adjustGenerationComplexity(long genTime) {
        System.out.println("Block was generating for " + genTime + " seconds");
        if (genTime > 12 && nLeadingZeros > 0) {
            nLeadingZeros--;
            System.out.println("N was decreased to " + nLeadingZeros + "\n");
        } else if (genTime < 5) {
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

    public synchronized List<Message> queuedMessages() {
        return List.copyOf(messageQueue);
    }

    public synchronized boolean queueMessage(Message message) {
        if (message.getId() != messageQueue.size() + 1 || !message.verifySignature())
            return false;
        messageQueue.add(message);
        return true;
    }

    public synchronized int getNextMessageId() {
        return messageQueue.size() + 1;
    }

    private boolean updateMessageQueue(List<Message> messagesToDequeue) {
        int dequeueSize = messagesToDequeue.size();
        if (dequeueSize == 0)
            return true;
        int queueSize = messageQueue.size();
        if (dequeueSize > queueSize)
            return false;
        if (dequeueSize == queueSize) {
            messageQueue.clear();
            return true;
        }
        //dequeueSize < queueSize
        messageQueue.subList(0, dequeueSize).clear();
        return true;
    }

    public synchronized void addBlockToChain(Block block) {
        Instant blockSubmitted = Instant.now();
        if (!(blockValid(block, lastBlock()) && acceptableHash(block) && isAcceptingNewBlocks()))
            return;
        if (!updateMessageQueue(block.getMessages()))
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
