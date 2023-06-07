package blockchain;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, InvalidKeyException {

        Blockchain blockchain = Blockchain.getInstance();

        ExecutorService executorService = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 25; i++) {
            executorService.execute(new MinerThread(new Miner(i,blockchain)));
        }


        executorService.shutdown();
        Thread.sleep(10000);



        System.out.println(blockchain);
    }
}
