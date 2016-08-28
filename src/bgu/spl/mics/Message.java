package bgu.spl.mics;

/**
 * A message is a data-object which is passed between micro-services as a means
 * of communication. The Message interface is a "Marker" interface which means
 * that it is used only to mark other types of objects as messages. It does not
 * contain any methods but every class that you want to send as a message (using
 * the {@link MessageBus}) must implement it.
 */
public interface Message {

}
