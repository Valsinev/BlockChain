package blockchain;

import blockchain.utils.BillHolder;
import blockchain.utils.KeyGenerator;
import blockchain.utils.StringUtil;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Blockchain implements Serializable, BillHolder {

    //FIELDS ***************************************************************
    private static final int BLOCKCHAIN_SIZE = 15;

    private static final Blockchain INSTANCE;

    static {
        try {
            INSTANCE = new Blockchain(BLOCKCHAIN_SIZE);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private final Deque<Block> blocks;

    private final TransactionHandler transactionHandler;

    private final PublicKey publicKey;

    private final KeyGenerator keyGenerator;

    private byte[] signature;
    private final int maxBlocks;

    private int numberOfZerosForHashBlock = 0;


    //CONSTRUCTOR *********************************************************
    private Blockchain(int maxBlocks) throws NoSuchAlgorithmException, InvalidKeyException {
        this.maxBlocks = maxBlocks;
        this.transactionHandler = new TransactionHandler();
        this.blocks = new ArrayDeque<>();
        this.keyGenerator = new KeyGenerator();
        this.publicKey = keyGenerator.getPublicKey();
        this.signature = new byte[0];
    }


    public static Blockchain getInstance() {
        return INSTANCE;
    }


    //METHODS **************************************************************
    public synchronized void addBlock(Block block) throws SignatureException {
        LocalTime timeBeforeValidBlock = LocalTime.now();
        LocalTime timeAfterFindingBlock;
        if (blocks.size() < maxBlocks && blockValidator(block)) {
            //send transaction with VC to block creator
            String blockCreationRewardMessage = block.getMiner() + " gets 100 VC";
            Transaction reward = createTransaction(TransactionHandler.generateId(),
                    blockCreationRewardMessage,
                    this,
                    100);
            transactionHandler.addTransaction(reward);
            ///////////////////////////////////////////

            transactionHandler.updateCurrentTransactions(block.getMessages());
            blocks.add(block);
            timeAfterFindingBlock = LocalTime.now();
            Duration creationTime = Duration.between(timeBeforeValidBlock, timeAfterFindingBlock);
            regulateComplexity(creationTime);
        }
    }

    public void sendTransaction(Transaction transaction) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if(transactionHandler.validateSignature(transaction) &&
            transactionHandler.validateTransactionId(transaction) &&
            transactionHandler.validateFunds(transaction)) {
            transactionHandler.addTransaction(transaction);
        }
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

    public int getNumberOfZerosForHashBlock() {
        return numberOfZerosForHashBlock;
    }

    public Block getLastBlock() {
        if (blocks.isEmpty()) {
            return null;
        } else return blocks.getLast();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Block b: blocks
             ) {
            stringBuilder.append(b.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    private void regulateComplexity(Duration creationTime) {

        if (creationTime.get(ChronoUnit.SECONDS) == 1) {
            numberOfZerosForHashBlock++;
            blocks.getLast().setnPrinter("N was increased to " + numberOfZerosForHashBlock);
        } else if (creationTime.get(ChronoUnit.SECONDS) > 60 && numberOfZerosForHashBlock > 0) {
            numberOfZerosForHashBlock--;
            blocks.getLast().setnPrinter("N was decreased to " + numberOfZerosForHashBlock);
        } else blocks.getLast().setnPrinter("N stays the same");
    }

    private boolean blockValidator(Block block) {
        boolean isValid = false;
        StringBuilder zeros = new StringBuilder();
        String hashOfBlock = StringUtil.applySha256(blockFieldsToString(block));
        if (hashOfBlock.equals(block.getHash())) {
            isValid = true;
        }
        if (numberOfZerosForHashBlock > 0) {
            zeros.append("0".repeat(numberOfZerosForHashBlock));
            if (hashOfBlock.substring(0, numberOfZerosForHashBlock).contentEquals(zeros) &&
                    block.getHashOfPreviousBlock().equals(blocks.getLast().getHash())) {
                isValid = true;
            }
        }
        if (!blocks.isEmpty() && !block.getHashOfPreviousBlock().equals(blocks.getLast().getHash())) {
            isValid = false;
        }
        if (blocks.isEmpty()) {
            isValid = block.getHashOfPreviousBlock().equals("0");
        }
        return isValid;
    }

    private String blockFieldsToString(Block block) {
        return block.allFieldsToString() +
                block.getMagicNumber();
    }

    public List<Transaction> getCurrentTransactions() {
        return transactionHandler.getCurrentTransactions();
    }

    public long getSize() {
        return this.blocks.size();
    }
}
