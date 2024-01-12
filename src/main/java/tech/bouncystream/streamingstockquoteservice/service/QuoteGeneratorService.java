package tech.bouncystream.streamingstockquoteservice.service;

import reactor.core.publisher.Flux;
import tech.bouncystream.streamingstockquoteservice.model.Quote;

import java.time.Duration;

public interface QuoteGeneratorService {

    Flux<Quote> fetchQuoteStream(Duration period);

}
