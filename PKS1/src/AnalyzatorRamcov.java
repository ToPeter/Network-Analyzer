import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import Objects.Ip;
import Objects.Ramec;

public class AnalyzatorRamcov {
	
	private final static String F_ETHERNET_TYPE = "./ConfigFiles/EthernetTypes";
	private final static String F_IP_PROTOCOL = "./ConfigFiles/IP_protocols.txt";
	private final static String F_TCP_PORTS = "./ConfigFiles/TCP_ports.txt";
	private final static String F_UDP_PORTS = "./ConfigFiles/UDP_ports1.txt";
	private final static String F_ICMP_TYPE = "./ConfigFiles/ICMP_type";
	
	public static String ipformat(String ip){
		StringBuilder vystup = new StringBuilder();
		
		for(int i=0; i<8; i=i+2){
			vystup.append(Integer.parseInt(ip.substring(i, i+2), 16)+(i<6 ? "." : ""));
		}
		
		return vystup.toString();
	}
	
	public void pridajIP(String SA, int velkost, ArrayList<Ip> ipecky1){
		
		for(Ip a: ipecky1){
			if(a.getAdresa().equals(SA)){
				a.setPocet(a.getPocet()+velkost);
				return;
			}
		}
		ipecky1.add(new Ip(SA,0));
		
	}
	
