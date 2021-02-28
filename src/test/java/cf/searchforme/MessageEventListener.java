package cf.searchforme;

public class MessageEventListener implements EventListener {

    @Subscribe(priority = EventPriority.HIGH)
    public void onEvent(MessageSendEvent event){
        if(event.getMessage().contains("test")){
            event.setCancelled(true);
            return;
        }

        System.out.printf("%s sent a message: %s\n", event.getSender(), event.getMessage());
    }

}
