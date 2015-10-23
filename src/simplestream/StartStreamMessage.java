package simplestream;

import org.json.simple.JSONObject;

public class StartStreamMessage extends Message {

	String format = "raw";
	private int width = 320;
	private int height = 240;

	public StartStreamMessage() {
		
	}

	
	public StartStreamMessage(int width, int height) {
		this.width = width;
		this.height = height;
	}


	@Override
	public String Type() {
		return "startstream";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", Type());
		obj.put("format", format);
		obj.put("width", width);
		obj.put("height", height);
		return obj.toJSONString();
	}

	@Override
	public void FromJSON(String jst) {
		JSONObject obj = null;

		try {
			obj = (JSONObject) parser.parse(jst);
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if (obj != null) {
			this.width = Integer.parseInt(obj.get("width").toString());
			this.height = Integer.parseInt(obj.get("height").toString());
		}

	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	

}
