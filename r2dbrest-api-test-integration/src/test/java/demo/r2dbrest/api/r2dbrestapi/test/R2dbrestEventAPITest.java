package demo.r2dbrest.api.r2dbrestapi.test;

import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.AssertionErrors;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.github.javafaker.Faker;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DirtiesContext
@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class R2dbrestEventAPITest {

	private static final String HTTP_LOCALHOST_URL = "http://localhost:8080/";

	private WebClient webClient;

	private final String cutomRandomObjectTablenameAndApiPath = "randomname";

	@BeforeAll
	public void setUp() {
		webClient = WebClient.builder().defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).baseUrl(HTTP_LOCALHOST_URL)
				.build();
	}

	final Faker faker = new Faker(Locale.getDefault());

//	@Test
//	void testCreateObjectIsReturned() {
//		final CustomRandomObject aRandomObject = buildRandomObject();
//		Mono<ClientResponse> exchange = webClient.post().uri(cutomRandomObjectTablenameAndApiPath)
//				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(aRandomObject)).exchange();
//		StepVerifier.create(exchange).assertNext(response -> AssertionErrors.assertTrue("Status code must be created",
//				HttpStatus.CREATED.equals(response.statusCode()))).expectComplete().verify();
//	}

	@Test
	void testCreateObjectIsAccessibleByAGet() throws InterruptedException {

		// Create initial object and post
		final CustomRandomObject initialObject = buildRandomObject();
		Mono<ClientResponse> exchange = post(initialObject);

		// Check post response status
		StepVerifier.create(exchange).assertNext(postResponse -> {
			AssertionErrors.assertTrue("Status code must be created and not " + postResponse.statusCode(),
					HttpStatus.CREATED.equals(postResponse.statusCode()));

		}).verifyComplete();

		// Check return from post
		StepVerifier.create(exchange.flatMap(r -> r.bodyToMono(CustomRandomObject.class))).assertNext(createObject -> {
			initialObject.setId(createObject.getId());
			AssertionErrors.assertEquals("CustomRandomObjects are not equals", initialObject, createObject);
		}).verifyComplete();

		// Prepare get exchange /randomname/{id} for same object
		final var getExchange = exchange.flatMap(postResponse -> {
			final String location = extractLocation(postResponse);
			return WebClient.builder().baseUrl(HTTP_LOCALHOST_URL).build().get().uri("/" + location).exchange();
		});

		// Check get response status
		StepVerifier.create(getExchange).assertNext(getResponse -> {
			AssertionErrors.assertTrue("Status code must be ok and not " + getResponse.statusCode(),
					HttpStatus.OK.equals(getResponse.statusCode()));
		}).verifyComplete();

		// Check get response body is the same
		StepVerifier.create(getExchange.flatMap(r -> r.bodyToMono(CustomRandomObject.class))).assertNext(getObject -> {
			initialObject.setId(getObject.getId());// ??
			AssertionErrors.assertEquals("CustomRandomObjects are not equals", initialObject, getObject);
		}).verifyComplete();
	}

	private String extractLocation(ClientResponse postResponse) {
		return postResponse.headers().header("location").get(0);
	}

	private Mono<ClientResponse> post(final CustomRandomObject aRandomObject) {
		Mono<ClientResponse> exchange = webClient.post().uri(cutomRandomObjectTablenameAndApiPath)
				.contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(aRandomObject)).exchange();
		return exchange;
	}

	private CustomRandomObject buildRandomObject() {
		final CustomRandomObject aRandomObject = new CustomRandomObject(null, faker.gameOfThrones().character(),
				faker.number().randomDigitNotZero(),
				new City(faker.gameOfThrones().city(), faker.number().randomDigitNotZero()));
		return aRandomObject;
	}

}
