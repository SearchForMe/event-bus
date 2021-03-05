package cf.searchforme.eventbus;

import java.lang.reflect.Method;

/**
 * Utility class used to sort the subscribed methods by priority.
 */
public class ComparableMethod implements Comparable<ComparableMethod> {

    private final Method method;
    private final int priority;

    public ComparableMethod(Method method){
        this.method = method;

        Subscribe annotation = method.getAnnotation(Subscribe.class);

        this.priority = annotation.priority().getPriority();
    }

    public Method getMethod(){
        return method;
    }

    public int getPriority(){
        return priority;
    }

    @Override
    public int compareTo(ComparableMethod o) {
        return -1 * Integer.compare(getPriority(), o.getPriority());
    }
}
