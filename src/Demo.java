import java.awt.AWTException;
import java.awt.Point;

import net.starwix.Cursor;


public class Demo {
	
	public static void main(String[] args) {
		try {
			Cursor cursor = new Cursor(5000);
			cursor.move(new Point(500, 250));
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
