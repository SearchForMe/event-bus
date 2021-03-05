# event-bus

A compact framework that allows you to create custom events and listen to them.
Especially useful for projects that use a UI, such as Swing or Android, when trying to
separate the View and the Controller

It uses streams, which means it is supported only by **Java 8+**
# Usage

Keep in mind that EventBus is not a class that contains static methods, which means that
you can have multiple instances of it in a project, allowing you to have different event streams.

```java


private final EventBus bus=new EventBus();
```
This should be placed in your main class.

### Example Event

```java
import Cancellable;
import Event;

/**
 * Implement the Cancellable interface if you want the
 * event to be cancellable.
 */
public class MessageSendEvent extends Event implements Cancellable {

    private boolean cancelled;

    private final String sender;
    private final String message;

    public MessageSendEvent(String sender, String message) {
        name = "CustomName";

        this.sender = sender;
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

}
```

This will create a custom MessageSend event, that can be cancelled.

### Creating a Listener

```java
import EventListener;
import cf.searchforme.Listener;
import EventPriority;
import Subscribe;

public class MessageEventListener implements EventListener {

    @Subscribe(priority = EventPriority.HIGH)
    public void onEvent(MessageSendEvent event) {
        if (event.getMessage().contains("test")) {
            event.setCancelled(true);
            return;
        }

        System.out.printf("%s sent a message: %s\n", event.getSender(), event.getMessage());
    }
}
```

This will listen for the MessageSendEvent, and cancel it if the message contains "test".

### Registering and calling the Event

```java
bus.subscribeListeners(new MessageEventListener());

MessageSendEvent event = new MessageSendEvent("foo", "bar");
bus.post(event);
```

You have to subscribe the listener class in the EventBus. To call an event you simply have to
post it to the bus. You can check whether it has been cancelled with the Cancellable#isCancelled method.

# Adding the dependency

### Maven
If you use Maven, add this to your pom.xml:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.SearchForMe</groupId>
        <artifactId>event-bus</artifactId>
        <version>v1.0.1</version>
    </dependency>
</dependencies>
```

### Gradle

Whereas if you use Gradle, add this to your build.gradle:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}

dependencies {
    compile 'com.github.SearchForMe:event-bus:v1.0.1'
}
```

### JAR File

If you don't use any of the above, you can simply add the [JAR file](https://github.com/SearchForMe/event-bus/releases/tag/v1.0) to your dependencies.
