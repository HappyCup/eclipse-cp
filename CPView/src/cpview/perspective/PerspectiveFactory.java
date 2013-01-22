package cpview.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class PerspectiveFactory implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addView("cpview.views.DiagramView", IPageLayout.TOP, 0.95f, layout.getEditorArea());
		layout.addView("org.eclipse.jdt.ui.PackageExplorer", IPageLayout.LEFT, 0.3f, "cpview.views.DiagramView");
		

	}

}
