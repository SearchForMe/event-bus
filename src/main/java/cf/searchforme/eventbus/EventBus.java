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

    private final Map<ComparableMethod, EventListener> methods = new TreeMap<>();

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
                    .forEach(method -> methods.put(new ComparableMethod(method), listener));
        }

        TreeSet<ComparableMethod> set = new TreeSet<>(methods.keySet());

        set = set.stream().sorted()
                .collect(Collectors.toCollection(TreeSet::new));

        set.forEach((method) -> methods.put(method, methods.get(method)));
    }

    /**
     * Unsubscribes the given listener class from the bus.
     * @param listener The listener instance.
     */
    public void unsubscribeListener(EventListener listener){
        methods.entrySet().removeIf(entry -> entry.getValue().equals(listener));
    }

    /**
     * Unsubscribes the given listener class from the bus. Used if you don't want to save the listener's instance.
     * @param listenerClass The listener class.
     */
    public void unsubscribeListener(Class<EventListener> listenerClass){
        methods.entrySet().removeIf(entry -> entry.getKey().getMethod().getDeclaringClass().equals(listenerClass));
    }

    /**
     * Clears the listeners.
     */
    public void unsubscribeAll(){
        methods.clear();
    }

    /**
     * Posts the event to all the registered listeners. Calls the methods annotated with {@link Subscribe}
     * @param event The event that is supposed to be posted.
     */
    public Event post(Event event) {
        methods.keySet().forEach((m) -> {
            Method method = m.getMethod();
            EventListener listener = methods.get(m);
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
