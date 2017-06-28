package eyesimo.bankmanager.tools;

import eyesimo.processor.segmentators.ImageFullChars;

public class CommonBankCharsInfo {

	private int classCount;
	private String[] classNames;
	private CurrentBankCharsInfo[] bankInfos;
	
	public CommonBankCharsInfo(int _classCount, String[] names, CurrentBankCharsInfo[] infos) {
		
		classCount = _classCount;
		classNames = names;
		bankInfos   = infos;
	}
	
	public void setClassCount(int count) {
		classCount = count;
	}
	
	public void setClassNames(int pos, String name) {
		classNames[pos] = name;
	}
	
	public void setClassNames(String[] names) {
		classNames = names;
	}
	
	public void setBankInfo(CurrentBankCharsInfo[] info) {
		bankInfos = info;
	}
	
	
	public CurrentBankCharsInfo[] getBankInfo() {
		return bankInfos;
	}
	
	
	public int getClassCount() {
		return classCount;
	}
	
	public String getClassNames(int pos) {
		return classNames[pos];
	}
	
	public String[] getClassNames() {
		return classNames;
	}
}
