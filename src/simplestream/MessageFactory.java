package simplestream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MessageFactory {

	private static final JSONParser parser = new JSONParser();

	public Message FromJSON(String jst) {
		JSONObject obj;
		try {
			obj = (JSONObject) parser.parse(jst);
		} catch (ParseException e) {
			// alert the user
			e.printStackTrace();
			return null;
		}
		if (obj != null) {
			Message msg = null;
			if (obj.get("type").equals("startstream"))
				msg = new StartStreamMessage();
			else if (obj.get("type").equals("stopstream"))
				msg = new StopStreamMessage();
			else if (obj.get("type").equals("image"))
				msg = new ImageMessage();
			msg.FromJSON(jst);
			return msg;
		} else
			return null;
	}

}
