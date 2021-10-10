import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import ioinformarics.oss.jackson.module.jsonld.JsonldModule;

public class main {

	public static void main(String[] args) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.registerModule(new JsonldModule());

		Person person = new Person("http://example.com/person/1234", "Example Name");
		String personJsonLd = objectMapper.writeValueAsString(person);
		
		System.out.println(personJsonLd);

		
		
	}

}
