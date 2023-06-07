package blockchain;

import blockchain.utils.BillHolder;
import blockchain.utils.KeyGenerator;
import blockchain.utils.StringUtil;

import java.security.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;

class Miner implements BillHolder {
    private final int Id;
    private final Blockchain blockchain;
    private final KeyGenerator keyGenerator;

    private final PublicKey publicKey;
    private byte[] signature;

    public Miner(int id, Blockchain blockchain) throws NoSuchAlgorithmException, InvalidKeyException {
        Id = id;
        this.blockchain = blockchain;
        this.keyGenerator = new KeyGenerator();
        this.publicKey = keyGenerator.getPublicKey();
        this.signature = new byte[0];
    }

    public void Mine() throws SignatureException {
        //mining
        LocalTime creationStart = LocalTime.now();
        List<Transaction> blockData = blockchain.getCurrentTransactions();

        Block lastBlock = blockchain.getLastBlock();
        String lastBlockHash;
        int blockId;
        if (lastBlock == null) {
            blockId = 1;
            lastBlockHash = "0";
        } else {
            blockId = lastBlock.getId() + 1;
            lastBlockHash = lastBlock.getHash();
        }
        int numberOfZeros = blockchain.getNumberOfZerosForHashBlock();

        long timeStamp = new Date().getTime();
        StringBuilder allFieldsWithoutMNumber = new StringBuilder();
        allFieldsWithoutMNumber.append(this).append(" gets 100 VC");
        allFieldsWithoutMNumber.append(this.Id);
        allFieldsWithoutMNumber.append(blockId);
        allFieldsWithoutMNumber.append(timeStamp);
        allFieldsWithoutMNumber.append(lastBlockHash);
        allFieldsWithoutMNumber.append(String.join("", blockData.stream().map(Transaction::getMessage).toList()));

        Random random = new Random();
        long magicNumber = random.nextInt();

        StringBuilder zeros = new StringBuilder();
        zeros.append("0".repeat(Math.max(0, numberOfZeros)));

        while (!generateHash(magicNumber, allFieldsWithoutMNumber.toString()).substring(0, numberOfZeros).contentEquals(zeros)) {
            magicNumber = random.nextInt();
        }


        Block newBlock = new Block(
                this,
                blockId,
                timeStamp,
                magicNumber,
                lastBlockHash,
                generateHash(magicNumber, allFieldsWithoutMNumber.toString()),
                blockData);

        LocalTime creationEnd = LocalTime.now();
        newBlock.setTimeGenerated(Duration.between(creationStart, creationEnd).get(ChronoUnit.SECONDS));

        blockchain.addBlock(newBlock);

    }

    protected String generateHash(long magicNumber, String blockFields) {
        return StringUtil.applySha256(blockFields + magicNumber);
    }

    @Override
    public String toString() {
        return "miner" + this.Id;
    }

    public long getId() {
        return this.Id;
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
}
