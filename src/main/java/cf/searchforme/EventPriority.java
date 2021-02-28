package cf.searchforme;

/**
 * The event priority. Used in the {@link cf.searchforme.Subscribe} annotation. High priority will be called first.
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
