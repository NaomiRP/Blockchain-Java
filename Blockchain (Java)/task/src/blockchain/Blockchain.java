package blockchain;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Blockchain {

    private final LinkedList<Block> blockchain = new LinkedList<>();

    private int nLeadingZeros = 0;
    private Instant lastBlockAccepted = Instant.now();
    private final List<Transaction> transactionQueue = new ArrayList<>();
    private final Set<Person> participants = new HashSet<>();
    private final Map<Person, Integer> vcBalance = new HashMap<>();

    private void adjustGenerationComplexity(long genTimeMillis) {
        System.out.println("Block was generating for " + ((int) genTimeMillis / 1000) + " seconds");
        if (genTimeMillis > 2500 && nLeadingZeros > 0) {
            nLeadingZeros--;
            System.out.println("N was decreased to " + nLeadingZeros + "\n");
        } else if (genTimeMillis < 500 && nLeadingZeros < 4) {
            nLeadingZeros++;
            System.out.println("N was increased to " + nLeadingZeros + "\n");
        } else {
            System.out.println("N stays the same\n");
        }
    }

    public synchronized boolean isAcceptingNewBlocks() {
        return blockchain.size() < 15;
    }

    public synchronized Block lastBlock() {
        return blockchain.peekLast();
    }

    public synchronized int requiredZeros() {
        return nLeadingZeros;
    }

    public synchronized void addParticipant(Person participant) {
        participants.add(participant);
    }

    public synchronized Person getRandomParticipant(Person excluded) {
        return participants.stream().filter(person -> !person.equals(excluded)).unordered().toList().get(0);
    }

    public synchronized int getAvailableBalance(Person person) {
        return vcBalance.getOrDefault(person, 0);
    }

    private synchronized void updateBalance(Person person, int amountToAdd) {
        int prevAmount = vcBalance.getOrDefault(person, 0);
        vcBalance.put(person, prevAmount + amountToAdd);
    }

    public synchronized List<Transaction> queuedTransactions() {
        return List.copyOf(transactionQueue);
    }

    public synchronized void queueTransaction(Transaction transaction) {
        if (transaction.getId() != transactionQueue.size() + 1 || !transaction.verifySignature())
            return;
        transactionQueue.add(transaction);
    }

    public synchronized int getNextTransactionId() {
        return transactionQueue.size() + 1;
    }

    private boolean updateTransactionQueue(List<Transaction> messagesToDequeue) {
        int dequeueSize = messagesToDequeue.size();
        if (dequeueSize == 0)
            return true;
        int queueSize = transactionQueue.size();
        if (dequeueSize > queueSize)
            return false;
        if (dequeueSize == queueSize) {
            transactionQueue.clear();
            return true;
        }
        //dequeueSize < queueSize
        transactionQueue.subList(0, dequeueSize).clear();
        return true;
    }

    public synchronized void addBlockToChain(Block block) {
        Instant blockSubmitted = Instant.now();
        if (!(blockValid(block, lastBlock()) && acceptableHash(block) && isAcceptingNewBlocks()))
            return;
        if (!updateTransactionQueue(block.getTransactions()))
            return;
        blockchain.add(block);
        System.out.println(block);
        long genTime = Duration.between(lastBlockAccepted, blockSubmitted).toMillis();
        adjustGenerationComplexity(genTime);
        lastBlockAccepted = Instant.now();

        Person miner = block.getMiner();
        addParticipant(miner);
        updateBalances(block);
    }

    private synchronized void updateBalances(Block block) {
        updateBalance(block.getMiner(), 100);
        for (Transaction transaction : block.getTransactions()) {
            updateBalance(transaction.getSender(), -1 * transaction.getAmount());
            updateBalance(transaction.getRecipient(), transaction.getAmount());
        }
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
        for (Integer balance: vcBalance.values()){
            if (balance < 0)
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
