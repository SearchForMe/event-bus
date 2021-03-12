package cf.searchforme.eventbus;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public final class Subscriber implements Comparable<Subscriber>{

    private final Method method;
    private final EventListener listener;
    private final int priority;

    public Subscriber(Method method, EventListener instance) {
        this.method = method;
        this.listener = instance;

        Subscribe annotation = method.getAnnotation(Subscribe.class);

        this.priority = annotation.priority().getPriority();
    }

    public Method getMethod() {
        return method;
    }

    public EventListener getListener() {
        return listener;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(@NotNull Subscriber o) {
        return -1 * Integer.compare(getPriority(), o.getPriority());
    }
}
