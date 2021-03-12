package cf.searchforme.eventbus;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * The main class. Allows to register, unregister and post events.
 */
public class EventBus {

    private LinkedList<Subscriber> subscribers = new LinkedList<>();

    private final String name;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public EventBus(){
        this.name = "";
    }

    /**
     * It is good practice to give each EventBus a name if you are using multiple ones.
     * @param name The name of the EventBus.
     */
    public EventBus(String name){
        this.name = name;
    }

    /**
     * Subscribes any given listener instances to the bus.
     * @param listeners The listener instances.
     */
    public void subscribeListeners(@NotNull EventListener... listeners){
        for (EventListener listener : listeners) {
            Arrays.stream(listener.getClass().getMethods())
                    .filter(method -> method.getAnnotation(Subscribe.class) != null
                            && method.getParameterTypes().length == 1
                            && Event.class.isAssignableFrom(method.getParameters()[0].getType()))
                    .forEach(method -> subscribers.add(new Subscriber(method, listener)));
        }
        subscribers = subscribers.stream().sorted().collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Unsubscribes the given listener class from the bus.
     * @param listener The listener instance.
     */
    public void unsubscribeListener(@NotNull EventListener listener){
        subscribers.removeIf(subscriber -> subscriber.getListener().equals(listener));
    }

    /**
     * Unsubscribes the given listener class from the bus. Used if you don't want to save the listener's instance.
     * @param listenerClass The listener class.
     */
    public void unsubscribeListener(@NotNull Class<EventListener> listenerClass){
        subscribers.removeIf(subscriber -> subscriber.getMethod().getDeclaringClass().equals(listenerClass));
    }

    public boolean isSubscribed(@NotNull EventListener listener){
        return subscribers.stream().anyMatch(subscriber -> subscriber.getListener().equals(listener));
    }

    public boolean isSubscribed(@NotNull Class<EventListener> listener){
        return subscribers.stream().anyMatch(subscriber -> subscriber.getListener().getClass().equals(listener));
    }

    /**
     * Clears the listeners.
     */
    public void unsubscribeAll(){
        subscribers.clear();
    }

    /**
     * Posts the event to all the registered listeners. Calls the methods annotated with {@link Subscribe}
     * @param event The event that is supposed to be posted.
     */
    public Event post(@NotNull Event event) {
        subscribers.forEach((subscriber) -> {
            Method method = subscriber.getMethod();
            EventListener listener = subscriber.getListener();
            if(method.getParameters()[0].getType().equals(event.getClass())){
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        return event;
    }

    /**
     * Posts the event to all the registered listeners asynchronously. Calls the methods annotated with {@link Subscribe}
     * @param event The event that is supposed to be posted.
     */
    public Future<Event> postAsync(@NotNull Event event) {
        return executor.submit(() -> post(event));
    }

    /**
     * Returns a clone of the subscriber list.
     * @return The cloned list.
     */
    public List<Subscriber> getSubscribers() {
        return (LinkedList<Subscriber>) subscribers.clone();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.isEmpty() ? "EventBus" : "EventBus{" + name + "}";
    }
}
