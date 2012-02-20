package cpview.tools;

/**
 * Simple Listener to observe ClassTools
 * @author Alexander Brömmer
 *
 */
public interface SubtoolListener {

	/**
	 * Called when a ClassTool moves or changes status of a class in the diagram
	 */
	void classChanged();

}
