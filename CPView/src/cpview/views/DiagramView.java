package cpview.views;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import cpview.tools.DiagramTool;


/**
 * TODO
 */

public class DiagramView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "cpview.views.DiagramView";
	
    /** The ID of the views context menu */
    public static final String CONTEXT_ID = ID + ".context";

	/**
	 * The constructor.
	 */
	public DiagramView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent){
		new DiagramTool(parent);
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}
}