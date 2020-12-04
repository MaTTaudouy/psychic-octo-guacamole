package demo.r2dbrest.api.r2dbrestapi.api;

import static demo.r2dbrest.api.r2dbrestapi.utils.R2dbcrestApiUtils.extractTablename;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;

import demo.r2dbrest.api.r2dbrestapi.utils.R2dbcQueryExecutor;
import reactor.core.publisher.Mono;

public class R2dbrestCrudHandler {

	private R2dbcQueryExecutor r2dbcQueryExecutor;

	public R2dbrestCrudHandler(R2dbcQueryExecutor r2dbcQueryExecutor) {
		this.r2dbcQueryExecutor = r2dbcQueryExecutor;
	}

	public Mono<ServerResponse> getById(ServerRequest request) {
		final String tablename = extractTablename(request);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(this.r2dbcQueryExecutor.findById(tablename, request.pathVariable("id")), Map.class);
	}

	public Mono<ServerResponse> getAll(ServerRequest request) {
		final String tablename = extractTablename(request);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(this.r2dbcQueryExecutor.findAll(tablename), Map.class);
	}

	public Mono<ServerResponse> post(ServerRequest request) {
		final String tablename = extractTablename(request);
		final Mono<Map<String, Object>> dataToInsert = request.bodyToMono(Map.class);
		return this.r2dbcQueryExecutor.createTableIfNecessary(tablename)
				.then(this.r2dbcQueryExecutor.save(tablename, dataToInsert)).flatMap(
						valueCreated -> ServerResponse
								.created(UriComponentsBuilder.fromPath(buildCreatedURUI(tablename, valueCreated))
										.build().toUri())
								.contentType(MediaType.APPLICATION_JSON).bodyValue(valueCreated));
	}

	private String buildCreatedURUI(final String tablename, Map<String, Object> valueCreated) {
		return String.format("%s/%s", tablename, valueCreated.get("id"));
	}

	public Mono<ServerResponse> put(ServerRequest request) {
		final String tablename = extractTablename(request);
		final Mono<Map<String, Object>> data = request.bodyToMono(Map.class);
		return ServerResponse
				.ok().contentType(MediaType.APPLICATION_JSON).body(
						this.r2dbcQueryExecutor.createTableIfNecessary(tablename)
								.then(this.r2dbcQueryExecutor.update(tablename, request.pathVariable("id"), data)),
						Integer.class);
	}

}
