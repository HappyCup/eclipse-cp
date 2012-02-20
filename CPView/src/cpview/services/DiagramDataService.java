package cpview.services;

import java.util.HashSet;
import java.util.Set;

import cpview.materials.Relationship;
import cpview.materials.Type;
import cpview.values.CPMode;
import cpview.values.RelationshipType;

public class DiagramDataService {

	private Set<Type> _classes;
	private Set<Relationship> _relationships;

	public DiagramDataService() {
		_classes = new HashSet<Type>();
		_relationships = new HashSet<Relationship>();
		createDummy(); //TODO

	}
	
	private void createDummy(){
		Type tool = new Type("Werkzeug");
		tool.setPinned(true);
		_classes.add(tool);
		Type gui = new Type("GUI");
		_classes.add(gui);
		Type service1 = new Type("Service1");
		_classes.add(service1);
		Type service2 = new Type("Service2");
		_classes.add(service2);
		Type material1 = new Type("Material1");
		_classes.add(material1);
		Type material2 = new  Type("Material2");
		_classes.add(material2);
		
		_relationships.add(new Relationship(tool, gui, CPMode.PRODUCE, RelationshipType.ASSOCIATION));
		_relationships.add(new Relationship(tool, service1, CPMode.CONSUME, RelationshipType.ASSOCIATION));
		_relationships.add(new Relationship(service1, service2, CPMode.PRODUCE, RelationshipType.INHERITENCE));
		_relationships.add(new Relationship(service2, material1, CPMode.CONSUME, RelationshipType.ASSOCIATION));
		_relationships.add(new Relationship(service2, material2, CPMode.CONSUME, RelationshipType.ASSOCIATION));
		_relationships.add(new Relationship(service1, material2, CPMode.PRODUCE, RelationshipType.DEPENDENCY));
		
	}

	public Set<Type> getClasses() {
		refreshVisibilities();
		return _classes;
	}

	public Set<Relationship> getRelationships() {
		refreshVisibilities();
		return _relationships;

	}
	
	public Set<Relationship> getRelationsshipsFromClass(Type cl){
		Set<Relationship> result = new HashSet<Relationship>();
		for (Relationship r: _relationships){
			if (r.getStart().equals(cl)){
				result.add(r);
			}
		}
		return result;		
	}

	public void refreshVisibilities(){
		for (Type c: _classes){
			c.setVisible(false);
		}
		for (Type c: _classes){
			if (c.isPinned()){
				c.setVisible(true);
			}
		}
		boolean newVisiblesFound = true;
		while (newVisiblesFound){
			newVisiblesFound = false;
			for (Relationship r: _relationships){
				if (r.getStart().isVisible() && r.getCPMode()==CPMode.CONSUME){
					if (r.getEnd().setVisible(true)) newVisiblesFound = true;
					
				}
				if (r.getStart().isVisible() && r.getCPMode()==CPMode.PRODUCE && r.getStart().getMode() == CPMode.PRODUCE){
					if (r.getEnd().setVisible(true)) newVisiblesFound =true;
				}
			}
		}
		
	}
}
