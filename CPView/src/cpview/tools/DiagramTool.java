package cpview.tools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Composite;

import cpview.materials.Relationship;
import cpview.materials.Type;
import cpview.services.DiagramDataService;

/**
 * This tool creates and manages all Subtools, Services and GUI for the Plugin.
 * 
 * @author Alexander Brömmer
 * 
 */
public class DiagramTool {

	/**
	 * GUI for this tool.
	 */
	private DiagramGui _gui;

	/**
	 * DataService to get all materials represented in this tool.
	 */
	private DiagramDataService _dataservice;

	/**
	 * Maps all Types to the subtool representing it.
	 */
	private Map<Type, ClassTool> _classSubTools;

	/**
	 * Set of all subtools representing Relationships.
	 */
	private Set<ConnectionTool> _connections;

	/**
	 * Creates a new Diagram Tool and initialises it with all Services, Subtools
	 * and GUI.
	 * 
	 * @param guiParent
	 *            Parent for the GUI of this Tool.
	 */
	public DiagramTool(Composite guiParent) {
		_gui = new DiagramGui(guiParent);
		_dataservice = new DiagramDataService();
		_classSubTools = new HashMap<Type, ClassTool>();
		_connections = new HashSet<ConnectionTool>();

		
		initialiseClassSubtools();
		initialiseConnectionSubtools();
		addListenersToSubtools();
	}

	/**
	 * Initialises all ClassTool. For each Class provided by the DataService one
	 * ClassTool is created.
	 */
	private void initialiseClassSubtools() {
		Set<Type> models = _dataservice.getClasses();
		for (Type model : models) {
			MenuManager menuManager = new MenuManager();
			menuManager.createContextMenu(_gui.getControl());
			ClassTool subtool = new ClassTool(model, menuManager);
			_gui.addChildFigure(subtool.getFigure());
			_classSubTools.put(model, subtool);
		}
	}

	/**
	 * Initialises all ConnectionTools. For each Relationship provided by the
	 * DataService one ConnectionTool is created.
	 */
	private void initialiseConnectionSubtools() {
		Set<Relationship> relationships = _dataservice.getRelationships();
		for (Relationship rel : relationships) {
			IFigure start = _classSubTools.get(rel.getStart()).getFigure();
			IFigure end = _classSubTools.get(rel.getEnd()).getFigure();
			ConnectionTool newConnectionTool = new ConnectionTool(rel, start,
					end);
			_connections.add(newConnectionTool);
			_gui.addChildFigure(newConnectionTool.getFigure());
		}
	}

	/**
	 * Add Listeners to all ClassSubtools. Listeners are notified when the
	 * location or status of the ClassTool changes.
	 */
	private void addListenersToSubtools() {
		for (ClassTool subtool : _classSubTools.values()) {
			subtool.addSubtoolListener(new SubtoolListener() {
				@Override
				public void classChanged() {
					refreshAll();
				}
			});
		}
	}

	/**
	 * Refreshes visibility of all Subtools
	 */
	private void refreshAll() {
		_dataservice.refreshVisibilities();
		for (ClassTool c : _classSubTools.values()) {
			c.refreshVisibility();
		}
		for (ConnectionTool c : _connections) {
			c.refreshVisibility();
		}
	}


}
