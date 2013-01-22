package cpview.materials;

import cpview.values.CPMode;
import cpview.values.RelationshipType;

public class Relationship {

	private final Type _startPoint;
	private final Type _endPoint;
	private final CPMode _cpMode;
	private final RelationshipType _relType;
	
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
		if (!_startPoint.isVisible() || !_endPoint.isVisible()){
			return false;
		}
		if (_cpMode == CPMode.PRODUCE && _startPoint.getMode()==CPMode.CONSUME){
			return false;
		}
		if (_startPoint.equals(_endPoint)){ //Self-references are not shown
			return false;
		}
		return true;
		
	}
	
	public RelationshipType getType(){
		return _relType;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_endPoint == null) ? 0 : _endPoint.hashCode());
		result = prime * result
				+ ((_relType == null) ? 0 : _relType.hashCode());
		result = prime * result
				+ ((_startPoint == null) ? 0 : _startPoint.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Relationship other = (Relationship) obj;
		if (_endPoint == null) {
			if (other._endPoint != null)
				return false;
		} else if (!_endPoint.equals(other._endPoint))
			return false;
		if (_relType != other._relType)
			return false;
		if (_startPoint == null) {
			if (other._startPoint != null)
				return false;
		} else if (!_startPoint.equals(other._startPoint))
			return false;
		return true;
	}
	


	


}
