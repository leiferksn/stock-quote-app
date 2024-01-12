package tech.bouncystream.streamingstockquoteservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Receiver;
import tech.bouncystream.streamingstockquoteservice.config.RabbitConfig;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteRunner implements CommandLineRunner {

    private final QuoteGeneratorService quoteGeneratorService;
    // private final QuoteHistoryService quoteHistoryService;
    private final QuoteMessageSender quoteMessageSender;
    private final Receiver receiver;

    @Override
    public void run(String... args) throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(25);

        quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100l))
                .take(25)
                .log("Got Quote, sending it to message: ")
                .flatMap(quoteMessageSender::sendQuoteMessage)
                .subscribe(result -> {
                    log.debug("Sent Quote Msg to Rabbit.");
                    countDownLatch.countDown();
                }, throwable -> {
                    log.error("Error sending quote message.");
                }, () -> log.debug("All done."));

        countDownLatch.await(1, TimeUnit.SECONDS);

//        quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100l))
//                .take(50)
//                .log("Got Quote: ")
//                .flatMap(quote -> quoteHistoryService.saveQuoteHistory(quote))
//                .subscribe(savedQuote -> {
//                    log.debug("Saved Quote: {}", savedQuote);
//                }, throwable -> {
//                    log.error("Error saving quote.");
//                }, () -> log.info("All done."));

        final var receivedCount = new AtomicInteger();
        receiver.consumeAutoAck(RabbitConfig.QUEUE)
                .log("Msg Receiver")
                .subscribe(msg -> {
                    log.debug("Received Message # {} - {}", receivedCount.incrementAndGet(), new String(msg.getBody()));
                }, throwable -> {
                    log.debug("Error Receiving", throwable);
                }, () -> {
                    log.debug("Complete.");
                });
    }
}
