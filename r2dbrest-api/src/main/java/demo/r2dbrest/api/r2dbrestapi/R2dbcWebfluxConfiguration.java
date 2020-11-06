package demo.r2dbrest.api.r2dbrestapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;

import com.fasterxml.jackson.databind.ObjectMapper;

import demo.r2dbrest.api.r2dbrestapi.utils.JsonToMapConverter;
import demo.r2dbrest.api.r2dbrestapi.utils.MapToJsonConverter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;

@Configuration
public class R2dbcWebfluxConfiguration extends AbstractR2dbcConfiguration {

	@Autowired
	private ObjectMapper objectMapper;

	private DataSourceProperties datasourceProperties;

	@Override
	public ConnectionFactory connectionFactory() {
		return ConnectionFactories.get(datasourceProperties.getUrl());
	}

	@Bean
	@Override
	public R2dbcCustomConversions r2dbcCustomConversions() {
		final List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(new JsonToMapConverter(objectMapper));
		converters.add(new MapToJsonConverter(objectMapper));
		return new R2dbcCustomConversions(getStoreConversions(), converters);
	}

}
