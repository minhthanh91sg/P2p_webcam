package simplestream;

import org.json.simple.JSONObject;

public class StopStreamMessage extends Message {

	StopStreamMessage() {

	}

	@Override
	public String Type() {

		return "stopstream";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", Type());
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String jst) {
		// TODO Auto-generated method stub

	}

}
