package demo.r2dbrest.api.r2dbrestapi.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.r2dbrest.api.r2dbrestapi.utils.R2dbcJsonConverter;
import demo.r2dbrest.api.r2dbrestapi.utils.R2dbcQueryExecutor;

@Configuration
@EnableWebFlux
public class RouterFunctionConfiguration implements WebFluxConfigurer {

	private static final String JSON_COLUMN_NAME = "data";

	@Autowired
	private DatabaseClient databaseClient;

	@Autowired
	private R2dbrestHandler r2dbrestHandler;

	@Bean
	public R2dbrestHandler r2dbrestHandler(R2dbcQueryExecutor r2dbcQueryExecutor) {
		return new R2dbrestHandler(r2dbcQueryExecutor);
	}

	@Bean
	R2dbcQueryExecutor r2dbcQueryExecutor(R2dbcEntityTemplate r2dbcTemplate, R2dbcJsonConverter r2dbcJsonConverter) {
		return new R2dbcQueryExecutor(r2dbcTemplate, r2dbcJsonConverter);
	}

	@Bean
	public R2dbcEntityTemplate r2dbcTemplate() {
		return new R2dbcEntityTemplate(databaseClient);
	}

	@Bean
	public R2dbcJsonConverter r2dbcJsonConverter(ObjectMapper objectMapper) {
		return new R2dbcJsonConverter(JSON_COLUMN_NAME, objectMapper);
	}

	@Bean
	public RouterFunction<?> requestMapping() {
		return RouterFunctions.route()
				.GET("/*/{id}", RequestPredicates.accept(MediaType.TEXT_PLAIN), r2dbrestHandler::getById)
				.GET("/*", RequestPredicates.accept(MediaType.TEXT_PLAIN), r2dbrestHandler::getAll)
				.POST("/*", RequestPredicates.accept(MediaType.APPLICATION_JSON), r2dbrestHandler::post).build();
	}

//	@Bean
//	public ObjectMapper jsonObjectMapper(R2dbcJsonConverter r2dbcJsonConverter) {
//		SimpleModule module = new SimpleModule();
//		module.setSerializers(new R2dbcTypeJsonSerializer(r2dbcJsonConverter));
//		return Jackson2ObjectMapperBuilder.json().modules(module).build();
//	}
}
