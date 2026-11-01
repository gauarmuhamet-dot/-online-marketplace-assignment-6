
package com.marketplace.observer;

public interface Observer<T> {
    void onEvent(T event);
}
