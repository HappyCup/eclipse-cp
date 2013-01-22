package cpjavaparser.values;
import cpview.IRelationshipName;
import cpview.ITypeName;
import cpview.values.CPMode;
import cpview.values.RelationshipType;


public class RelationshipName implements IRelationshipName {
	
	private final CPMode _mode;
	private final ITypeName _destination;
	private final ITypeName _origin;
	private final RelationshipType _relationshipType;

	public RelationshipName(ITypeName origin, ITypeName destination, CPMode mode, RelationshipType relType) {
		_origin = origin;
		_destination = destination;
		_mode = mode;
		_relationshipType = relType;
	}

	@Override
	public ITypeName getOriginName() {
		return _origin;
	}

	@Override
	public ITypeName getDestinationName() {
		return _destination;
	}

	@Override
	public CPMode getCPMode() {
		return _mode;
	}

	@Override
	public RelationshipType getRelationshipType() {
		return _relationshipType;
	}

}
