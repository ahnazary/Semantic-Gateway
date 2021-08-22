import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException{
		
		SurfaceForm Test = new SurfaceForm("/home/amirhossein/Documents/GitHub/semantic-broker/Broker/input","/home/amirhossein/Documents/GitHub/semantic-broker/Broker/saref.ttl");
		
		Test.exactQuery();
		Test.morphemesQuery();
		
	}	
}
