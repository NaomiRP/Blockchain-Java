package blockchain;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static blockchain.StringUtil.applySha256;

public class Block {

    private final int miner;
    private final int id;
    private final long timeStamp;
    private int magic;
    private final String previousHash;
    private final List<Message> messages;
    private String hash;

    public Block(Block previousBlock, int minLeadingZeros, int miner, List<Message> messages) {
        timeStamp = new Date().getTime();
        this.miner = miner;
        this.messages = messages;
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

    public List<Message> getMessages() {
        return messages;
    }

    public String getHash() {
        return hash;
    }

    public boolean isValidHash() {
        return applySha256(stringify()).equals(hash);
    }

    private String stringify() {
        return id + timeStamp + miner + magic + previousHash + stringifyMessages(";");
    }

    private String stringifyMessages(String delimiter) {
        List<String> messageStrings = messages.stream().map(Message::toString).toList();
        return String.join(delimiter, messageStrings);
    }

    @Override
    public String toString() {
        return "Block:" +
               "\nCreated by miner # " + miner +
               "\nId: " + id +
               "\nTimestamp: " + timeStamp +
               "\nMagic number: " + magic +
               "\nHash of the previous block:\n" + previousHash +
               "\nHash of the block:\n" + hash +
               "\nBlock data: " + (messages.size() == 0 ? "no messages" : "\n" + stringifyMessages("\n"));
    }
}
