package Objects;

public class Size {
	private int downOne,upperOne,count;

	public int getDownOne() {
		return downOne;
	}

	public void setDownOne(int downOne) {
		this.downOne = downOne;
	}

	public int getUpperOne() {
		return upperOne;
	}

	public void setUpperOne(int upperOne) {
		this.upperOne = upperOne;
	}

	public int getCount() {
		return count;
	}
	
	public void add(){
		this.count++;
	}
	
	public Size(){
		this.downOne=0;
		this.upperOne=0;
		this.count=0;
	}
	
}
