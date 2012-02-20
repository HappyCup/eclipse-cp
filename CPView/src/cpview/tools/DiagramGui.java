package cpview.tools;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * GUI for the DiagramTool
 * 
 * @author Alexander Brömmer
 * 
 */
public class DiagramGui {

	/**
	 * Main Figure of the Diagram. This Figure is parent to all other Figures in
	 * the Diagram.
	 */
	private IFigure _figure;
	private Canvas _canvas;

	/**
	 * Creates a new DiagramGUI
	 * 
	 * @param parent
	 *            GUI-Parent for this Diagram
	 */
	public DiagramGui(Composite parent) {
		_canvas = new Canvas(parent, SWT.NONE);
		_canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		LightweightSystem lws = new LightweightSystem(_canvas);
		_figure = new Figure();
		lws.setContents(_figure);
		}

	/**
	 * Adds a new Child to this GUI. As there is no Layout applied to this GUI
	 * each Child is responsible for its own location.
	 * 
	 * @param child
	 *            The Figure to be added to the Diagram
	 */
	public void addChildFigure(IFigure child) {
		_figure.add(child);
	}
	
	/**
	 * Returns the Control containing all elements of this GUI
	 * @return the Control
	 */
	public Control getControl(){
		return _canvas;
	}

}
