package blockchain.utils;

import blockchain.Transaction;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;

public interface BillHolder {
    void sendTransaction(Transaction transaction) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException;

    Transaction createTransaction(long id, String message, BillHolder receiver, long amount) throws SignatureException;

    PublicKey getPublicKey();
    byte[] getSignature();
}
