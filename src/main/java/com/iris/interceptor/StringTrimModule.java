package com.iris.interceptor;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * This Class responsible for trimming the String Fields
 * 
 * @author svishwakarma
 *
 */
@Component
public class StringTrimModule extends SimpleModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 287174008719744023L;

	public StringTrimModule() {
		addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 8567965457062608897L;

			@Override
			public String deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
				return jsonParser.getValueAsString().trim();
			}
		});
	}
}