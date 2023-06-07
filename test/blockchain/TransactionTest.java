package blockchain;

import junit.framework.TestCase;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class TransactionTest extends TestCase {
    private final Transaction transaction;
    Blockchain blockchain;
    Miner miner1;
    Miner miner2;

    public TransactionTest() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        blockchain = Blockchain.getInstance();
        miner1 = new Miner(1, blockchain);
        miner2 = new Miner(2, blockchain);
        transaction = new Transaction(1, "miner1 sent 30 VC to miner2", miner1, miner2, 30);
    }

    public void testGetId() {
        assertEquals(1, transaction.getId());
    }

    public void testSetId() {
        transaction.setId(2);
        assertEquals(2, transaction.getId());
    }

    public void testGetAmount() {
        assertEquals(30, transaction.getAmount());
    }

    public void testGetSender() {
        assertEquals(miner1, transaction.getSender());
    }

    public void testGetReceiver() {
        assertEquals(miner2, transaction.getReceiver());
    }

    public void testGetMessage() {
        String message = "miner1 sent 30 VC to miner2";
        assertEquals(message, transaction.getMessage());
    }
}