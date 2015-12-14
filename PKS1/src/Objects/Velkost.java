package Objects;

public class Velkost {
	private int spodna,horna,pocet;

	public int getSpodna() {
		return spodna;
	}

	public void setSpodna(int spodna) {
		this.spodna = spodna;
	}

	public int getHorna() {
		return horna;
	}

	public void setHorna(int horna) {
		this.horna = horna;
	}

	public int getPocet() {
		return pocet;
	}
	
	public void pridaj(){
		this.pocet++;
	}
	
	public Velkost(){
		this.spodna=0;
		this.horna=0;
		this.pocet=0;
	}
	
}
