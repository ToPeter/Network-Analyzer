package Objects;

public class Ip implements Comparable<Ip>{

	private String address;
	private int count;
	
	public int compareTo(Ip arg0) {
		return arg0.count - this.count;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public Ip(String address, int size){
		this.address=address;
		this.count=size;
	}

}
