package blockchain;

import java.util.Date;
import java.util.Random;

public class Block {

    private final int id;
    private final long timeStamp;
    private int magic;
    private final String previousHash;
    private String hash;

    public Block(int minLeadingZeros) {
        timeStamp = new Date().getTime();
        id = 1;
        previousHash = "0";
        setHash(minLeadingZeros);
    }

    public Block(Block previousBlock, int minLeadingZeros) {
        timeStamp = new Date().getTime();
        id = previousBlock.id + 1;
        previousHash = previousBlock.hash;
        setHash(minLeadingZeros);
    }

    private void setHash(int minLeadingZeros) {
        Random r = new Random();
        do {
            magic = r.nextInt();
            hash = StringUtil.applySha256(id + timeStamp + magic + previousHash);
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
        return "Block:\nId: " + id +
               "\nTimestamp: " + timeStamp +
               "\nMagic number: " + magic +
               "\nHash of the previous block:\n" + previousHash +
               "\nHash of the block:\n" + hash;
    }
}
