package tech.bouncystream.streamingstockquoteservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteRunner implements CommandLineRunner {

    private final QuoteGeneratorService quoteGeneratorService;
    private final QuoteHistoryService quoteHistoryService;

    @Override
    public void run(String... args) throws Exception {

        quoteGeneratorService.fetchQuoteStream(Duration.ofMillis(100l))
                .take(50)
                .log("Got Quote: ")
                .flatMap(quote -> quoteHistoryService.saveQuoteHistory(quote))
                .subscribe(savedQuote -> {
                    log.debug("Saved Quote: {}", savedQuote);
                }, throwable -> {
                    log.error("Error saving quote.");
                }, () -> log.info("All done."));
    }
}
