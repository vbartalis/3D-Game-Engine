package vbartalis.tools.coloure;

public class HsvColour {
	
	private float hue;
	private float saturation;
	private float value;

	public HsvColour(float hue, float saturation, float value){
		this.hue = hue;
		this.saturation = saturation;
		this.value = value;
	}

	public float getHue() {
		return hue;
	}

	public void setHue(float hue) {
		this.hue = hue;
	}

	public float getSaturation() {
		return saturation;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}
	
	public Colour convertToRgb(){
		return Colour.hsvToRgb(hue, saturation, value);
	}
	
}
