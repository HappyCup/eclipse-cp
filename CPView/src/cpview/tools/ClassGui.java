package cpview.tools;

import org.eclipse.draw2d.ActionListener;
import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.SchemeBorder.Scheme;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

/**
 * GUI-Class for the ClassTool
 * 
 * @author Alexander Brömmer
 * 
 */
class ClassGui {

	/**
	 * Figure representing the Class
	 */
	private IFigure _figure;

	/**
	 * Label with the Name of the Class.
	 */
	private Label _namelabel;

	/**
	 * Button to activate Consumer-View
	 */
	private Button _consumeButton;

	/**
	 * Button to activate Producer-View
	 */
	private Button _produceButton;
	
	/**
	 * The pin-symbol indicating this Class is pinned
	 */
	private Ellipse _pinSymbol;

	public static int PRODUCE_COLOR = SWT.COLOR_RED;

	/**
	 * Creates new ClassGUI and initializes all Components.
	 */
	public ClassGui() {
		LayoutManager layout = new BorderLayout();
		_figure = new Panel();
		_figure.setLayoutManager(layout);

		Font defaultFont = Display.getCurrent().getSystemFont();

		_namelabel = new Label();
		_namelabel.setFont(defaultFont);

		_consumeButton = new Button("C");
		_consumeButton.setFont(defaultFont);

		_produceButton = new Button("P");
		_produceButton.setFont(defaultFont);
		
		_pinSymbol = new Ellipse();
		_pinSymbol.setSize(16, 16);
		_pinSymbol.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));

		Figure buttonFigure = new Figure();
		buttonFigure.setLayoutManager(new GridLayout(2, true));
		buttonFigure.add(_consumeButton, new GridData(SWT.FILL, SWT.FILL, true,
				true));
		buttonFigure.add(_produceButton, new GridData(SWT.FILL, SWT.FILL, true,
				true));

		_figure.add(_namelabel, BorderLayout.TOP);
		_figure.add(buttonFigure, BorderLayout.BOTTOM);
		_figure.add(_pinSymbol, BorderLayout.LEFT);

		_figure.setMinimumSize(new Dimension(100, 50));
		_figure.setBorder(createCustomBorder());

		
	}



	/**
	 * Moves the Figure on its parent. Does not affect size or layout of the
	 * Figure.
	 * 
	 * @param offset
	 *            Offset by which the Figure is moved.
	 */
	public void move(Dimension offset) {
		Rectangle bounds = _figure.getBounds();
		bounds = bounds.getCopy().translate(offset.width, offset.height);
		_figure.setBounds(bounds);
	}

	/**
	 * Sets text to be displayed as the represented classes name.
	 * 
	 * @param className
	 *            The text
	 * @return false if text did not change
	 */
	public boolean setDisplayedName(String className) {
		if (className.equals(_namelabel.getText())) {
			return false;
		}
		_namelabel.setText(className);
		pack();
		return true;
	}

	/**
	 * Adds a CombinedMouseListener which is notified when the Figure is clicked
	 * or dragged.
	 * 
	 * @param listener
	 *            the Listener to be registered
	 */
	public void addCombinedMouseListener(final CombinedMouseListener listener) {
		_figure.addMouseListener(listener);
		_figure.addMouseMotionListener(listener);
		_figure.addAncestorListener(new AncestorListener.Stub() {		
			@Override
			public void ancestorAdded(IFigure ancestor) {
				ancestor.addMouseMotionListener(listener);
				
			}
		});
	}

	/**
	 * Adds Listener to the consumeButton
	 * 
	 * @param listener
	 *            the Listener to be registered
	 */
	public void addConsumeButtonListener(ActionListener listener) {
		_consumeButton.addActionListener(listener);
	}

	/**
	 * Adds Listener to the produceButton
	 * 
	 * @param listener
	 *            the Listener to be registered
	 */
	public void addProduceButtonListener(ActionListener listener) {
		_produceButton.addActionListener(listener);
	}

	/**
	 * Makes the figure visible or invisible.
	 * 
	 * @param flag
	 *            true for visible, false for invisible
	 */
	public void setVisible(boolean flag) {
		_figure.setVisible(flag);
	}

	/**
	 * Sets the background color of the figure
	 * 
	 * @param colorID
	 *            The new Color
	 */
	public void setColor(Color color) {
		_figure.setBackgroundColor(color);
	}

	/**
	 * @return the gui-Figure
	 */
	public IFigure getFigure() {
		return _figure;
	}

	/**
	 * Sets the backround color for the Consume-Button
	 * 
	 * @param color
	 *            The new Color
	 */
	public void setConsumeButtonColor(Color color) {
		_consumeButton.setBackgroundColor(color);
	}

	/**
	 * Resizes the Figure to its preferred size, but does not make it smaller
	 * than minimum size.
	 */
	private void pack() {
		Dimension newSize = _figure.getPreferredSize().union(
				_figure.getMinimumSize());
		_figure.setSize(newSize);
	}

	/**
	 * Sets the backround color for the Produce-Button
	 * 
	 * @param color
	 *            The new Color
	 */
	public void setProduceButtonColor(Color color) {
		_produceButton.setBackgroundColor(color);	
	}
	
	private Border createCustomBorder() {
		Color c1 = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
		Color c2 = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		Color[] shadows = {c2,c1};
		Color[] highlights = {c1};
		Scheme scheme = new Scheme(highlights, shadows);
		return new SchemeBorder(scheme);
	}


	/**
	 * Adds a Mouse Listener to the GUI
	 * @param mouseListener the new MouseListener
	 */
	public void addMouseListener(MouseListener mouseListener) {
		_figure.addMouseListener(mouseListener);
		
	}


	/**
	 * Sets the visibility of the pin-symbol
	 * @param flag true for visible; false for invisible
	 */
	public void setPinVisible(boolean flag) {
		_pinSymbol.setVisible(flag);
	}



	/**
	 * Brings the Figure to the front, so it is drawn over other figures.
	 */
	public void bringToFront() {
		//TODO: Find a better implementation for bringToFront
		IFigure parent = _figure.getParent();
		parent.remove(_figure);
		parent.add(_figure);
	}




}
