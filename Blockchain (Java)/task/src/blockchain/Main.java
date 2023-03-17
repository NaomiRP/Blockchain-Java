package blockchain;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter how many zeros the hash must start with: ");
        Scanner in = new Scanner(System.in);
        int minLeadingZeros = in.nextInt();

        Blockchain blockchain = new Blockchain();
        for (int i = 0; i < 5; i++) {
            blockchain.generateBlock(minLeadingZeros);
        }
        if (!blockchain.isValid())
            System.out.println("Invalid blockchain!");
    }
}
