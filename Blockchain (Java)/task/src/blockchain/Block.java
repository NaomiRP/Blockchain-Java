package blockchain;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static blockchain.StringUtil.applySha256;

public class Block {

    private final Person miner;
    private final int id;
    private final long timeStamp;
    private int magic;
    private final String previousHash;
    private final List<Transaction> transactions;
    private String hash;

    public Block(Block previousBlock, int minLeadingZeros, Person miner, List<Transaction> transactions) {
        timeStamp = new Date().getTime();
        this.miner = miner;
        this.transactions = transactions;
        id = previousBlock == null ? 1 : previousBlock.id + 1;
        previousHash = previousBlock == null ? "0" : previousBlock.hash;
        setHash(minLeadingZeros);
    }

    private void setHash(int minLeadingZeros) {
        Random r = new Random();
        do {
            magic = r.nextInt();
            hash = applySha256(stringify());
        } while (!hash.startsWith("0".repeat(minLeadingZeros)));
    }

    public Person getMiner() {
        return miner;
    }

    public int getId() {
        return id;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getHash() {
        return hash;
    }

    public boolean isValidHash() {
        return applySha256(stringify()).equals(hash);
    }

    private String stringify() {
        return id + timeStamp + miner.getName() + magic + previousHash + stringifyTransactions(";");
    }

    private String stringifyTransactions(String delimiter) {
        List<String> messageStrings = transactions.stream().map(Transaction::toString).toList();
        return String.join(delimiter, messageStrings);
    }

    @Override
    public String toString() {
        return "Block:" +
               "\nCreated by " + miner +
               "\n" + miner + " gets 100 VC" +
               "\nId: " + id +
               "\nTimestamp: " + timeStamp +
               "\nMagic number: " + magic +
               "\nHash of the previous block:\n" + previousHash +
               "\nHash of the block:\n" + hash +
               "\nBlock data: " + (transactions.size() == 0 ? "no messages" : "\n" + stringifyTransactions("\n"));
    }
}
