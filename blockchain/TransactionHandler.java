package blockchain;

import blockchain.utils.BillHolder;

import java.security.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionHandler {

    private static long transactionIds = 0;

    private volatile List<Transaction> currentTransactions;


    private final List<Transaction> allTransactions;

    public TransactionHandler() {
        this.currentTransactions = Collections.synchronizedList(new ArrayList<>());
        this.allTransactions = Collections.synchronizedList(new ArrayList<>());
    }
    public void addTransaction(Transaction transaction) {
        this.currentTransactions.add(transaction);
        this.allTransactions.add(transaction);
    }

    public long getBalance(BillHolder billHolder) {
        //all transactions when BillHolder sends money
        long sent = allTransactions.stream()
                .filter(transaction -> transaction.getSender() == billHolder)
                .map(Transaction::getAmount)
                .mapToLong(Long::longValue).sum();

        //all transactions when BillHolder receives money
        long received = allTransactions.stream()
                .filter(transaction -> transaction.getReceiver() == billHolder)
                .map(Transaction::getAmount)
                .mapToLong(Long::longValue).sum();

        return received - sent;
    }

    public boolean validateTransactionId(Transaction transaction) {
        return transaction.getId() > transactionIds;
    }

    public boolean validateSignature(Transaction transaction) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Signature signatureVerificationAlgorithm = Signature.getInstance("SHA256WithRSA");
        signatureVerificationAlgorithm.initVerify(transaction.getSender().getPublicKey());
        signatureVerificationAlgorithm.update(transaction.getMessage().getBytes());
        return signatureVerificationAlgorithm.verify(transaction.getSender().getSignature());
    }

    public List<Transaction> getCurrentTransactions() {
        return currentTransactions;
    }

    protected synchronized void updateCurrentTransactions(List<Transaction> transactions) {
        //current - messages
        currentTransactions = currentTransactions.stream()
                .filter(x -> !transactions.contains(x))
                .collect(Collectors.toList());
    }

    public void updateTransactionIds() {
        transactionIds++;
    }

    public static long generateId() {
        return transactionIds + 1;
    }

    public List<Transaction> getAllTransactions() {
        return allTransactions;
    }

    public boolean validateFunds(Transaction transaction) {
        long senderVC = getBalance(transaction.getSender());
        return senderVC - transaction.getAmount() > 0; //if sender has enough money to send
    }
}
