package net.starwix;

import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

/**
 * @author starwix
 */
public class ImageArray {
	private int[][] data;
	private int type;
	
	private long[][] hashData;
	private long[][][] integralData;
	
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
	
	public Point findByHash(ImageArray image) {
		
		return null;
	}
	
	public Point findByIntegral(ImageArray image, int diff, int part) {
		int ny = getHeight() - image.getHeight();
		int nx = getWidth() - image.getWidth();
		Point p = null;
		long minDifference = Long.MAX_VALUE;
		for (int y = 0; y < ny; y++) {
			for (int x = 0; x < nx; x++) {
				long t = findDifference(x, y, image, 0, 0, image.getWidth(), image.getHeight(), part);
				if (t < minDifference) {
					minDifference = t;
					p = new Point(x, y);
				}
			}
		}
		return minDifference > diff ? null : p;
		
	}
	
	private long findDifference(int x, int y, ImageArray image, int ix, int iy, int w, int h, int part) {
		if (--part == 0) {
			long sum = 0;
			for (int i = 0; i < 4; i++) {
				sum += Math.abs(getIntegral(i, x, y, x + w - 1, y + h - 1) - image.getIntegral(i, ix, iy, ix + w - 1, iy + h - 1));
			}
			return sum;
		}
		

		if (h > w) {
			int nh = h / 2;
			return findDifference(x, y, image, ix, iy, w, nh, part) +
					findDifference(x, y + nh, image, ix, iy + nh, w, h - nh, part);
		} else {
			int nw = w / 2;
			return findDifference(x, y, image, ix, iy, nw, h, part) +
					findDifference(x + nw, y, image, ix + nw, iy, w - nw, h, part);
		}
	}
	
	private void findIntegral() {
		integralData = new long[4][getHeight()][getWidth()];

		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				int t = data[y][x];
				for (int i = 0; i < 4; i++) {
					integralData[i][y][x] = getIntegral(i, x, y - 1) + getIntegral(i, 0, y, x - 1, y) + (t & 0xFF);
					t >>= 8;
				}
			}
		}
	}
	
	private long getIntegral(int i, int ex, int ey) {
		if (integralData == null) {
			findIntegral();
		}
		if (ex < 0 || ey < 0) {
			return 0;
		}
		return integralData[i][ey][ex];
	}
	
	private long getIntegral(int i, int bx, int by, int ex, int ey) {
		if (integralData == null) {
			findIntegral();
		}
		if (bx > ex || by > ey || bx < 0 || by < 0) {
			return 0;
		}
		return integralData[i][ey][ex] - getIntegral(i, ex, by - 1) - getIntegral(i, bx - 1, ey) + getIntegral(i, bx - 1, by - 1);
	}
} 
