package blockchain;

import java.util.List;

public class Block {

    private final Miner miner;
    private final int id;
    private final long timestamp;
    private final long magicNumber;
    private final String hashOfPreviousBlock;
    private final String hash;
    private long timeGenerated;
    private String nPrinter = "";
    private final List<Transaction> blockData;
    private final String minerRewardMessage;

    public Block(Miner miner,
                 int id,
                 long timestamp,
                 long magicNumber,
                 String hashOfPreviousBlock,
                 String hash,
                 List<Transaction> blockData) {
        this.miner = miner;
        this.id = id;
        this.timestamp = timestamp;
        this.magicNumber = magicNumber;
        this.hashOfPreviousBlock = hashOfPreviousBlock;
        this.hash = hash;
        this.blockData = blockData;
        this.timeGenerated = 0;
        this.minerRewardMessage = miner + " gets 100 VC";
    }

    public int getId() {
        return id;
    }

    public void setnPrinter(String nPrinter) {
        this.nPrinter = nPrinter;
    }

    public long getMagicNumber() {
        return magicNumber;
    }

    public String getHashOfPreviousBlock() {
        return hashOfPreviousBlock;
    }


    public String getHash() {
        return hash;
    }


    @Override
    public String toString() {
        StringBuilder blockDataString = new StringBuilder();
        for (int i = 0; i <= blockData.size() - 1; i++) {
            blockDataString.append(blockData.get(i).getMessage()).append("\n");
        }
        return String.format(
                """
                        Block:
                        Created by: %s
                        %s
                        Id: %d
                        Timestamp: %s
                        Magic number: %d
                        Hash of the previous block:\s
                        %s
                        Hash of the block:\s
                        %s
                        Block data:
                        %s
                        Block was generating for %d seconds
                        %s
                        """,
                this.miner,
                this.minerRewardMessage,
                this.id,
                this.timestamp,
                this.magicNumber,
                this.hashOfPreviousBlock,
                this.hash,
                blockDataString,
                this.timeGenerated,
                this.nPrinter
        ).replaceAll ("(?m)^[ \t]*\r?\n", "");
    }

    public String allFieldsToString() {

        return minerRewardMessage +
                miner.getId() +
                id +
                timestamp +
                hashOfPreviousBlock +
                String.join("", blockData.stream().map(Transaction::getMessage).toList());
    }
    public Miner getMiner() {
        return this.miner;
    }

    public void setTimeGenerated(long timeGenerated) {
        this.timeGenerated = timeGenerated;
    }

    public List<Transaction> getMessages() {
        return this.blockData;
    }
}
