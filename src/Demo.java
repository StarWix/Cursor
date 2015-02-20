import java.awt.AWTException;
import java.awt.Point;

import net.starwix.Mouse;


public class Demo {
	
	public static void main(String[] args) {
		try {
			Mouse mouse = new Mouse(100);
			mouse.move(new Point(500, 250), true);
			mouse.move(new Point(1024, 0));
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
