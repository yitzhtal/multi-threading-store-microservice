package bgu.spl.mics;

/**
 * a callback is a function designed to be called in the future in order to
 * handle a result of some operation (i.e., message received).
 */
public interface Callback<V> {

    public void call(V c);

}
