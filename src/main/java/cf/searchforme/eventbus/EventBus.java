package cf.searchforme.eventbus;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * The main class. Allows to register, unregister and post events.
 */
public class EventBus {

    private List<Subscriber> subscribers = new LinkedList<>();

    /**
     * Subscribes any given listener instances to the bus.
     * @param listeners The listener instances.
     */
    public void subscribeListeners(EventListener... listeners){
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
    public void unsubscribeListener(EventListener listener){
        subscribers.removeIf(subscriber -> subscriber.getListener().equals(listener));
    }

    /**
     * Unsubscribes the given listener class from the bus. Used if you don't want to save the listener's instance.
     * @param listenerClass The listener class.
     */
    public void unsubscribeListener(Class<EventListener> listenerClass){
        subscribers.removeIf(subscriber -> subscriber.getMethod().getDeclaringClass().equals(listenerClass));
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
    public Event post(Event event) {
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
    public Event postAsync(Event event) {
        CompletableFuture<Event> future = CompletableFuture.supplyAsync(() -> post(event));

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException exception){
            exception.printStackTrace();
        }

        return null;
    }

}
