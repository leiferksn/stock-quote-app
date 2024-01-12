package tech.bouncystream.streamingstockquoteservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import tech.bouncystream.streamingstockquoteservice.model.Quote;

import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StreamingStockQuoteServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void contextLoads() {
	}

	@Test
	void testShouldFetchQuotes() {
		webTestClient.get()
				.uri("/quotes?size=20")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Quote.class)
				.hasSize(20)
				.consumeWith(quotes -> {
					assertThat(quotes.getResponseBody()).allSatisfy(q -> assertThat(q.getPrice()).isPositive());
					assertThat(quotes.getResponseBody()).hasSize(20);
				});
	}

	@Test
	void testShouldStreamQuotes() throws InterruptedException {
		final var countdownLatch = new CountDownLatch(10);

		webTestClient.get()
				.uri("/quotes")
				.accept(MediaType.APPLICATION_NDJSON)
				.exchange()
				.returnResult(Quote.class)
				.getResponseBody()
				.take(10)
				.subscribe(quote -> {
					assertThat(quote.getPrice()).isPositive();
					countdownLatch.countDown();
				});

		countdownLatch.await();
	}

}
