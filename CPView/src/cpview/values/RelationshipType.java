package cpview.values;

/**
 * Types for Relationships.
 * @author Alexander Brömmer
 *
 */
public enum RelationshipType {
	
	/**
	 * Structural Relationship (Class 2 is a field in Class 1)
	 */
	ASSOCIATION,
	
	/**
	 * Non-structural Relationship (Class 1 uses Class 2)
	 */
	DEPENDENCY,
	
	/**
	 * Implements or Extends-Relationship (Class 1 is subtype of Class 2)
	 */
	INHERITENCE;

}
