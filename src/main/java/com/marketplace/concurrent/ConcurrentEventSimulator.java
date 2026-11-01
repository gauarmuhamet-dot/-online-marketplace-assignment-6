
package com.marketplace.concurrent;

import com.marketplace.observer.EventPublisher;
import com.marketplace.observer.MarketplaceEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class ConcurrentEventSimulator {

    private final int threadCount;
    private final int eventsPerThread;

    public ConcurrentEventSimulator(
            int threadCount,
            int eventsPerThread
    ) {
        if (threadCount < 3) {
            throw new IllegalArgumentException(
                    "At least 3 threads are required"
            );
        }

        if (eventsPerThread <= 0) {
            throw new IllegalArgumentException(
                    "Events per thread must be positive"
            );
        }

        this.threadCount = threadCount;
        this.eventsPerThread = eventsPerThread;
    }

    public void run(
            EventPublisher<MarketplaceEvent> publisher
    ) throws Exception {

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        List<Future<?>> tasks = new ArrayList<>();

        try {
            for (int i = 0; i < threadCount; i++) {
                final int producerId = i;

                Future<?> task = executor.submit(() -> {
                    for (int j = 0; j < eventsPerThread; j++) {

                        MarketplaceEvent event =
                                new MarketplaceEvent(
                                        MarketplaceEvent.Type.STOCK_CHANGED,
                                        "P" + producerId,
                                        BigDecimal.ZERO,
                                        j
                                );

                        publisher.notifyObservers(
                                "STOCK_CHANGED", event
                        );
                    }
                });

                tasks.add(task);
            }

            for (Future<?> task : tasks) {
                task.get();
            }

        } finally {
            executor.shutdown();

            if (!executor.awaitTermination(
                    30, TimeUnit.SECONDS)) {

                executor.shutdownNow();

                if (!executor.awaitTermination(
                        10, TimeUnit.SECONDS)) {
                    throw new IllegalStateException(
                            "Executor did not terminate"
                    );
                }
            }
        }
    }
}
