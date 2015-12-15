package Objects;

public class Frame {
	private int frameNumber,sizeOfDriver,sizeOfMedium,IP_version,sourcePort,destinationPort,TFTPnumber;
	private String hexaFrame,type,filter,ICMP_type,ARP_type;
	private String ETH_sourceMAC,ETH_destinationMAC,ETH_type;
	private String IP_protocol,sourceIP,destinationIP;
	private String sourcePortName,destinationPortName;
	private String ARP_sourceMAC,ARP_destinationMAC;
	private Boolean controlCheck;
	private char SYN,FIN,RST,ACK;
	
	
	public int getTFTPnumber() {
		return TFTPnumber;
	}
	public void setTFTPnumber(int tFTPnumber) {
		tFTPnumber = tFTPnumber;
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
	public int getIP_version() {
		return IP_version;
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
	public void setIP_version(int iPversion) {
		IP_version = iPversion;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
	public int getFrameNumber() {
		return frameNumber;
	}
	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}
	public int getSizeOfDriver() {
		return sizeOfDriver;
	}
	public void setSizeOfDriver(int sizeOfDriver) {
		this.sizeOfDriver = sizeOfDriver;
	}
	public String getHexaFrame() {
		return hexaFrame;
	}
	public void setHexaFrame(String hexaFrame) {
		this.hexaFrame = hexaFrame;
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
		StringBuilder printOut = new StringBuilder();
		printOut.append("Frame #" + frameNumber + "\nLenght of frame given by packet driver - "
				+ sizeOfDriver + "\nLenght of frame transfering via medium - " + sizeOfMedium
				+ "\n" + ETH_type + "\nSource MAC address: " + ETH_sourceMAC.toUpperCase() + "\nTarget MAC adress: "
				+ ETH_destinationMAC.toUpperCase() + "\n");
		
		if("ICMP".toUpperCase().equals(filter.toUpperCase())){
			printOut.append("ICMP type: "+ICMP_type+"\n");
		}
		
		printOut.append(hexaFrame.toUpperCase() + "\n");
		
		return printOut.toString();
	}
	
	public String basicPrintOut(){
		return "Frame #" + frameNumber + "\nLenght of frame given by packet driver - "
				+ sizeOfDriver + "\nLenght of frame transfering via medium - " + sizeOfMedium
				+ "\n" + ETH_type + "\nSource MAC address: " + ETH_sourceMAC.toUpperCase() + "\nTarget MAC adress: "
				+ ETH_destinationMAC.toUpperCase() + "\n" + hexaFrame.toUpperCase() + "\n";
	}
	
	public int getSizeOfMedium() {
		return sizeOfMedium;
	}
	public void setSizeOfMedium(int sizeOfMedium) {
		this.sizeOfMedium = sizeOfMedium;
	}
	public Frame() {
		this.controlCheck=false;
	}
	public Boolean getControlCheck() {
		return controlCheck;
	}
	public void setControlCheck(Boolean controlCheck) {
		this.controlCheck = controlCheck;
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
