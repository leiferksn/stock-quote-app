package tech.bouncystream.streamingstockquoteservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.bouncystream.streamingstockquoteservice.model.Quote;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class QuoteGeneratorServiceTest {

    private QuoteGeneratorService service;


    @BeforeEach
    void setUp() {
        service = new QuoteGeneratorServiceImpl();
    }

    @Test
    void shouldFetchQuoteStream() throws InterruptedException {
        final var quotesFlux = service.fetchQuoteStream(Duration.of(1000, ChronoUnit.MILLIS));
        Consumer<Quote> quoteConsumer = System.out::println;
        Consumer<Throwable> throwableConsumer = e -> System.out.println(e.getMessage());

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Runnable done = () ->  countDownLatch.countDown();

        quotesFlux.take(30).subscribe(quoteConsumer, throwableConsumer, done);

        countDownLatch.await();
    }
}
