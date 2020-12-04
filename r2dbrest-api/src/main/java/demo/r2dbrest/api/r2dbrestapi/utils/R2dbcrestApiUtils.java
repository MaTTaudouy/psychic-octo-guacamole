package demo.r2dbrest.api.r2dbrestapi.utils;

import org.apache.logging.log4j.util.Strings;
import org.springframework.web.reactive.function.server.ServerRequest;

public class R2dbcrestApiUtils {

	public static String extractTablename(ServerRequest request) {
		return extractTablename(request, 1);
	}

	public static String extractTablename(ServerRequest request, int index) {
		final String tablename = request.path().split("/")[index];
		if (Strings.isBlank(tablename)) {
			throw new IllegalArgumentException();
		}
		return tablename;
	}
}
