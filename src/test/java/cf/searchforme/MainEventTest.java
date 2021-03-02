package cf.searchforme;

public class MainEventTest {

    public static void main(String[] args) {
        EventBus bus = new EventBus();

        bus.subscribeListeners(new MessageEventListener());

        MessageSendEvent one = new MessageSendEvent("foo", "test!");
        bus.post(one);
        System.out.println("Cancelled: " + one.isCancelled());

        MessageSendEvent two = new MessageSendEvent("bar", "test!");
        MessageSendEvent futureEvent = (MessageSendEvent) bus.postAsync(two);
        System.out.println("Cancelled: " + futureEvent.isCancelled());
    }

}
