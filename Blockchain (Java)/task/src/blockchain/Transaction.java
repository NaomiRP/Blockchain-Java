package blockchain;

import java.util.Random;

public class Transaction {

    private final Person sender;
    private int amount;
    private final Person recipient;

    private final int id;

    private final byte[] signature;

    public Transaction(Person sender, int availableAmount, Person recipient, int id) {
        this.sender = sender;
        this.recipient = recipient;
        this.id = id;
        determineAmount(availableAmount);
        signature = sender.signTransaction(transactionData());
    }

    public Person getSender() {
        return sender;
    }

    public int getAmount() {
        return amount;
    }

    public Person getRecipient() {
        return recipient;
    }

    public int getId() {
        return id;
    }

    public boolean verifySignature() {
        return SignatureUtil.verify(transactionData(), signature, sender.getPublicKey());
    }

    private void determineAmount(int availableAmount) {
        var r = new Random();
        amount = r.nextInt(1, availableAmount);
    }

    private String transactionData() {
        return id + ", " + this;
    }

    @Override
    public String toString() {
        return sender + " sent " + amount + " VC to " + recipient;
    }
}
