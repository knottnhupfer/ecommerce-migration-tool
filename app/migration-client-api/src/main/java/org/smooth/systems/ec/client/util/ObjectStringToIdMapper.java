package org.smooth.systems.ec.client.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectStringToIdMapper extends ObjectMapper<String, Long> {

	public ObjectStringToIdMapper(String fileName) {
		super(fileName);
	}

	@Override
	protected String convertStringToKey(String key) {
		return key.trim();
	}

	@Override
	protected Long convertStringToValue(String value) {
		return Long.valueOf((value).trim());
	}
}
