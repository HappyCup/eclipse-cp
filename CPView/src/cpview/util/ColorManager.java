package cpview.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Manages Color-Resources. All Colors created by this class will be disposed
 * when their Display is disposed and must not be disposed manually.
 * 
 * @author Alexander Brömmer
 * 
 */
public class ColorManager {

	private static Display display = Display.getDefault();
	private static Map<RGB, Color> colors = new HashMap<RGB, Color>();

	static {
		display.addListener(SWT.Dispose, new Listener() {

			@Override
			public void handleEvent(Event event) {
				disposeAll();
			}
		});
	}

	/**
	 * Returns the Color represented by this RGB. A new Color will only be
	 * created if no Color for this RGB was created before.
	 * 
	 * @param rgb
	 *            The RGB
	 * @return The Color
	 */
	public static Color getColorFromRGB(RGB rgb) {
		Color newColor = colors.get(rgb);
		if (newColor == null) {
			newColor = new Color(display, rgb);
			colors.put(rgb, newColor);
		}
		return newColor;
	}

	/**
	 * Diposes all Colors created by this Class
	 */
	private static void disposeAll() {
		for (Color c : colors.values()) {
			c.dispose();
		}
	}

}
