package cpview.materials;

import cpview.values.CPMode;
import cpview.values.RelationshipType;

public class Relationship {

	private final Type _startPoint;
	private final Type _endPoint;
	private final CPMode _cpMode;
	private final RelationshipType _relType;//TODO 

	public Relationship(Type startPoint,
			Type endPoint, CPMode cpMode, RelationshipType relationshipType) {
		_startPoint = startPoint;
		_endPoint = endPoint;
		_cpMode = cpMode;
		_relType = relationshipType; 
	}
	
	
	public Type getStart(){
		return _startPoint;
	}
	
	public Type getEnd(){
		return _endPoint;
	}

	public CPMode getCPMode(){
		return _cpMode;
	}
	
	public boolean isVisible(){
		return _startPoint.isVisible() && _endPoint.isVisible();
	}
	
	public RelationshipType getType(){
		return _relType;
	}


	


}
