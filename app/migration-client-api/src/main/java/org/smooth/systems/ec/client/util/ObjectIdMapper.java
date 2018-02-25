package org.smooth.systems.ec.client.util;

public class ObjectIdMapper extends ObjectMapper<Long, Long> {

	public ObjectIdMapper(String fileName) {
		super(fileName);
	}

	@Override
	protected Long convertStringToKey(String key) {
		return Long.valueOf((key).trim());
	}

	@Override
	protected Long convertStringToValue(String value) {
		return Long.valueOf((value).trim());
	}
}
