package cpview.materials;


import cpview.values.CPMode;

public class Type {
	
	private final String _classname;
	private CPMode _mode;
	private boolean _visible;
	private boolean _pinned;
	
	
	public Type(String classname) {
		_classname = classname;
		_pinned = false;
		_mode = CPMode.CONSUME;
	}

	public String getClassName() {
		return _classname;
	}
	
	public void setMode(CPMode mode){
		_mode=mode;
	}
	
	public CPMode getMode(){
		return _mode;
	}

	public boolean isVisible() {
		return _visible;
	}

	public boolean setVisible(boolean visible) {
		boolean temp = _visible;
		_visible = visible;
		return (temp!=visible);
	}

	public boolean isPinned() {
		return _pinned;
	}
	
	public void setPinned(boolean pinned){
		_pinned = pinned;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Type){
			Type otherClass = (Type) other;
			return otherClass._classname.equals(this._classname);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return _classname.hashCode();
	}
	

}
