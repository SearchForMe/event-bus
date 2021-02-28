package cf.searchforme;

public class MainEventTest {

    public static void main(String[] args) {
        EventBus bus = new EventBus();

        bus.subscribeListeners(new MessageEventListener());

        MessageSendEvent one = new MessageSendEvent("foo", "this is a test");
        bus.post(one);
        System.out.println("Cancelled: " + one.isCancelled());

        MessageSendEvent two = new MessageSendEvent("foo", "bar");
        bus.postAsync(two);
    }

}
