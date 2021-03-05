package cf.searchforme.eventbus;

/**
 * The event priority. Used in the {@link Subscribe} annotation. High priority listeners will be called first.
 */
public enum EventPriority {

    LOW(0), NORMAL(1), HIGH(2);

    final private int priority;

    EventPriority(int priority){
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

}
