import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldId;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldLink;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldNamespace;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldProperty;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldResource;
import ioinformarics.oss.jackson.module.jsonld.annotation.JsonldType;

@JsonldResource
@JsonldNamespace(name = "s", uri = "http://schema.org/")
@JsonldType("s:Person")
@JsonldLink(rel = "s:knows", name = "knows", href = "http://example.com/person/2345")
public class Person {
	public Person(String string, String string2) {
		// TODO Auto-generated constructor stub
	}
	
	@JsonldId
    private String id;
    @JsonldProperty("s:name")
    private String name;

    // constructor, getters, setters
}
