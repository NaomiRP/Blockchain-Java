package blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blockchain {

    private final List<Block> blockchain = new ArrayList<>();

    private int nLeadingZeros = 0;

    public void generateBlock() {
        int curLen = blockchain.size();
        if (curLen == 0)
            blockchain.add(new Block(null, nLeadingZeros));
        else
            blockchain.add(new Block(blockchain.get(curLen - 1), nLeadingZeros));
        long genEndTime = new Date().getTime();
        Block newBlock = blockchain.get(curLen);
        long genTime = genEndTime - newBlock.getTimeStamp();
        System.out.println(newBlock);
        System.out.println("Block was generating for " + genTime + " seconds");
        if (genTime > 60 && nLeadingZeros > 0) {
            nLeadingZeros--;
            System.out.println("N was decreased to " + nLeadingZeros + "\n");
        } else if (genTime < 15) {
            nLeadingZeros++;
            System.out.println("N was increased to " + nLeadingZeros + "\n");
        } else {
            System.out.println("N stays the same\n");
        }
    }

    public boolean isValid() {
        Block cur = blockchain.get(0);
        if (!"0".equals(cur.getPreviousHash()) || cur.getId() != 1)
            return false;
        Block prev;
        for (int i = 1; i < blockchain.size(); i++) {
            prev = cur;
            cur = blockchain.get(i);
            if (!cur.getPreviousHash().equals(prev.getHash()) || cur.getId() - 1 != i)
                return false;
        }
        return true;
    }
}
