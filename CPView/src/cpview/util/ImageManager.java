package cpview.util;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class ImageManager {

	private static Display display = Display.getDefault();
	private static ImageRegistry _registry;

	static {
		_registry = new ImageRegistry();
		display.addListener(SWT.Dispose, new Listener() {

			@Override
			public void handleEvent(Event event) {
				_registry.dispose();
			}
		});
	}

	public static Image getIconImage(String string) {
		Image image = _registry.get(string);
		if (image == null) {
			Path path = new Path("icons/" + string);
			URL url = FileLocator
					.find(Platform.getBundle("CPView"), path, null);

			try {
				image = new Image(display, url.openStream());
			} catch (IOException e) {
				e.printStackTrace();

			}
			_registry.put(string, image);
		}

		return image;
	}

}
