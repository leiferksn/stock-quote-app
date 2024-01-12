package tech.bouncystream.streamingstockquoteservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import tech.bouncystream.streamingstockquoteservice.domain.QuoteHistory;
import tech.bouncystream.streamingstockquoteservice.model.Quote;
import tech.bouncystream.streamingstockquoteservice.repositories.QuoteHistoryRepository;

import java.time.Instant;

@AllArgsConstructor
@Service
public class QuoteHistoryServiceImpl implements QuoteHistoryService {

    private final QuoteHistoryRepository quoteHistoryRepository;

    @Override
    public Mono<QuoteHistory> saveQuoteHistory(Quote quote) {
        return quoteHistoryRepository.save(QuoteHistory.builder()
                        .ticker(quote.getTicker())
                        .price(quote.getPrice())
                        .instant(Instant.now())
                .build());
    }
}
