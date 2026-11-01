
package com.marketplace.observer;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventPublisher<T> implements Subject<T> {

    private final ConcurrentHashMap<String, CopyOnWriteArraySet<Observer<T>>>
            listeners = new ConcurrentHashMap<>();

    @Override
    public void subscribe(String eventType, Observer<T> observer) {
        Objects.requireNonNull(eventType);
        Objects.requireNonNull(observer);

        listeners.computeIfAbsent(
                eventType,
                key -> new CopyOnWriteArraySet<>()
        ).add(observer);
    }

    @Override
    public void unsubscribe(String eventType, Observer<T> observer) {
        Objects.requireNonNull(eventType);
        Objects.requireNonNull(observer);

        listeners.computeIfPresent(eventType, (key, subscribers) -> {
            subscribers.remove(observer);
            return subscribers.isEmpty() ? null : subscribers;
        });
    }

    @Override
    public void notifyObservers(String eventType, T event) {
        Objects.requireNonNull(eventType);
        Objects.requireNonNull(event);

        CopyOnWriteArraySet<Observer<T>> subscribers =
                listeners.get(eventType);

        if (subscribers == null) {
            return;
        }

        for (Observer<T> observer : subscribers) {
            observer.onEvent(event);
        }
    }

    public int getSubscriberCount(String eventType) {
        CopyOnWriteArraySet<Observer<T>> subscribers =
                listeners.get(eventType);

        return subscribers == null ? 0 : subscribers.size();
    }
}
