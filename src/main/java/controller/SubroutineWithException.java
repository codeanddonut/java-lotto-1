package controller;

@FunctionalInterface
public interface SubroutineWithException<T, E extends Exception> {
    T get() throws E;
}