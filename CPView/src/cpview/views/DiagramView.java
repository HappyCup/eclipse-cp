package cpview.views;


import org.eclipse.swt.dnd.DropTarget;
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
    
    private DiagramTool _diagramTool;

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
		_diagramTool = new DiagramTool(parent);
	}
	
	public DiagramTool getDiagramTool(){
		return _diagramTool;
	}
	


	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
	}
}