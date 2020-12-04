package demo.r2dbrest.api.r2dbrestapi.api;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static demo.r2dbrest.api.r2dbrestapi.utils.R2dbcrestApiUtils.*;
import demo.r2dbrest.api.r2dbrestapi.utils.R2dbcQueryExecutor;
import reactor.core.publisher.Mono;

public class R2dbrestEventHandler {

	private R2dbcQueryExecutor r2dbcQueryExecutor;

	public R2dbrestEventHandler(R2dbcQueryExecutor r2dbcQueryExecutor) {
		this.r2dbcQueryExecutor = r2dbcQueryExecutor;
	}

	public Mono<ServerResponse> eventById(ServerRequest request) {
		final String tablename = extractTablename(request, 2);
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(this.r2dbcQueryExecutor.findById(tablename, request.pathVariable("id")), Map.class);
	}
}
