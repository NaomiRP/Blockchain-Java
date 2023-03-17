package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blockchain {

    private List<Block> blockchain = new ArrayList<>();

    public void generateBlock(int minLeadingZeros) {
        int curLen = blockchain.size();
        if (curLen == 0)
            blockchain.add(new Block(minLeadingZeros));
        else
            blockchain.add(new Block(blockchain.get(curLen - 1), minLeadingZeros));
        long genEndTime = new Date().getTime();
        Block newBlock = blockchain.get(curLen);
        long genTime = genEndTime - newBlock.getTimeStamp();
        System.out.println(newBlock);
        System.out.println("Block was generating for " + genTime + " seconds\n");
    }

    public boolean isValid() {
        if (!"0".equals(blockchain.get(0).getPreviousHash()))
            return false;
        for (int i = 1; i < blockchain.size(); i++) {
            if (!blockchain.get(i).getPreviousHash().equals(blockchain.get(i-1).getHash()))
                return false;
        }
        return true;
    }
}
