package eyesimo.processor.judgers;

public class AnalyzeResult {

	String className;
	int classIndex;
	
	public AnalyzeResult(String _className, int _classIndex) {
		className = _className;
		classIndex = _classIndex;
	}
	
	public String getClassName() {
		return className;
	}
	
	public int getClassIndex() {
		return classIndex;
	}
}
