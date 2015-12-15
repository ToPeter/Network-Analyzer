package Objects;

public class ICMP {
	private String address;
	private String mac;
	private int count;

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
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public ICMP(String address, String mac, int size){
		this.address=address;
		this.count=size;
		this.mac = mac;
	}
}
