package blockchain;

import java.util.Date;
import java.util.Random;

import static blockchain.StringUtil.applySha256;

public class Block {

    private final int miner;
    private final int id;
    private final long timeStamp;
    private int magic;
    private final String previousHash;
    private String hash;

    public Block(Block previousBlock, int minLeadingZeros, int miner) {
        timeStamp = new Date().getTime();
        this.miner = miner;
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

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getMagicNumber() {
        return magic;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public boolean isValidHash() {
        return applySha256(stringify()).equals(hash);
    }

    private String stringify() {
        return id + timeStamp + miner + magic + previousHash;
    }

    @Override
    public String toString() {
        return "Block:" +
               "\nCreated by miner # " + miner +
               "\nId: " + id +
               "\nTimestamp: " + timeStamp +
               "\nMagic number: " + magic +
               "\nHash of the previous block:\n" + previousHash +
               "\nHash of the block:\n" + hash;
    }
}
