package blockchain;

import java.security.SignatureException;

public class MinerThread extends Thread{
    private final Miner miner;

    public MinerThread(Miner miner) {

        this.miner = miner;
    }
    @Override
    public void run() {
        try {
            miner.Mine();

        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
