package cpview.tools;


import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.graphics.Color;
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
	private Layer _primaryLayer;
	private ConnectionLayer _connectionLayer;
	private Canvas _canvas;

	/**
	 * Creates a new DiagramGUI
	 * 
	 * @param parent
	 *            GUI-Parent for this Diagram
	 */
	public DiagramGui(Composite parent) {
		_canvas = new Canvas(parent, SWT.DOUBLE_BUFFERED);
		_canvas.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
				
		LightweightSystem lws = new LightweightSystem(_canvas);
		_primaryLayer = new Layer();
		_primaryLayer.setVisible(true);
		_primaryLayer.setOpaque(true);
		
		_connectionLayer = new ConnectionLayer();
		_connectionLayer.setConnectionRouter(new ShortestPathConnectionRouter(_primaryLayer));
		
		LayeredPane root = new LayeredPane();
		root.add(_primaryLayer);
		root.add(_connectionLayer);
		lws.setContents(root);
		}

	/**
	 * Adds a new Child to this GUI. As there is no Layout applied to this GUI
	 * each Child is responsible for its own location.
	 * 
	 * @param child
	 *            The Figure to be added to the Diagram
	 */
	public void addChildFigure(IFigure child) {
		_primaryLayer.add(child);
	}
	
	public void addConnectionFigure(Connection connection){
		_connectionLayer.add(connection);
	}
	
	public void addDropListener(DropTargetListener listener){
		//TODO Drop Listener
	}
	
	/**
	 * Returns the Control containing all elements of this GUI
	 * @return the Control
	 */
	public Control getControl(){
		return _canvas;
	}

	public void setBackgroundColor(Color color) {
		_primaryLayer.setBackgroundColor(color);	
	}

	public void removeChildFigure(IFigure figure) {
		_primaryLayer.remove(figure);
		
	}

	public void removeConnectionFigure(Connection figure) {
		_connectionLayer.remove(figure);
		
	}

}
