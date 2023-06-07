package blockchain;

import junit.framework.TestCase;

import java.security.*;
import java.util.List;

public class TransactionHandlerTest extends TestCase {
    Blockchain blockchain;
    TransactionHandler transactionHandler;
    Transaction transaction;
    Miner miner1;
    Miner miner2;

    public TransactionHandlerTest() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        blockchain = Blockchain.getInstance();
        transactionHandler = new TransactionHandler();
        miner1 = new Miner(1, blockchain);
        miner2 = new Miner(2, blockchain);
        transaction = new Transaction(1,"miner1 sent 30 VC to miner2", miner1, miner2, 30);
    }


    public void testAddTransaction() {
        transactionHandler.addTransaction(transaction);
        assertEquals(1, transactionHandler.getAllTransactions().size());
    }

    public void testGetCurrentTransaction() {
        transactionHandler.addTransaction(transaction);
        assertEquals(List.of(transaction), transactionHandler.getCurrentTransactions());
    }

    public void testUpdateCurrentTransaction() {
        Transaction transaction1 = new Transaction(1,"miner1 sent 300 VC to miner2", miner1, miner2, 300);
        Transaction transaction2 = new Transaction(1,"miner1 sent 300 VC to miner2", miner1, miner2, 300);
        Transaction transaction3 = new Transaction(1,"miner1 sent 30 VC to miner2", miner1, miner2, 30);
        Transaction transaction4 = new Transaction(1,"miner1 sent 30 VC to miner2", miner1, miner2, 30);
        transactionHandler.addTransaction(transaction1);
        transactionHandler.addTransaction(transaction2);
        transactionHandler.addTransaction(transaction3);
        transactionHandler.addTransaction(transaction4);
        List<Transaction> transactionsToRemove = List.of(transaction1, transaction2);
        transactionHandler.updateCurrentTransactions(transactionsToRemove);
        assertEquals(List.of(transaction3, transaction4), transactionHandler.getCurrentTransactions());
    }

    public void testGenerateId() {
        long id = TransactionHandler.generateId();
        assertEquals(1, id);
    }

    public void testGetBalance() {
        transactionHandler.addTransaction(transaction);
        assertEquals(30, transactionHandler.getBalance(miner2));
    }

    public void testValidateSignature() throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        Transaction transaction1 = miner1.createTransaction(1, "miner1 sent 100 VC to miner2", miner2, 100);
        boolean isValid = transactionHandler.validateSignature(transaction1);
        assertTrue(isValid);
    }
}