import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import Objects.Ip;
import Objects.Frame;

public class FrameAnalyzer {
	
	private final static String F_ETHERNET_TYPE = "./ConfigFiles/EthernetTypes";
	private final static String F_IP_PROTOCOL = "./ConfigFiles/IP_protocols.txt";
	private final static String F_TCP_PORTS = "./ConfigFiles/TCP_ports.txt";
	private final static String F_UDP_PORTS = "./ConfigFiles/UDP_ports1.txt";
	private final static String F_ICMP_TYPE = "./ConfigFiles/ICMP_type";
	
	public static String IPformat(String ip){
		StringBuilder output = new StringBuilder();
		
		for(int i=0; i<8; i=i+2){
			output.append(Integer.parseInt(ip.substring(i, i+2), 16)+(i<6 ? "." : ""));
		}
		
		return output.toString();
	}
	
	public void addIP(String SA, int size, ArrayList<Ip> IPs1){
		
		for(Ip a: IPs1){
			if(a.getAddress().equals(SA)){
				a.setCount(a.getCount()+size);
				return;
			}
		}
		IPs1.add(new Ip(SA,0));
		
	}
	
	public Frame analyze(String input, ArrayList<Ip> IPs1){
		String IPversion,packet,type,SA,DA,sourcePortName,destinationPortName,protocol;
		int lenght,sourcePort,destinationPort,ipHeaderSize,tcpBegining,ipBegining,arpBegining;
		
		Frame frame = new Frame();
		
		frame.setHexaFrame(input);
		
		packet = input.replaceAll("\\s", ""); // delete white spaces 
		lenght = packet.length() / 2;
		frame.setSizeOfDriver(lenght);
		frame.setSizeOfMedium(lenght + 4 <= 64 ? 64 : lenght+4);

		type = packet.substring(24, 28); // EtherType from frame

		if (Integer.parseInt(type, 16) > 1500) {    // parsed from 16tkova do 10tkova
			type=readFile(type, F_ETHERNET_TYPE);
			frame.setETH_type("Ethernet II");
			IPversion = packet.substring(28, 29);
			if("IP".equals(type)){
				if("4".equals(IPversion)){
					//System.out.println("v4");
					frame.setIP_version(4);
					
					ipHeaderSize=Integer.parseInt(packet.substring(29,30),16)*32/8;
					SA=packet.substring(52,60);
					DA=packet.substring(60, 68);
					SA=FrameAnalyzer.IPformat(SA);
					
					frame.setSourceIP(SA);
					DA=FrameAnalyzer.IPformat(DA);
					frame.setDestinationIP(DA);
					addIP(SA, lenght,IPs1);
					
					protocol = readFile(packet.substring(28+9*2, 28+9*2+2), F_IP_PROTOCOL);
					frame.setIP_protocol(protocol);
					
					if("TCP".equals(protocol)){
						//ramec.setIP_protocol("TCP");
						tcpBegining=28+ipHeaderSize*2;
						
						String flags = Integer.toBinaryString(Integer.parseInt(packet.substring(tcpBegining+12*2,tcpBegining+(12+2)*2),16));
						flags=flags.substring(flags.length()-6);
						frame.setFIN(flags.charAt(5));
						frame.setACK(flags.charAt(1));
						frame.setRST(flags.charAt(3));
						frame.setSYN(flags.charAt(4));
						
						sourcePort=Integer.parseInt(packet.substring(tcpBegining, tcpBegining+4),16);
						frame.setSourcePort(sourcePort);
						sourcePortName=readFile(packet.substring(tcpBegining, tcpBegining+4),F_TCP_PORTS);
						frame.setTCP_sourcePort(sourcePortName);
						
						destinationPort=Integer.parseInt(packet.substring(tcpBegining+4, tcpBegining+8),16);
						frame.setDestinationPort(destinationPort);
						destinationPortName=readFile(packet.substring(tcpBegining+4, tcpBegining+8), F_TCP_PORTS);
						frame.setTCP_destinationPort(destinationPortName);
						frame.setFilter("unknown".equals(sourcePortName) ? destinationPortName : sourcePortName);
					}
					else{
						if("UDP".equals(protocol)){
							//ramec.setIP_protocol("UDP");							
							tcpBegining=28+ipHeaderSize*2;
							
							sourcePort=Integer.parseInt(packet.substring(tcpBegining, tcpBegining+4),16);
							frame.setSourcePort(sourcePort);
							sourcePortName = readFile(packet.substring(tcpBegining, tcpBegining+4),F_UDP_PORTS);
							frame.setTCP_sourcePort(sourcePortName);
							
							destinationPort=Integer.parseInt(packet.substring(tcpBegining+4, tcpBegining+8),16);
							frame.setDestinationPort(destinationPort);
							destinationPortName = readFile(packet.substring(tcpBegining+4, tcpBegining+8), F_UDP_PORTS);
							frame.setTCP_destinationPort(destinationPortName);
							frame.setFilter("unknown".equals(sourcePortName) ? destinationPortName : sourcePortName);
						}
						else{
							if("ICMP".equals(protocol)){
								frame.setFilter("ICMP");
								tcpBegining=28+ipHeaderSize*2;
								
								frame.setICMP_type(readFile(packet.substring(tcpBegining, tcpBegining+2),F_ICMP_TYPE));
								//System.out.println(ramec.getICMP_type());
							}
							else{
								frame.setIP_protocol(protocol);
							}
						}
					}
				}
				else{
					//System.out.println("v6");
					frame.setIP_version(6);
				}
			}
			else{
				if("ARP".equals(type)){
					frame.setFilter("ARP");
					arpBegining = 28;
					String sourceMAC,destinationMAC;
					int MAClenght=Integer.parseInt(packet.substring(arpBegining+4*2,arpBegining+5*2),16);
					int IPlenght=Integer.parseInt(packet.substring(arpBegining+5*2,arpBegining+6*2),16);
					int operation = Integer.parseInt(packet.substring(arpBegining+6*2,arpBegining+8*2),16);
					sourceMAC=packet.substring(arpBegining+8*2, arpBegining+8*2+MAClenght*2);
					frame.setARP_sourceMAC(MACformating(sourceMAC));
					destinationMAC=packet.substring(arpBegining+(8+IPlenght+MAClenght)*2, arpBegining+(8+IPlenght+2*MAClenght)*2);
					frame.setARP_destinationMAC(MACformating(destinationMAC));
					
					if(operation == 1){
						//System.out.println("request");
						frame.setARP_type("request");
					}
					else{
						//System.out.println("reply");
						frame.setARP_type("reply");
					}				
					
					SA=packet.substring(arpBegining+8*2+MAClenght*2,arpBegining+(8+MAClenght+IPlenght)*2);
					DA=packet.substring(arpBegining+(8+2*MAClenght+IPlenght)*2,arpBegining+(8+2*MAClenght+2*IPlenght)*2);
					SA=FrameAnalyzer.IPformat(SA);
					frame.setSourceIP(SA);
					DA=FrameAnalyzer.IPformat(DA);
					frame.setDestinationIP(DA);	
					addIP(SA, lenght, IPs1);
				}
				else{
					readFile(type, F_ETHERNET_TYPE);
				}
			}
		} else {
			type = packet.substring(28, 32);
			if ("AAAA".toUpperCase().equals(type.toUpperCase())) {
				frame.setETH_type("IEEE 802.3 SNAP");
			} else if ("FFFF".toUpperCase().equals(type.toUpperCase())) {
				frame.setETH_type("IEEE 802.3 RAW");
			} else {
				frame.setETH_type("IEEE 802.3 LLC");
			}
		}

		frame.setETH_destinationMAC(FrameAnalyzer.MACformating(packet.substring(0, 12)));
		frame.setETH_sourceMAC(FrameAnalyzer.MACformating(packet.substring(12, 24)));
		//ramec.setETH_type(paket.substring(24, 28));
		return frame;
		
	}

	public static String MACformating(String address){
		StringBuilder MAC = new StringBuilder();
		for(int i=0; i<12; i=i+2){
			MAC.append(address.substring(i, i+2)+" ");
		}
		return MAC.toString().toUpperCase();
	}
	
	public static String readFile(String type, String file) { // read from confi files 
		String number="",name;
		Path filePath = Paths.get(file);
		Scanner sc=null;
		try {
			sc = new Scanner(filePath);
			while(sc.hasNextLine()) {
				name = sc.nextLine().replace("\t", "");
				if(file.equals(F_ETHERNET_TYPE) || file.equals(F_TCP_PORTS) || file.equals(F_UDP_PORTS)){
					number=name.substring(0, 4);
					if(number.equalsIgnoreCase(type)) {
						sc.close();
						return name.substring(4).replaceAll("\\s", "");
					}
				}
				else{
					number=name.substring(0, 2);
					if(number.equalsIgnoreCase(type)) {
						sc.close();
						return name.substring(2).replaceAll("\\s", "");
					}
				}
			}
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sc.close();
		return "unknown";
	}
}