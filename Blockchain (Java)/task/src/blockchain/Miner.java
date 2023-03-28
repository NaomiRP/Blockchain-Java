package blockchain;

public class Miner implements Runnable {

    private final Blockchain blockchain;
    private final int id;

    public Miner(Blockchain blockchain, int id) {
        this.blockchain = blockchain;
        this.id = id;
    }

    public void run() {
        while (blockchain.isAcceptingNewBlocks()) {
            blockchain.addBlockToChain(new Block(blockchain.lastBlock(), blockchain.requiredZeros(), id));
        }
    }
}
