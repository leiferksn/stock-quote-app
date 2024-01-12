package tech.bouncystream.streamingstockquoteservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.OutboundMessageResult;
import reactor.rabbitmq.QueueSpecification;
import reactor.rabbitmq.Sender;
import tech.bouncystream.streamingstockquoteservice.config.RabbitConfig;
import tech.bouncystream.streamingstockquoteservice.model.Quote;

import java.util.Queue;

import static tech.bouncystream.streamingstockquoteservice.config.RabbitConfig.QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuoteMessageSender {

    private final ObjectMapper objectMapper;
    private final Sender sender;

    @SneakyThrows
    public Mono<Void> sendQuoteMessage(Quote quote) {
        final var jsonBytes = objectMapper.writeValueAsBytes(quote);
        Flux<OutboundMessageResult> confirmations = sender.sendWithPublishConfirms(Flux.just(new OutboundMessage("", QUEUE, jsonBytes)));

        sender.declareQueue(QueueSpecification.queue(QUEUE))
                .thenMany(confirmations)
                .doOnError(e -> log.error("Send failed.", e))
                .subscribe(r -> {
                    if (r.isAck()) {
                        log.info("Message sent successfully {}", new String(r.getOutboundMessage().getBody()));
                    }
                });
        return Mono.empty();
    }

}
