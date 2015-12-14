package Objects;

public class Ramec {
	private int cisloRamca,velkostDriver,velkostMedium,IPverzia,sourcePort,destinationPort,TFTPcislo;
	private String ramecHexa,typ,filter,ICMP_type,ARP_type;
	private String ETH_sourceMAC,ETH_destinationMAC,ETH_type;
	private String IP_protocol,sourceIP,destinationIP;
	private String sourcePortName,destinationPortName;
	private String ARP_sourceMAC,ARP_destinationMAC;
	private Boolean skontrolovana;
	private char SYN,FIN,RST,ACK;
	
	
	public int getTFTPcislo() {
		return TFTPcislo;
	}
	public void setTFTPcislo(int tFTPcislo) {
		TFTPcislo = tFTPcislo;
	}
	public String getICMP_type() {
		return ICMP_type;
	}
	public void setICMP_type(String iCMP_type) {
		ICMP_type = iCMP_type;
	}
	public char getSYN() {
		return SYN;
	}
	public void setSYN(char sYN) {
		SYN = sYN;
	}
	public char getFIN() {
		return FIN;
	}
	public void setFIN(char fIN) {
		FIN = fIN;
	}
	public char getRST() {
		return RST;
	}
	public void setRST(char rST) {
		RST = rST;
	}
	public char getACK() {
		return ACK;
	}
	public void setACK(char aCK) {
		ACK = aCK;
	}
	public int getIPverzia() {
		return IPverzia;
	}
	public int getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}
	public int getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(int destinationPort) {
		this.destinationPort = destinationPort;
	}
	public void setIPverzia(int oPverzia) {
		IPverzia = oPverzia;
	}
	public String getTyp() {
		return typ;
	}
	public void setTyp(String typ) {
		this.typ = typ;
	}	
	public int getCisloRamca() {
		return cisloRamca;
	}
	public void setCisloRamca(int cisloRamca) {
		this.cisloRamca = cisloRamca;
	}
	public int getVelkostDriver() {
		return velkostDriver;
	}
	public void setVelkostDriver(int velkostDriver) {
		this.velkostDriver = velkostDriver;
	}
	public String getRamecHexa() {
		return ramecHexa;
	}
	public void setRamecHexa(String ramecHexa) {
		this.ramecHexa = ramecHexa;
	}
	public String getETH_sourceMAC() {
		return ETH_sourceMAC;
	}
	public void setETH_sourceMAC(String eTH_sourceMAC) {
		ETH_sourceMAC = eTH_sourceMAC;
	}
	public String getETH_destinationMAC() {
		return ETH_destinationMAC;
	}
	public void setETH_destinationMAC(String eTH_destinationMAC) {
		ETH_destinationMAC = eTH_destinationMAC;
	}
	public String getETH_type() {
		return ETH_type;
	}
	public void setETH_type(String eTH_type) {
		ETH_type = eTH_type;
	}
	public String getIP_protocol() {
		return IP_protocol;
	}
	public void setIP_protocol(String iP_protocol) {
		IP_protocol = iP_protocol;
	}
	public String getSourceIP() {
		return sourceIP;
	}
	public void setSourceIP(String iP_sourceIP) {
		sourceIP = iP_sourceIP;
	}
	public String getDestinationIP() {
		return destinationIP;
	}
	public void setDestinationIP(String iP_destinationIP) {
		destinationIP = iP_destinationIP;
	}
	public String getTCP_sourcePort() {
		return sourcePortName;
	}
	public void setTCP_sourcePort(String tCP_sourcePort) {
		sourcePortName = tCP_sourcePort;
	}
	public String getTCP_destinationPort() {
		return destinationPortName;
	}
	public void setTCP_destinationPort(String tCP_destinationPort) {
		destinationPortName = tCP_destinationPort;
	}
	@Override
	public String toString() {
		StringBuilder vypis = new StringBuilder();
		
		vypis.append("Rámec #" + cisloRamca + "\nDĺžka rámca poskytnutá paketovým drajverom - "
				+ velkostDriver + "\nDĺžka rámca prenášaného po médiu - " + velkostMedium
				+ "\n" + ETH_type + "\nZdrojová MAC adresa: " + ETH_sourceMAC.toUpperCase() + "\nCieľová MAC adresa: "
				+ ETH_destinationMAC.toUpperCase() + "\n");
		
		if("ICMP".toUpperCase().equals(filter.toUpperCase())){
			vypis.append("ICMP typ: "+ICMP_type+"\n");
		}
		
		vypis.append(ramecHexa.toUpperCase() + "\n");
		
		return vypis.toString();
	}
	
	public String zakladnyVypis(){
		return "Rámec #" + cisloRamca + "\nDĺžka rámca poskytnutá paketovým drajverom - "
				+ velkostDriver + "\nDĺžka rámca prenášaného po médiu - " + velkostMedium
				+ "\n" + ETH_type + "\nZdrojová MAC adresa: " + ETH_sourceMAC.toUpperCase() + "\nCieľová MAC adresa: "
				+ ETH_destinationMAC.toUpperCase() + "\n" + ramecHexa.toUpperCase() + "\n";
	}
	
	public int getVelkostMedium() {
		return velkostMedium;
	}
	public void setVelkostMedium(int velkostMedium) {
		this.velkostMedium = velkostMedium;
	}
	public Ramec() {
		this.skontrolovana=false;
	}
	public Boolean getSkontrolovana() {
		return skontrolovana;
	}
	public void setSkontrolovana(Boolean skontrolovana) {
		this.skontrolovana = skontrolovana;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getSourcePortName() {
		return sourcePortName;
	}
	public void setSourcePortName(String sourcePortName) {
		this.sourcePortName = sourcePortName;
	}
	public String getDestinationPortName() {
		return destinationPortName;
	}
	public void setDestinationPortName(String destinationPortName) {
		this.destinationPortName = destinationPortName;
	}
	public String getARP_type() {
		return ARP_type;
	}
	public void setARP_type(String aRP_type) {
		ARP_type = aRP_type;
	}
	public String getARP_sourceMAC() {
		return ARP_sourceMAC;
	}
	public void setARP_sourceMAC(String aRP_sourceMAC) {
		ARP_sourceMAC = aRP_sourceMAC;
	}
	public String getARP_destinationMAC() {
		return ARP_destinationMAC;
	}
	public void setARP_destinationMAC(String aRP_destinationMAC) {
		ARP_destinationMAC = aRP_destinationMAC;
	}
	
}
