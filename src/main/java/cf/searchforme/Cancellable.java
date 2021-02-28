package cf.searchforme;

/**
 * This interface is to be implemented when an event should be able to be cancelled.
 */
public interface Cancellable {

    /**
     * @return Whether an event is cancelled.
     */
    boolean isCancelled();

    /**
     * @param cancel Sets the cancel state.
     */
    void setCancelled(boolean cancel);

}
