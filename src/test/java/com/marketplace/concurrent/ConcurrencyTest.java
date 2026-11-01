
package com.marketplace.concurrent;

import com.marketplace.observer.EventPublisher;
import com.marketplace.observer.MarketplaceEvent;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ConcurrencyTest {

    @Test
    void fourThreadsDeliverExactly4000Events() throws Exception {
        EventPublisher<MarketplaceEvent> publisher =
                new EventPublisher<>();

        AtomicInteger received = new AtomicInteger();

        publisher.subscribe(
                "STOCK_CHANGED",
                event -> received.incrementAndGet()
        );

        ConcurrentEventSimulator simulator =
                new ConcurrentEventSimulator(4, 1000);

        simulator.run(publisher);

        assertEquals(4000, received.get());
    }

    @Test
    void threeThreadsDeliverAllEvents() throws Exception {
        EventPublisher<MarketplaceEvent> publisher =
                new EventPublisher<>();

        AtomicInteger received = new AtomicInteger();

        publisher.subscribe(
                "STOCK_CHANGED",
                event -> received.incrementAndGet()
        );

        ConcurrentEventSimulator simulator =
                new ConcurrentEventSimulator(3, 100);

        simulator.run(publisher);

        assertEquals(300, received.get());
    }

    @Test
    void multipleSubscribersReceiveAllEvents() throws Exception {
        EventPublisher<MarketplaceEvent> publisher =
                new EventPublisher<>();

        AtomicInteger first = new AtomicInteger();
        AtomicInteger second = new AtomicInteger();

        publisher.subscribe(
                "STOCK_CHANGED",
                event -> first.incrementAndGet()
        );

        publisher.subscribe(
                "STOCK_CHANGED",
                event -> second.incrementAndGet()
        );

        ConcurrentEventSimulator simulator =
                new ConcurrentEventSimulator(4, 250);

        simulator.run(publisher);

        assertEquals(1000, first.get());
        assertEquals(1000, second.get());
    }

    @Test
    void invalidThreadCountIsRejected() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConcurrentEventSimulator(2, 100)
        );
    }
}
