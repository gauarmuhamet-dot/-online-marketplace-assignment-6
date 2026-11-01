
package com.marketplace.observer;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {

    private MarketplaceEvent createEvent() {
        return new MarketplaceEvent(
                MarketplaceEvent.Type.STOCK_CHANGED,
                "P001",
                BigDecimal.ZERO,
                10
        );
    }

    @Test
    void subscriberReceivesEvent() {
        EventPublisher<MarketplaceEvent> publisher =
                new EventPublisher<>();

        AtomicInteger counter = new AtomicInteger();

        publisher.subscribe(
                "STOCK_CHANGED",
                event -> counter.incrementAndGet()
        );

        publisher.notifyObservers(
                "STOCK_CHANGED", createEvent()
        );

        assertEquals(1, counter.get());
    }

    @Test
    void differentEventTypeIsIgnored() {
        EventPublisher<MarketplaceEvent> publisher =
                new EventPublisher<>();

        AtomicInteger counter = new AtomicInteger();

        publisher.subscribe(
                "PRICE_CHANGED",
                event -> counter.incrementAndGet()
        );

        publisher.notifyObservers(
                "STOCK_CHANGED", createEvent()
        );

        assertEquals(0, counter.get());
    }

    @Test
    void unsubscribeStopsNotifications() {
        EventPublisher<MarketplaceEvent> publisher =
                new EventPublisher<>();

        AtomicInteger counter = new AtomicInteger();

        Observer<MarketplaceEvent> listener =
                event -> counter.incrementAndGet();

        publisher.subscribe("STOCK_CHANGED", listener);

        publisher.notifyObservers(
                "STOCK_CHANGED", createEvent()
        );

        publisher.unsubscribe("STOCK_CHANGED", listener);

        publisher.notifyObservers(
                "STOCK_CHANGED", createEvent()
        );

        assertEquals(1, counter.get());
    }

    @Test
    void listenerCanUnsubscribeDuringCallback() {
        EventPublisher<MarketplaceEvent> publisher =
                new EventPublisher<>();

        AtomicInteger counter = new AtomicInteger();

        @SuppressWarnings("unchecked")
        Observer<MarketplaceEvent>[] holder = new Observer[1];

        holder[0] = event -> {
            counter.incrementAndGet();
            publisher.unsubscribe("STOCK_CHANGED", holder[0]);
        };

        publisher.subscribe("STOCK_CHANGED", holder[0]);

        publisher.notifyObservers(
                "STOCK_CHANGED", createEvent()
        );

        publisher.notifyObservers(
                "STOCK_CHANGED", createEvent()
        );

        assertEquals(1, counter.get());
    }

    @Test
    void duplicateSubscriptionIsNotAdded() {
        EventPublisher<MarketplaceEvent> publisher =
                new EventPublisher<>();

        Observer<MarketplaceEvent> listener = event -> {};

        publisher.subscribe("STOCK_CHANGED", listener);
        publisher.subscribe("STOCK_CHANGED", listener);

        assertEquals(
                1,
                publisher.getSubscriberCount("STOCK_CHANGED")
        );
    }
}
