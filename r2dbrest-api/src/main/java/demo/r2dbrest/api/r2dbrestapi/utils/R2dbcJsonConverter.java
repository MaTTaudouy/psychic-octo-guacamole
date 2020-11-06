package demo.r2dbrest.api.r2dbrestapi.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.r2dbc.postgresql.codec.Json;

public class R2dbcJsonConverter {

	private final String jsonColumnname;

	private final ObjectMapper objectMapper;

	public R2dbcJsonConverter(@NonNull String jsonColumnname, ObjectMapper objectMapper) {
		Assert.notNull(jsonColumnname, "Json column name must be provided");
		this.jsonColumnname = jsonColumnname;
		this.objectMapper = objectMapper;
	}

	public String jsonToString(Map<String, Object> value) {
		return getJson(value).asString();
	}

	private Json getJson(Map<String, Object> value) {
		return (Json) value.get(jsonColumnname);
	}

	public String getJsonColumnname() {
		return jsonColumnname;
	}

	public Json getStringValue(Map<String, Object> v) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> convertColumn(Map<String, Object> value) {
		final String jsonValue = jsonToString(value);
		try {
			return objectMapper.readValue(jsonValue, new TypeReference<>() {
			});
		} catch (IOException e) {
			System.err.println(String.format("Problem while parsing JSON %s : %s", jsonValue, e));
		}
		return Collections.emptyMap();
	}
}
