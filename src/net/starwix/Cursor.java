/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Stas Sviridov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.starwix;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

/**
 * 
 * @author starwix
 *
 */
public class Cursor {
	
	private Robot robot;
	private double delta;
	
	public Cursor(int rate, double speed) throws AWTException {
		robot = new Robot();
		robot.setAutoDelay(1000 / rate);
		this.delta = speed / rate;
	}
	
	public Cursor(double speed) throws AWTException {
		this(GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDisplayMode().getRefreshRate(),
				speed);
	}
	
	public void move(Point target) {
		Point current, prev = null;
		double notCoveredX = 0, notCoveredY = 0;
		
		while (!(current = MouseInfo.getPointerInfo().getLocation()).equals(target)) {
			if (!current.equals(prev)) {
				notCoveredX = notCoveredY = 0;
			}
			double x = Math.abs(target.x - current.x) - notCoveredX;
			double y = Math.abs(target.y - current.y) - notCoveredY;
			double d = distance(x, y);
			
			if (d <= delta) {
				prev = target;
			} else {
				double k = delta / d;
				
				double dx = x * k + notCoveredX;
				double dy = y * k + notCoveredY;
				
				prev = new Point(current.x + (current.x < target.x ? (int)dx : -(int)dx),
								 current.y + (current.y < target.y ? (int)dy : -(int)dy));
				
				notCoveredX = dx - (int)dx;
				notCoveredY = dy - (int)dy;
			}

			robot.mouseMove(prev.x, prev.y);
		}
	}
	
	private double distance(double a, double b) {
		return Math.sqrt(a * a + b * b);
	}
	
}
