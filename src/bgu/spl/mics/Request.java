package bgu.spl.mics;

/**
 * A "Marker" interface extending {@link Message}. A micro-service that sends a
 * Request Message expects to receive a result of type {@code <R>} when a
 * micro-service that received the request completed handling it.
 * When sending a request, it will be received only by single subscriber - in a
 * Round-Robin fashion.
 */

public interface Request<R> extends Message {

}
