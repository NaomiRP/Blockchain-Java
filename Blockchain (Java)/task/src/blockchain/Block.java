package blockchain;

import java.util.Date;
import java.util.Random;

import static java.lang.Math.random;
import static java.lang.Math.round;
import static java.lang.Math.toIntExact;

public class Block {

    private final int miner;
    private final int id;
    private final long timeStamp;
    private int magic;
    private final String previousHash;
    private String hash;

    public Block(Block previousBlock, int minLeadingZeros) {
        timeStamp = new Date().getTime();
        miner = toIntExact(round(random()*10));
        id = previousBlock == null ? 1 : previousBlock.id + 1;
        previousHash = previousBlock == null ? "0" : previousBlock.hash;
        setHash(minLeadingZeros);
    }

    private void setHash(int minLeadingZeros) {
        Random r = new Random();
        do {
            magic = r.nextInt();
            hash = StringUtil.applySha256(id + timeStamp + miner + magic + previousHash);
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
