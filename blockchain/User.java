package blockchain;

import blockchain.utils.BillHolder;
import blockchain.utils.KeyGenerator;

import java.security.*;

public class User implements BillHolder {
    private final PublicKey publicKey;

    private final KeyGenerator keyGenerator;
    private final Blockchain blockchain;
    private final String name;
    private byte[] signature;

    public User(String name, Blockchain blockchain) throws NoSuchAlgorithmException, InvalidKeyException {

        this.keyGenerator = new KeyGenerator();
        this.publicKey = keyGenerator.getPublicKey();
        this.blockchain = blockchain;
        this.name = name;
        this.signature = new byte[0];
    }

    @Override
    public void sendTransaction(Transaction transaction) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {

        blockchain.sendTransaction(transaction);
    }

    @Override
    public Transaction createTransaction(long id, String message, BillHolder receiver, long amount) throws SignatureException {
        this.signature = keyGenerator.sign(message);
        return new Transaction(TransactionHandler.generateId(), message, this, receiver, amount);
    }

    @Override
    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    @Override
    public byte[] getSignature() {
        return this.signature;
    }

    public String getName() {
        return name;
    }
}
