package demo.r2dbrest.api.r2dbrestapi.utils;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.r2dbc.postgresql.codec.Json;

@WritingConverter
public class MapToJsonConverter implements Converter<Map<String, Object>, Json> {

	private final ObjectMapper objectMapper;

	public MapToJsonConverter(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public Json convert(Map<String, Object> source) {
		try {
			return Json.of(objectMapper.writeValueAsString(source));
		} catch (JsonProcessingException e) {
//			log.error("Error occurred while serializing map to JSON: {}", source, e);
		}
		return Json.of("");
	}

}
