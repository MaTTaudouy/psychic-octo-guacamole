package demo.r2dbrest.api.r2dbrestapi.api;

import java.util.Map;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import demo.r2dbrest.api.r2dbrestapi.utils.R2dbcQueryExecutor;
import reactor.core.publisher.Mono;

public class R2dbrestHandler {

	private R2dbcQueryExecutor r2dbcQueryExecutor;

	public R2dbrestHandler(R2dbcQueryExecutor r2dbcQueryExecutor) {
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
		final Mono<Map<String, Object>> resource = request.bodyToMono(Map.class);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(this.r2dbcQueryExecutor
				.createTableIfNecessary(tablename).then(this.r2dbcQueryExecutor.save(tablename, resource)),
				String.class);
	}

	private String extractTablename(ServerRequest request) {
		final String tablename = request.path().split("/")[1];
		if (Strings.isBlank(tablename)) {
			throw new IllegalArgumentException();
		}
		return tablename;
	}

}
