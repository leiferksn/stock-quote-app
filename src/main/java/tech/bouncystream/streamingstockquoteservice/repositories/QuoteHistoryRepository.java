package tech.bouncystream.streamingstockquoteservice.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import tech.bouncystream.streamingstockquoteservice.domain.QuoteHistory;

public interface QuoteHistoryRepository extends ReactiveMongoRepository<QuoteHistory, String> {
}
