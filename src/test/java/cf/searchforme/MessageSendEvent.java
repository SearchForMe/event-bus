package cf.searchforme;

public class MessageSendEvent extends Event implements Cancellable {

    private boolean cancelled;

    private final String sender;
    private final String message;

    public MessageSendEvent(String sender, String message){
        name = "CustomName";

        this.sender = sender;
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage(){
        return message;
    }

}
