package cf.searchforme.eventbus;

import cf.searchforme.eventbus.event.MessageForwardEvent;
import cf.searchforme.eventbus.event.MessageSendEvent;

public class MessageListener implements EventListener {

    @Subscribe(priority = EventPriority.HIGH)
    public void onMessageSendHigh(MessageSendEvent event){
        if(event.getMessage().contains("test")){
            System.out.println("Message (" + event.getMessage() + ") has been cancelled.");
            event.setCancelled(true);
        }
    }

    @Subscribe(priority = EventPriority.NORMAL)
    public void onMessageSendNormal(MessageSendEvent event){
        System.out.printf("%s sent a message: %s%s\n", event.getSender(), event.getMessage(), event.isCancelled() ?
                " (cancelled)" : "");
    }

    @Subscribe(priority = EventPriority.LOW)
    public void onMessageForward(MessageForwardEvent event){
        System.out.printf("%s forwarded a message to %s: %s%s\n", event.getSender(), event.getRecipients().toString(),
                event.getMessage(), event.isCancelled() ? " (cancelled)" : "");
    }

}
