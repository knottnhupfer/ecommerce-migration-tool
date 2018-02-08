package org.smooth.systems.ec.client.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectStringMapper extends ObjectMapper<String, String> {

	public ObjectStringMapper(String fileName) {
		super(fileName);
	}

	@Override
	protected String convertStringToKey(String key) {
		return key.trim();
	}

	@Override
	protected String convertStringToValue(String value) {
		return value.trim();
	}
}
