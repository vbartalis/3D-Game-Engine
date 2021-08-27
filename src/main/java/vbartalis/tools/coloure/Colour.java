package vbartalis.tools.coloure;

import lombok.Getter;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.FloatBuffer;

public class Colour {
	
	private static final float GAMMA = 2.2f;

	@Getter
	private Vector3f rgb = new Vector3f();
	@Getter
	private float a = 1;
	
	public Colour() {
	}
	
	public Colour(Colour colour) {
		this.rgb.set(colour.rgb);
		this.a = colour.a;
	}

	public Colour(float r, float g, float b) {
		rgb.set(r, g, b);
	}

	public Colour(Vector3f colour) {
		rgb.set(colour);
	}

	public Colour(float r, float g, float b, float a) {
		rgb.set(r, g, b);
		this.a = a;
	}

	public Colour(int r, int g, int b) {
		rgb.set(r / 255f, g / 255f, b / 255f);
	}
	
	public byte[] asByteArray() {
		return new byte[] {(byte) (rgb.x * 255), (byte) (rgb.y * 255), (byte) (rgb.z * 255),  (byte) (a * 255)};
	}
	
	public byte[] asByteArrayNoAlpha() {
		return new byte[] {(byte) (rgb.x * 255), (byte) (rgb.y * 255), (byte) (rgb.z * 255)};
	}
	
	public Colour toLinearSpace() {
		rgb.x = (float) Math.pow(rgb.x, GAMMA);
		rgb.y = (float) Math.pow(rgb.y, GAMMA);
		rgb.z = (float) Math.pow(rgb.z, GAMMA);
		return this;
	}

	public FloatBuffer getAsFloatBuffer() {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
		buffer.put(new float[] { rgb.x, rgb.y, rgb.z, a });
		buffer.flip();
		return buffer;
	}

	public float getR() {
		return rgb.x;
	}

	public float getG() {
		return rgb.y;
	}

	public float getB() {
		return rgb.z;
	}

	public Colour duplicate() {
		return new Colour(this);
	}

	public void multiplyBy(Colour colour) {
		this.rgb.x *= colour.rgb.x;
		this.rgb.y *= colour.rgb.y;
		this.rgb.z *= colour.rgb.z;
	}

	public void setRGB(float r, float g, float b) {
		rgb.set(r, g, b);
	}

	public void setRGB(Vector3f colour) {
		rgb.set(colour);
	}

	public void setRGB(Colour colour) {
		this.rgb.set(colour.rgb);
		this.a = colour.a;
	}

	public void setRGB(float r, float g, float b, float a) {
		rgb.set(r, g, b);
		this.a = a;
	}

	public void setR(float r) {
		rgb.x = r;
	}

	public void setG(float g) {
		rgb.y = g;
	}

	public void setB(float b) {
		rgb.z = b;
	}

	public boolean isEqualTo(Colour colour) {
		return (rgb.x == colour.rgb.x && rgb.y == colour.rgb.y && rgb.z == colour.rgb.z && a == colour.a);
	}

	//normalize
	public Colour scale(float value) {
		rgb.normalize(value);
		return this;
	}

	public String toString() {
		return ("(" + rgb.x + ", " + rgb.y + ", " + rgb.z + ")");
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public float lengthSquared() {
		return rgb.lengthSquared();
	}
	
	public HsvColour convertToHsv() {
		float[] hsv = new float[3];
		Color.RGBtoHSB((int) (rgb.x * 255), (int) (rgb.y * 255), (int) (rgb.z * 255), hsv);
		return new HsvColour(hsv[0], hsv[1], hsv[2]);
	}

	public static Colour sub(Colour colLeft, Colour colRight, Colour dest) {
		if (dest == null) {
			Vector3f newCol = colLeft.rgb.sub(colRight.rgb, new Vector3f());
			Colour col = new Colour(newCol);
			newCol.
			Vec3Pool.release(newCol);
			return col;
		} else {
			Vector3f.sub(colLeft.rgb, colRight.rgb, dest.rgb);
			return dest;
		}
	}

	public static float calculateDifference(Colour colA, Colour colB) {
		return Colour.sub(colB, colA, null).length();
	}

	public static Colour hsvToRgb(float hue, float saturation, float value) {
		int h = (int) (hue * 6);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);
		switch (h) {
		case 0:
			return new Colour(value, t, p);
		case 1:
			return new Colour(q, value, p);
		case 2:
			return new Colour(p, value, t);
		case 3:
			return new Colour(p, q, value);
		case 4:
			return new Colour(t, p, value);
		case 5:
			return new Colour(value, p, q);
		default:
			return new Colour();
		}
	}

	public static Colour interpolate(Colour colour1, Colour colour2, float blend, Colour dest) {
		float colour1Weight = 1 - blend;
		float r = (colour1Weight * colour1.rgb.x) + (blend * colour2.rgb.x);
		float g = (colour1Weight * colour1.rgb.y) + (blend * colour2.rgb.y);
		float b = (colour1Weight * colour1.rgb.z) + (blend * colour2.rgb.z);
		if (dest == null) {
			return new Colour(r, g, b);
		} else {
			dest.setRGB(r, g, b);
			return dest;
		}
	}

	public static Colour add(Colour colour1, Colour colour2, Colour dest) {
		if (dest == null) {
			return new Colour(Vector3f.add(colour1.rgb, colour2.rgb, null));
		} else {
			Vector3f.add(colour1.rgb, colour2.rgb, dest.rgb);
			return dest;
		}
	}

}
