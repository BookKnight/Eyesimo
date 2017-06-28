package eyesimo.processor.judgers;

import java.util.Comparator;

public class ObjectToCompare implements Comparator{

	int objClass;
	double length;
	
	public ObjectToCompare() {
		objClass = 0;
		length   = 0;
	}
	
	public void setObjClass( int _objClass ) {
		objClass = _objClass;
	}
	
	public int getObjClass() {
		return objClass;
	}
	
	public void setLength( double _length ) {
		length = _length;
	}
	
	public double getLength() {
		return length;
	}

	@Override
	public int compare(Object o1, Object o2) {
		
		if (o1 instanceof ObjectToCompare && o2 instanceof ObjectToCompare) {
			
			ObjectToCompare obj_1 = (ObjectToCompare) o1;
			ObjectToCompare obj_2 = (ObjectToCompare) o2;
			
			if (obj_1.getLength() > obj_2.getLength()) return 1;
			if (obj_1.getLength() < obj_2.getLength()) return -1;
		}
		
		return 0;
	}
}
