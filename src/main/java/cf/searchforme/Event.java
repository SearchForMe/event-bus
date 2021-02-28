package cf.searchforme;

/**
 * The abstract class that is to be extended when making a custom event.
 */
public abstract class Event {

    public String name;

    /**
     * @return Returns the event name. If none is provided, it will return the class name.
     */
    public String getName() {
        if (name == null) {
            name = getClass().getSimpleName();
        }

        return name;
    }

}
