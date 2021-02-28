package cf.searchforme;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * The main class. Allows to register, unregister and post events.
 */
public class EventBus {

    private final List<EventListener> registeredListeners = new ArrayList<>();

    /**
     * Subscribes any given listener classes to the bus.
     * @param listeners The listener instances.
     */
    public void subscribeListeners(EventListener... listeners){
        registeredListeners.addAll(Arrays.asList(listeners));
    }

    /**
     * Unsubscribes the given listener class from the bus.
     * @param listener The listener instance.
     */
    public void unsubscribeListener(EventListener listener){
        registeredListeners.remove(listener);
    }

    /**
     * Unsubscribes the given listener class from the bus. Used if you don't want to save the listener instance.
     * @param listenerClass The listener class.
     */
    public void unsubscribeListener(Class<EventListener> listenerClass){
        registeredListeners.removeIf(listener -> listener.getClass().equals(listenerClass));
    }

    /**
     * Clears the listeners.
     */
    public void unsubscribeAll(){
        registeredListeners.clear();
    }

    /**
     * Posts the event to all the registered listeners. Calls the methods annotated with {@link cf.searchforme.Subscribe}
     * @param event The event that is supposed to be posted.
     */
    public void post(Event event) {
        registeredListeners.forEach(listener -> {
            Arrays.stream(listener.getClass().getMethods())
                    .filter(method -> method.getAnnotation(Subscribe.class) != null
                            && method.getParameterTypes().length == 1
                            && method.getParameters()[0].getType().equals(event.getClass()))
                    .sorted(Comparator.comparingInt((Method one)
                            -> one.getAnnotation(Subscribe.class).priority().getPriority()).reversed())
                    .forEachOrdered(method -> {
                        try {
                            method.invoke(listener, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
        });
    }

    /**
     * Posts the event to all the registered listeners asynchronously. Calls the methods annotated with {@link cf.searchforme.Subscribe}
     * @param event The event that is supposed to be posted.
     */
    public void postAsync(Event event){
        CompletableFuture.runAsync(() -> {
            post(event);
        });
    }

}
