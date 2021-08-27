package vbartalis.tools.coloure;

import java.util.HashMap;
import java.util.Map;

public class ColourRepository {
	
	private Map<String, ColourPalette> palettes = new HashMap<>();
	
	public void addPalette(ColourPalette palette) {
		palettes.put(palette.id, palette);
	}
	
	/**Gets a duplicate of the colour at x,y in the specified colour palette.
	 * @param paletteId
	 * @param x
	 * @param y
	 * @return
	 */
	public Colour getColour(String paletteId, int x, int y) {
		return getColourPalette(paletteId).get(x, y).duplicate();
	}
	
	public ColourPalette getColourPalette(String paletteId) {
		return palettes.get(paletteId);
	}

}
