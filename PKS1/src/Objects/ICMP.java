package Objects;

public class ICMP {
	private String adresa;
	private String mac;
	private int pocet;

	public String getAdresa() {
		return adresa;
	}

	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}

	public int getPocet() {
		return pocet;
	}

	public void setPocet(int pocet) {
		this.pocet = pocet;
	}
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public ICMP(String adresa, String mac, int velkost){
		this.adresa=adresa;
		this.pocet=velkost;
		this.mac = mac;
	}
}
