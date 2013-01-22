package cpview.tools;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import cpview.Activator;
import cpview.materials.Relationship;
import cpview.materials.Type;
import cpview.preferences.PreferenceConstants;
import cpview.services.DiagramDataService;
import cpview.services.IDiagramDataListener;
import cpview.util.ColorManager;

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
	private Map<Relationship, ConnectionTool> _connections;

	/**
	 * Creates a new Diagram Tool and initialises it with all Services, Subtools
	 * and GUI.
	 * 
	 * @param guiParent
	 *            Parent for the GUI of this Tool.
	 */
	public DiagramTool(Composite guiParent) {
		_gui = new DiagramGui(guiParent);
		_dataservice = Activator.getDefault().getDiagram();
		_classSubTools = new HashMap<Type, ClassTool>();
		_connections = new HashMap<Relationship, ConnectionTool>();

		initialiseClassSubtools();
		initialiseConnectionSubtools();
		addListenerToDataService();

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		initColors(store);
		createPreferenceListener(store);
		
	}

	private void initColors(IPreferenceStore store) {
		RGB rgb = PreferenceConverter.getColor(store,
				PreferenceConstants.BACKGROUND_COLOR);
		Color backgroundColor = ColorManager.getColorFromRGB(rgb);
		_gui.setBackgroundColor(backgroundColor);

	}

	/**
	 * Initialises all ClassTool. For each Class provided by the DataService one
	 * ClassTool is created.
	 */
	private void initialiseClassSubtools() {
		Collection<Type> models = _dataservice.getClasses();
		for (Type model : models) {
			if (_classSubTools.get(model) == null) {
				MenuManager menuManager = new MenuManager();
				menuManager.createContextMenu(_gui.getControl());
				ClassTool subtool = new ClassTool(model, menuManager);
				_gui.addChildFigure(subtool.getFigure());
				addListenersToSubtool(subtool);
				_classSubTools.put(model, subtool);
			}
		}
		}

	/**
	 * Initialises all ConnectionTools. For each Relationship provided by the
	 * DataService one ConnectionTool is created.
	 */
	private void initialiseConnectionSubtools() {
		Set<Relationship> relationships = _dataservice.getRelationships();
		for (Relationship rel : relationships) {
			if (_connections.get(rel)==null) {
				IFigure start = _classSubTools.get(rel.getStart()).getFigure();
				IFigure end = _classSubTools.get(rel.getEnd()).getFigure();
				ConnectionTool newConnectionTool = new ConnectionTool(rel,
						start, end);
				_connections.put(rel, newConnectionTool);
				_gui.addConnectionFigure(newConnectionTool.getFigure());
			}
		}
	}

	/**
	 * Add Listeners to all ClassSubtools. Listeners are notified when the
	 * location or status of the ClassTool changes.
	 */
	private void addListenersToSubtool(ClassTool subtool) {

		subtool.addSubtoolListener(new SubtoolListener() {
			@Override
			public void classChanged() {
				refreshAll();
			}

		});

	}

	private void addListenerToDataService() {
		_dataservice.addDataListener(new IDiagramDataListener() {

			@Override
			public void dataChanged() {
				initialiseClassSubtools();
				initialiseConnectionSubtools();
				for (DropTargetListener listener: _dataservice.getDropListeners()){
					_gui.addDropListener(listener);
				}
				refreshAll();
			}
		});

	}

	/**
	 * Refreshes visibility of all Subtools
	 */
	private void refreshAll() {
		_dataservice.refreshVisibilities();
		for (ClassTool c : _classSubTools.values()) {
			c.refreshVisibility();
		}
		for (ConnectionTool c : _connections.values()) {
			c.refreshVisibility();
		}
	}

	private void createPreferenceListener(final IPreferenceStore store) {
		store.addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				initColors(store);
			}
		});
	}
	
	private void removeAllClassSubtools(){
		for (ClassTool tool : _classSubTools.values()){
			tool.removeListeners();
			_gui.removeChildFigure(tool.getFigure());
		}
		_classSubTools = new HashMap<Type, ClassTool>();
	}
	
	private void removeAllConnections(){
		for (ConnectionTool tool: _connections.values()){
			_gui.removeConnectionFigure(tool.getFigure());
		}
		_connections = new HashMap<Relationship, ConnectionTool>();
	}

	public void clear() {
		removeAllClassSubtools();
		removeAllConnections();
		_dataservice.clear();
		
	}

}
