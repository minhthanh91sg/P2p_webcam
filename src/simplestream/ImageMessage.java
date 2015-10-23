package simplestream;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;

public class ImageMessage extends Message {

	private byte[] base64_image = null;

	public ImageMessage() {

	}

	public ImageMessage(byte[] base64_image) {
		this.base64_image = base64_image;
	}

	@Override
	public String Type() {
		return "image";
	}

	@SuppressWarnings("unchecked")
	@Override
	public String ToJSON() {
		JSONObject obj = new JSONObject();
		obj.put("type", Type());
		try {
			obj.put("data", new String(base64_image,
					"US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(-1);
		}

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

			this.setBase64_image(((String) obj.get("data")).getBytes());

		}

	}

	public byte[] getBase64_image() {
		return base64_image;
	}

	public void setBase64_image(byte[] base64_image) {
		this.base64_image = base64_image;
	}

}
