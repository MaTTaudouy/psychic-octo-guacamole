package demo.r2dbrest.api.r2dbrestapi.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.r2dbc.postgresql.codec.Json;

@ReadingConverter
public class JsonToMapConverter implements Converter<Json, Map<String, Object>> {

	private final ObjectMapper objectMapper;

	public JsonToMapConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Map<String, Object> convert(Json json) {
		try {
			return objectMapper.readValue(json.asString(), new TypeReference<>() {
			});
		} catch (IOException e) {
//			log.error("Problem while parsing JSON: {}", json, e);
		}
		return new HashMap<>();
	}

}
