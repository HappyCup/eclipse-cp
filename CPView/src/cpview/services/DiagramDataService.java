package cpview.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.dnd.DropTargetListener;

import cpview.IDiagram;
import cpview.IRelationshipName;
import cpview.ITypeName;
import cpview.materials.Relationship;
import cpview.materials.Type;
import cpview.values.CPMode;

public class DiagramDataService implements IDiagram {

	private Set<ITypeName> _registeredClassNames;
	private Set<IRelationshipName> _registeredReleationships;
	private Map<ITypeName, Type> _classes;
	private Set<Relationship> _relationships;
	private Set<IDiagramDataListener> _listeners;
	private Set<DropTargetListener> _dropTargetListeners;

	public DiagramDataService() {
		_registeredClassNames = new HashSet<ITypeName>();
		_registeredReleationships = new HashSet<IRelationshipName>();
		_classes = new HashMap<ITypeName, Type>();
		_relationships = new HashSet<Relationship>();
		_listeners = new HashSet<IDiagramDataListener>();
		_dropTargetListeners = new HashSet<DropTargetListener>();
	}

	public Collection<Type> getClasses() {
		refreshVisibilities();
		return _classes.values();
	}

	public Set<Relationship> getRelationships() {
		refreshVisibilities();
		return _relationships;

	}

	public Set<Relationship> getRelationsshipsFromClass(Type cl) {
		Set<Relationship> result = new HashSet<Relationship>();
		for (Relationship r : _relationships) {
			if (r.getStart().equals(cl)) {
				result.add(r);
			}
		}
		return result;
	}

	public void refreshVisibilities() {
		for (Type c : _classes.values()) {
			c.setVisible(false);
		}
		for (Type c : _classes.values()) {
			if (c.isPinned()) {
				c.setVisible(true);
			}
		}
		boolean newVisiblesFound = true;
		while (newVisiblesFound) {
			newVisiblesFound = false;
			for (Relationship r : _relationships) {
				if (r.getStart().isVisible() && r.getCPMode() == CPMode.CONSUME) {
					if (r.getEnd().setVisible(true))
						newVisiblesFound = true;

				}
				if (r.getStart().isVisible() && r.getCPMode() == CPMode.PRODUCE
						&& r.getStart().getMode() == CPMode.PRODUCE) {
					if (r.getEnd().setVisible(true))
						newVisiblesFound = true;
				}
			}
		}

	}

	public void refresh() {
		for (IDiagramDataListener listener : _listeners) {
			listener.dataChanged();
		}
	}

	public void addDataListener(IDiagramDataListener listener) {
		_listeners.add(listener);
	}

	@Override
	public void registerClassByName(ITypeName className) {
		_registeredClassNames.add(className);

	}

	@Override
	public void registerRelationship(IRelationshipName relName) {
		_registeredReleationships.add(relName);
	}

	@Override
	public void buildDiagramFromRegister() {
		for (ITypeName classname : _registeredClassNames) {
			if (_classes.get(classname) == null) {
				Type newType = new Type(classname.getSimpleName());
				_classes.put(classname, newType);
				newType.setPinned(true);
			}
		}
		for (IRelationshipName rel : _registeredReleationships) {
			Type originType = _classes.get(rel.getOriginName());
			Type destinationType = _classes.get(rel.getDestinationName());
			if (originType != null && destinationType != null) {
				destinationType.setPinned(false);
				_relationships.add(new Relationship(originType,
						destinationType, rel.getCPMode(), rel
								.getRelationshipType()));
			}
		}
		refresh();
	}

	public Set<DropTargetListener> getDropListeners() {
		return _dropTargetListeners;
	}
	public void addDropTargetListener(DropTargetListener listener){
		_dropTargetListeners.add(listener);
	}

	public void clear() {
		_registeredClassNames = new HashSet<ITypeName>();
		_registeredReleationships = new HashSet<IRelationshipName>();
		_classes.clear();
		_relationships.clear();
		
	}

}
