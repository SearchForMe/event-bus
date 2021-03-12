package cf.searchforme.eventbus;

import cf.searchforme.eventbus.event.MessageForwardEvent;
import cf.searchforme.eventbus.event.MessageSendEvent;

public class OtherMessageListener implements EventListener {

    @Subscribe
    public void onForward(MessageSendEvent event){
        System.out.println("Other Message Listener was called");
    }

}
