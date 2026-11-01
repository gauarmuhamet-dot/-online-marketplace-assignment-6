
package com.marketplace.observer;

public interface Subject<T> {

    void subscribe(String eventType, Observer<T> observer);

    void unsubscribe(String eventType, Observer<T> observer);

    void notifyObservers(String eventType, T event);
}
