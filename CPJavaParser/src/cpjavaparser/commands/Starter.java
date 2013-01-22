package cpjavaparser.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.handlers.HandlerUtil;

import cpjavaparser.parser.Parser;
import cpjavaparser.values.RelationshipName;
import cpjavaparser.values.TypeName;
import cpview.IDiagram;

public class Starter extends AbstractHandler {

	/**
	 * Constructor for the Starter (does nothing yet)
	 */
	public Starter() {
		super();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IDiagram diagram = cpview.Activator.getDefault().getDiagram();
		

		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		IStructuredSelection selection = (IStructuredSelection) sel;
		Object selectedElement = selection.getFirstElement();
		
		parse(selectedElement, diagram);
		return null;
		
		
		
	}
	
	private void parse(Object selectedElement, IDiagram diagram)
	{
	Parser parser = new Parser();

	if (selectedElement instanceof IJavaProject) {

		IJavaProject project = (IJavaProject) selectedElement;

		try {
			parser.evaluateProject(project);
		} catch (JavaModelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}else if (selectedElement instanceof IPackageFragment){
		IPackageFragment pack = (IPackageFragment) selectedElement;
		try {
			parser.evaluatePackage(pack);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}else if (selectedElement instanceof ICompilationUnit){
		ICompilationUnit unit = (ICompilationUnit) selectedElement;
		try{
			parser.evaluateCompilationUnit(unit);
		}catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	for (TypeName t : parser.getClasses()) {
		diagram.registerClassByName(t);
	}
	for (RelationshipName r : parser.getRelationships()) {
		diagram.registerRelationship(r);
	}
	diagram.buildDiagramFromRegister();
	try {
		PlatformUI.getWorkbench().showPerspective("CPView.perspective", PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	} catch (WorkbenchException e) {
		System.out.println("Error opening CPView-Perspective"); // TODO Userfriendly Errors
	}
//	try {
//		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
//				.getActivePage().showView("cpview.views.DiagramView");
//	} catch (PartInitException e) {
//		System.out.println("View not found!");
//	}
	
}

}
