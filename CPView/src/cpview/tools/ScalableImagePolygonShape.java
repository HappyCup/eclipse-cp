package cpview.tools;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScalablePolygonShape;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;

public class ScalableImagePolygonShape extends ScalablePolygonShape {

	private Image _image;
	private boolean _imageVisible;

	public ScalableImagePolygonShape(Image image) {
		super();
		_image = image;
		_imageVisible = false;
	}

	@Override
	protected void fillShape(Graphics graphics) {
		super.fillShape(graphics);
		if (_image != null && _imageVisible == true) {
			graphics.pushState();
			Path path = new Path(Display.getCurrent());
			PointList points = getScaledPoints();
			for (int i = 0; i < points.size(); ++i) {
				Point p = points.getPoint(i);
				path.lineTo(p.x + getBounds().x, p.y + getBounds().y);
			}
			Point first = points.getFirstPoint();
			path.lineTo(first.x + getBounds().x, first.y + getBounds().y);
			graphics.setClip(path);
			Rectangle imgRect = new Rectangle(_image.getBounds());
			graphics.drawImage(_image, imgRect, this.getBounds());
			graphics.popState();
		}
	}

	public void setImage(Image image) {
		_image = image;
	}

	public Image getImage() {
		return _image;
	}

	public void setImageVisible(boolean flag) {
		_imageVisible = flag;
	}

}
