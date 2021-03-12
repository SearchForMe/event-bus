package cf.searchforme.eventbus;

import cf.searchforme.eventbus.event.MessageForwardEvent;
import cf.searchforme.eventbus.event.MessageSendEvent;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MainEventTest {

    EventBus eventBus = new EventBus();

    @Test
    void testSubscription(){
        long start = System.currentTimeMillis();

        eventBus.subscribeListeners(new MessageListener(), new OtherMessageListener());

        long end = System.currentTimeMillis();
        System.out.println("DEBUG: testSubscription took " + (end - start) + " ms");

        testPosting();
    }

    void testPosting() {
        long start = System.currentTimeMillis();
        eventBus.post(new MessageSendEvent("foo", "test!"));
        eventBus.post(new MessageForwardEvent("foo", "test!", Arrays.asList("the", "rock")));
        long end = System.currentTimeMillis();
        System.out.println("DEBUG: testPosting took " + (end - start) + " ms");
    }

}
