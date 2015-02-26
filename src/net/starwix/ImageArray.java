package net.starwix;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

/**
 * @author starwix
 */
public class ImageArray {
	private int[][] data;
	private int type;
	
	public ImageArray(BufferedImage image, int type) {
		int w = image.getWidth();
		int h = image.getHeight();

		if (image.getType() != type) {
			BufferedImage temp = new BufferedImage(w, h, type);
			Graphics g = temp.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			image = temp;
		}
		this.type = type;
		
		DataBuffer t = image.getRaster().getDataBuffer(); // work fast
		data = new int[h][w];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				data[y][x] = t.getElem(y * w + x);
			}
		}
	}
	
	public ImageArray(BufferedImage image) throws AWTException {
		this(image, image.getType());
	}
	
	public int[][] getData() {
		return data;
	}
	
	public int getType() {
		return type;
	}
	
	public int getHeight() {
		return data.length;
	}
	
	public int getWidth() {
		return data[0].length;
	}
} 