	public Ramec analyzuj(String vstup, ArrayList<Ip> ipecky1){
		String IPverzia,paket,type,SA,DA,sourcePortName,destinationPortName,protocol;
		int velkost,sourcePort,destinationPort,ipHeaderSize,tcpZaciatok,ipZaciatok,arpZaciatok;
		
		Ramec ramec = new Ramec();
		
		ramec.setRamecHexa(vstup);
		
		paket = vstup.replaceAll("\\s", ""); // delete white spaces 
		velkost = paket.length() / 2;
		ramec.setVelkostDriver(velkost);
		ramec.setVelkostMedium(velkost + 4 <= 64 ? 64 : velkost+4);

		type = paket.substring(24, 28); // EtherType from frame

		if (Integer.parseInt(type, 16) > 1500) {    // parsed from 16tkova do 10tkova
			type=citajSubor(type, F_ETHERNET_TYPE);
			ramec.setETH_type("Ethernet II");
			IPverzia = paket.substring(28, 29);
			if("IP".equals(type)){
				if("4".equals(IPverzia)){
					//System.out.println("v4");
					ramec.setIPverzia(4);
					
					ipHeaderSize=Integer.parseInt(paket.substring(29,30),16)*32/8;
					SA=paket.substring(52,60);
					DA=paket.substring(60, 68);
					SA=AnalyzatorRamcov.ipformat(SA);
					
					ramec.setSourceIP(SA);
					DA=AnalyzatorRamcov.ipformat(DA);
					ramec.setDestinationIP(DA);
					pridajIP(SA, velkost,ipecky1);
					
					protocol = citajSubor(paket.substring(28+9*2, 28+9*2+2), F_IP_PROTOCOL);
					ramec.setIP_protocol(protocol);
					
					if("TCP".equals(protocol)){
						//ramec.setIP_protocol("TCP");
						tcpZaciatok=28+ipHeaderSize*2;
						
						String flags = Integer.toBinaryString(Integer.parseInt(paket.substring(tcpZaciatok+12*2,tcpZaciatok+(12+2)*2),16));
						flags=flags.substring(flags.length()-6);
						ramec.setFIN(flags.charAt(5));
						ramec.setACK(flags.charAt(1));
						ramec.setRST(flags.charAt(3));
						ramec.setSYN(flags.charAt(4));
						
						sourcePort=Integer.parseInt(paket.substring(tcpZaciatok, tcpZaciatok+4),16);
						ramec.setSourcePort(sourcePort);
						sourcePortName=citajSubor(paket.substring(tcpZaciatok, tcpZaciatok+4),F_TCP_PORTS);
						ramec.setTCP_sourcePort(sourcePortName);
						
						destinationPort=Integer.parseInt(paket.substring(tcpZaciatok+4, tcpZaciatok+8),16);
						ramec.setDestinationPort(destinationPort);
						destinationPortName=citajSubor(paket.substring(tcpZaciatok+4, tcpZaciatok+8), F_TCP_PORTS);
						ramec.setTCP_destinationPort(destinationPortName);
						ramec.setFilter("unknown".equals(sourcePortName) ? destinationPortName : sourcePortName);
					}
					else{
						if("UDP".equals(protocol)){
							//ramec.setIP_protocol("UDP");							
							tcpZaciatok=28+ipHeaderSize*2;
							
							sourcePort=Integer.parseInt(paket.substring(tcpZaciatok, tcpZaciatok+4),16);
							ramec.setSourcePort(sourcePort);
							sourcePortName = citajSubor(paket.substring(tcpZaciatok, tcpZaciatok+4),F_UDP_PORTS);
							ramec.setTCP_sourcePort(sourcePortName);
							
							destinationPort=Integer.parseInt(paket.substring(tcpZaciatok+4, tcpZaciatok+8),16);
							ramec.setDestinationPort(destinationPort);
							destinationPortName = citajSubor(paket.substring(tcpZaciatok+4, tcpZaciatok+8), F_UDP_PORTS);
							ramec.setTCP_destinationPort(destinationPortName);
							ramec.setFilter("unknown".equals(sourcePortName) ? destinationPortName : sourcePortName);
						}
						else{
							if("ICMP".equals(protocol)){
								ramec.setFilter("ICMP");
								tcpZaciatok=28+ipHeaderSize*2;
								
								ramec.setICMP_type(citajSubor(paket.substring(tcpZaciatok, tcpZaciatok+2),F_ICMP_TYPE));
								//System.out.println(ramec.getICMP_type());
							}
							else{
								ramec.setIP_protocol(protocol);
							}
						}
					}
				}
				else{
					//System.out.println("v6");
					ramec.setIPverzia(6);
				}
			}
			else{
				if("ARP".equals(type)){
					ramec.setFilter("ARP");
					arpZaciatok = 28;
					String sourceMAC,destinationMAC;
					int dlzkaMAC=Integer.parseInt(paket.substring(arpZaciatok+4*2,arpZaciatok+5*2),16);
					int dlzkaIP=Integer.parseInt(paket.substring(arpZaciatok+5*2,arpZaciatok+6*2),16);
					int operation = Integer.parseInt(paket.substring(arpZaciatok+6*2,arpZaciatok+8*2),16);
					sourceMAC=paket.substring(arpZaciatok+8*2, arpZaciatok+8*2+dlzkaMAC*2);
					ramec.setARP_sourceMAC(formatujMAC(sourceMAC));
					destinationMAC=paket.substring(arpZaciatok+(8+dlzkaIP+dlzkaMAC)*2, arpZaciatok+(8+dlzkaIP+2*dlzkaMAC)*2);
					ramec.setARP_destinationMAC(formatujMAC(destinationMAC));
					
					if(operation == 1){
						//System.out.println("request");
						ramec.setARP_type("request");
					}
					else{
						//System.out.println("reply");
						ramec.setARP_type("reply");
					}				
					
					SA=paket.substring(arpZaciatok+8*2+dlzkaMAC*2,arpZaciatok+(8+dlzkaMAC+dlzkaIP)*2);
					DA=paket.substring(arpZaciatok+(8+2*dlzkaMAC+dlzkaIP)*2,arpZaciatok+(8+2*dlzkaMAC+2*dlzkaIP)*2);
					SA=AnalyzatorRamcov.ipformat(SA);
					ramec.setSourceIP(SA);
					DA=AnalyzatorRamcov.ipformat(DA);
					ramec.setDestinationIP(DA);	
					pridajIP(SA, velkost, ipecky1);
				}
				else{
					citajSubor(type, F_ETHERNET_TYPE);
				}
			}
		} else {
			type = paket.substring(28, 32);
			if ("AAAA".toUpperCase().equals(type.toUpperCase())) {
				ramec.setETH_type("IEEE 802.3 SNAP");
			} else if ("FFFF".toUpperCase().equals(type.toUpperCase())) {
				ramec.setETH_type("IEEE 802.3 RAW");
			} else {
				ramec.setETH_type("IEEE 802.3 LLC");
			}
		}

		ramec.setETH_destinationMAC(AnalyzatorRamcov.formatujMAC(paket.substring(0, 12)));
		ramec.setETH_sourceMAC(AnalyzatorRamcov.formatujMAC(paket.substring(12, 24)));
		//ramec.setETH_type(paket.substring(24, 28));
		return ramec;
		
	}

	public static String formatujMAC(String adresa){
		StringBuilder mac = new StringBuilder();
		for(int i=0; i<12; i=i+2){
			mac.append(adresa.substring(i, i+2)+" ");
		}
		return mac.toString().toUpperCase();
	}
	
	public static String citajSubor(String type, String file) { // read from confi files 
		String cislo="",nazov;
		Path filePath = Paths.get(file);
		Scanner sc=null;
		try {
			sc = new Scanner(filePath);
			while(sc.hasNextLine()) {
				nazov = sc.nextLine().replace("\t", "");
				if(file.equals(F_ETHERNET_TYPE) || file.equals(F_TCP_PORTS) || file.equals(F_UDP_PORTS)){
					cislo=nazov.substring(0, 4);
					if(cislo.equalsIgnoreCase(type)) {
						sc.close();
						return nazov.substring(4).replaceAll("\\s", "");
					}
				}
				else{
					cislo=nazov.substring(0, 2);
					if(cislo.equalsIgnoreCase(type)) {
						sc.close();
						return nazov.substring(2).replaceAll("\\s", "");
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