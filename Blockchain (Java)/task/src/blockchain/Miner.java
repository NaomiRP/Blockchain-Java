package blockchain;

public class Miner implements Runnable {

    private final Blockchain blockchain;
    private final Person miner;

    public Miner(Blockchain blockchain, Person miner) {
        this.blockchain = blockchain;
        this.miner = miner;
    }

    public void run() {
        while (blockchain.isAcceptingNewBlocks()) {
            blockchain.addBlockToChain(new Block(blockchain.lastBlock(), blockchain.requiredZeros(), miner, blockchain.queuedTransactions()));
        }
    }
}
