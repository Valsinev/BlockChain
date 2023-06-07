package blockchain;


import blockchain.utils.BillHolder;

public class Transaction {

    private final long amount;
    private long id;

    String message;
    private final BillHolder sender;
    private final BillHolder receiver;

    public Transaction(long id, String message, BillHolder sender, BillHolder receiver, long amount) {
        this.amount = amount;
        this.id = id;
        this.receiver = receiver;
        this.sender = sender;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getAmount() {
        return amount;
    }


    public BillHolder getSender() {
        return sender;
    }

    public BillHolder getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

}
