package simplestream;

import org.json.simple.parser.JSONParser;

public abstract class Message {

	protected static final JSONParser parser = new JSONParser();

	abstract public String Type();

	abstract public String ToJSON();

	abstract public void FromJSON(String jst);

}
