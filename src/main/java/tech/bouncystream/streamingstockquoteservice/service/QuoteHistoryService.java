package tech.bouncystream.streamingstockquoteservice.service;

import reactor.core.publisher.Mono;
import tech.bouncystream.streamingstockquoteservice.domain.QuoteHistory;
import tech.bouncystream.streamingstockquoteservice.model.Quote;

public interface QuoteHistoryService {

    Mono<QuoteHistory> saveQuoteHistory(Quote quote);

}
