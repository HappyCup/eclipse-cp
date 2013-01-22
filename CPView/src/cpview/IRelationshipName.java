package cpview;

import cpview.values.CPMode;
import cpview.values.RelationshipType;

public interface IRelationshipName {
	
	public ITypeName getOriginName();
	
	public ITypeName getDestinationName();
	
	public CPMode getCPMode();
	
	public RelationshipType getRelationshipType();

}
