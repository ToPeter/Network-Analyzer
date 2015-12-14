package Objects;

public class Ip implements Comparable<Ip>{

	private String adresa;
	private int pocet;
	
	public int compareTo(Ip arg0) {
		return arg0.pocet - this.pocet;
	}

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
	
	public Ip(String adresa, int velkost){
		this.adresa=adresa;
		this.pocet=velkost;
	}

}
