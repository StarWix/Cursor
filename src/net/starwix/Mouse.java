package net.starwix;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

/**
 * Class allows control mouse.
 * 
 * @author starwix
 */
public class Mouse {
	
	private Robot robot;
	private double delta;
	
	public Mouse(int rate, double speed) throws AWTException {
		robot = new Robot();
		robot.setAutoDelay(1000 / rate);
		this.delta = speed / rate;
	}
	
	public Mouse(double speed) throws AWTException {
		this(GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice().getDisplayMode().getRefreshRate(),
				speed);
	}
	
	public void move(Point target, boolean click) {
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
		
		if (click) { 
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
	}
	
	public void move(Point target) {
		move(target, false);
	}
	
	private double distance(double a, double b) {
		return Math.sqrt(a * a + b * b);
	}
}
