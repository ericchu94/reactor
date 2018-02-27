package net.ericchu.reactor;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.function.Consumer;

public class Reactor {

    public static void main(String[] args) throws IOException {
        // I want this to run start (even without subscribing)
        wrapper();

        // I also want this to run start
        wrapper().subscribe(it -> {
            System.out.println("Wrapper success");
        }, Throwable::printStackTrace);

        System.in.read();
    }

    // Some wrapper that converts a method with callback to reactive streams
    private static Mono<Void> wrapper() {
        return Mono.<Void>create(sink -> {
            start(success -> {
                if (success)
                    sink.success();
                else
                    sink.error(new Exception());
            });
        }).doOnSuccess(it -> {
            System.out.println("Started successfully");
        }).doOnError(it -> {
            System.out.println("Failed to start");
        });
    }

    // Method with callback that I want to wrap
    private static void start(Consumer<Boolean> fn) {
        fn.accept(true);
    }
}
