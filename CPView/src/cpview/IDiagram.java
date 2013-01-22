package cpview;

import org.eclipse.swt.dnd.DropTargetListener;

public interface IDiagram {
		
	public void registerClassByName(ITypeName className);
	
	public void registerRelationship(IRelationshipName relName);
	
	public void buildDiagramFromRegister();
	
	public void refresh();
	
	public void addDropTargetListener(DropTargetListener listener);
	
}
