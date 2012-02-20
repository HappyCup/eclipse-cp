package cpview.tools;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.draw2d.ActionEvent;
import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Menu;

import cpview.Activator;
import cpview.materials.Type;
import cpview.preferences.PreferenceConstants;
import cpview.util.ColorManager;
import cpview.values.CPMode;

/**
 * Represents a Class in the Class-Diagram as a colored Rectangle with the
 * ClassName and two Buttons to switch between Consumer- and Producer-View. It
 * manages its location itself and can be dragged to any point in its parent.
 * 
 * @author Alexander Brömmer
 * 
 */
public class ClassTool {

	/**
	 * The Class (or Type) represented by this tool
	 */
	private Type _class;

	/**
	 * GUI-class for this tool.
	 */
	private ClassGui _gui;

	/**
	 * Set of Observers
	 */
	private Set<SubtoolListener> _listeners;

	private Color _consumeColor;
	private Color _produceColor;

	/**
	 * The pop-up Menu for this Class
	 */
	private MenuManager _menuManager;

	/**
	 * Creates a new ClassTool for the given Type and initialises its GUI.
	 * 
	 * @param type
	 *            The Class (or Type) this tool represents.
	 */
	public ClassTool(Type type, MenuManager menuManager) {
		_class = type;
		_gui = new ClassGui();
		_gui.setDisplayedName(_class.getClassName());
		_gui.addCombinedMouseListener(new MouseDragListener());
		_gui.setVisible(_class.isVisible());
		_gui.setPinVisible(_class.isPinned());
		_listeners = new HashSet<SubtoolListener>();
		_menuManager = menuManager;
		createButtonListeners();
		createPopupListener();
		contributeToMenu();

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		initColors(store);
		createPreferenceListener(store);
	}

	private void contributeToMenu() {
		_menuManager.add(new Action("Pin", IAction.AS_CHECK_BOX) {
			@Override
			public boolean isChecked() {
				return _class.isPinned();
			}
			
			@Override
			public void run() {
				_class.setPinned(!_class.isPinned());
				_gui.setPinVisible(_class.isPinned());
				notifyObservers();
			}

		});

	}

	private void createPopupListener() {
		_gui.addMouseListener(new MouseListener.Stub() {

			@Override
			public void mousePressed(MouseEvent event) {
				Menu menu = _menuManager.getMenu();
				// right-click = button3 (not sure if platform-independent)
				if (event.button == 3 && menu != null && !menu.isDisposed()) {
					menu.setVisible(true);
					//event.consume();
				}
			}
		});

	}

	private void initColors(IPreferenceStore store) {
		RGB rgb = PreferenceConverter.getColor(store,
				PreferenceConstants.CONSUME_COLOR);
		_consumeColor = ColorManager.getColorFromRGB(rgb);
		rgb = PreferenceConverter.getColor(store,
				PreferenceConstants.PRODUCE_COLOR);
		_produceColor = ColorManager.getColorFromRGB(rgb);
		_gui.setConsumeButtonColor(_consumeColor);
		_gui.setProduceButtonColor(_produceColor);
		if (_class.getMode() == CPMode.CONSUME)
			_gui.setColor(_consumeColor);
		else
			_gui.setColor(_produceColor);
	}

	/**
	 * Creates and adds Listeners to the Buttons on the GUI.
	 */
	private void createButtonListeners() {
		_gui.addConsumeButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				_class.setMode(CPMode.CONSUME);
				_gui.setColor(_consumeColor);
				notifyObservers();
			}
		});
		_gui.addProduceButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				_class.setMode(CPMode.PRODUCE);
				_gui.setColor(_produceColor);
				notifyObservers();
			}
		});

	}

	/**
	 * @return The GUI-Figure of this tool.
	 */
	public IFigure getFigure() {
		return _gui.getFigure();
	}

	/**
	 * Moves the GUI-Figure inside its parent.
	 * 
	 * @param offset
	 *            The offset by which the Figure should be moved.
	 */
	private void moveFigure(Dimension offset) {
		_gui.move(offset);
		notifyObservers();
	}

	/**
	 * Notifies all registred SubtoolListeners.
	 */
	private void notifyObservers() {
		for (SubtoolListener listener : _listeners) {
			listener.classChanged();
		}
	}

	/**
	 * Registers a new SubtoolListener which is notified when this tool moves or
	 * changes its status.
	 * 
	 * @param subtoolListener
	 *            The listener to be registered
	 */
	public void addSubtoolListener(SubtoolListener subtoolListener) {
		_listeners.add(subtoolListener);
	}

	/**
	 * Listens to MouseDrags and moves the Figure on Drag.
	 * 
	 * @author Alexander Brömmer
	 * 
	 */
	private class MouseDragListener extends MouseMotionListener.Stub implements
			CombinedMouseListener {

		private Point _location;

		@Override
		public void mousePressed(MouseEvent event) {
			if (event.button == 1) { // If Mouse 1 was pressed
				_location = event.getLocation();
				_gui.bringToFront();
				//Memo to myself : NEVER CONSUME DRAW2D-EVENTS (leads to very strange behavior)
			}

		}

		@Override
		public void mouseReleased(MouseEvent event) {
			if (event.button == 1) { // If Mouse 1 was released
				_location = null;
			}
		}

		@Override
		public void mouseDoubleClicked(MouseEvent me) {
			// nothing
		}

		@Override
		public void mouseDragged(MouseEvent event) {
			if (_location == null)
				return;
			Point newLocation = event.getLocation();
			if (newLocation == null)
				return;
			Dimension offset = newLocation.getDifference(_location);
			_location = newLocation;
			moveFigure(offset);
		}

	}

	/**
	 * Sets visibility of GUI to visibility of represented Type (or Class)
	 */
	public void refreshVisibility() {
		_gui.setVisible(_class.isVisible());
	}

	private void createPreferenceListener(final IPreferenceStore store) {
		store.addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				initColors(store);
			}
		});
	}

}
