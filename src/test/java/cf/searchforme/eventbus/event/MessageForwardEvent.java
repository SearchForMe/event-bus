package cf.searchforme.eventbus.event;

import java.util.List;

public class MessageForwardEvent extends MessageSendEvent {

    private final List<String> recipients;

    public MessageForwardEvent(String sender, String message, List<String> recipients) {
        super(sender, message);

        this.recipients = recipients;
    }

    public List<String> getRecipients(){
        return recipients;
    }
}
