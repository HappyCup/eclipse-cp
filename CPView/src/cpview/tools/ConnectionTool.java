package cpview.tools;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import cpview.materials.Relationship;
import cpview.values.RelationshipType;

/**
 * Draws a line between two Figures representing a Relationship between two
 * Classes. The line is always directed at the center of the Class-Figures and
 * is decorated with arrows depending on the type of Relationship.
 * 
 * @author Alexander Brömmer
 * 
 */
public class ConnectionTool {

	/**
	 * The represented Relationship
	 */
	private Relationship _relationship;

	/**
	 * Anchor at the dependend class
	 */
	private ChopboxAnchor _startAnchor;

	/**
	 * Anchor at the other class
	 */
	private ChopboxAnchor _endAnchor;

	/**
	 * The Connection
	 */
	private PolylineConnection _connection;

	/**
	 * Creates a new ConnectionTool
	 * 
	 * @param rel
	 *            The represented Relationship
	 * @param start
	 *            Figure where the connection starts
	 * @param end
	 *            Figure where the connection ends
	 */
	public ConnectionTool(Relationship rel, IFigure start, IFigure end) {
		_relationship = rel;
		_startAnchor = new ChopboxAnchor(start);
		_endAnchor = new ChopboxAnchor(end);
		initConnection();
	}
	
	/**
	 * Initialises Connection with attributes defined in Relationship
	 */
	private void initConnection(){
		_connection = new PolylineConnection(){
			@Override
			public boolean containsPoint(int x, int y) { //Connections are not clickable in this diagram
				return false;
			}
		};
		_connection.setSourceAnchor(_startAnchor);
		_connection.setTargetAnchor(_endAnchor);
		_connection.setVisible(_relationship.isVisible());
		PolygonDecoration decoration = new PolygonDecoration();
		if (_relationship.getType() == RelationshipType.INHERITENCE){
			Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
			decoration.setBackgroundColor(white);
		}else if (_relationship.getType() == RelationshipType.DEPENDENCY){
			_connection.setLineStyle(SWT.LINE_CUSTOM); 	//SWT.LINE_DASHED makes Dashes to small
			float[] dash = {4.0f};
			_connection.setLineDash(dash);
			_connection.setLineDashOffset(4.0f);
		}
		_connection.setTargetDecoration(decoration);
	}

	/**
	 * Sets visibility of the connection to visibility of represented
	 * Relationship
	 */
	public void refreshVisibility() {
		_connection.setVisible(_relationship.isVisible());
	}

	/**
	 * @return The GUI-Figure
	 */
	public Connection getFigure() {
		return _connection;
	}

}
