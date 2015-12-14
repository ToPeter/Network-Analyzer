import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JTextArea;

import GUI.Okno;
import Objects.Ip;
import Objects.Ramec;

 // purpose of this class is to determine if three-way-handshake was succesfull and if not then determin what fails (specif)
// container which contains all ArrayLists witch data which I am gonna work with 

public class HlavnaTrieda {
	private String file = "/home/jakub/Plocha/Test/TCPkompletnaASI2.pcap";
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	private final static String KOMPLETNA = "ano";
	
	private ArrayList<String> packety;
	private ArrayList<Ramec> ramce;
	private ArrayList<Ip> ipecky1;
	private ArrayList<String> cb_nazvy = new ArrayList<String>();
	private ArrayList<Ramec> kompletna = null;
	private ArrayList<Ramec> nekompletna = null;
	
	public HlavnaTrieda(){
		ramce = new ArrayList<Ramec>();
		ipecky1 = new ArrayList<Ip>();
		//packety=new ArrayList<String>();
		/*cb_nazvy.add("http");
		cb_nazvy.add("https");
		cb_nazvy.add("telnet");
		cb_nazvy.add("ftp-data");
		cb_nazvy.add("ftp-control");
		cb_nazvy.add("ssh");
		cb_nazvy.add("tftp");
		cb_nazvy.add("ICMP");
		cb_nazvy.add("ARP");*/
	}
	
	//public static void main(String[] args) throws FileNotFoundException {
	public void nacitaj(){
				
		packety = new ArrayList<String>();
		PacketReader.nacitajPackety(file,packety);
		if(packety!=null){
			AnalyzatorRamcov analyzer = new AnalyzatorRamcov();
			for(int i=0;i<packety.size();i++){
				ramce.add(analyzer.analyzuj(packety.get(i),ipecky1));
				ramce.get(ramce.size()-1).setCisloRamca(i+1);
			}
		}
		//Okno pokus = new Okno();
		
		//uloha1(pokus);
		
		/*if(KOMPLETNA.equals("ano")){
			kompletna(ramce);			
		}*/
		
	}
	
	public void kompletna(ArrayList<Ramec> ramce){
		ArrayList<Ramec>  pomocna2 = null;
		ArrayList<Ramec> pomocna = new ArrayList<Ramec>();
		kompletna=null;
		nekompletna=null;
		for (int i = 0; i < ramce.size() && (kompletna==null || nekompletna==null); i++) {
			if(!ramce.get(i).getSkontrolovana()){
				pomocna = new ArrayList<Ramec>();
				pomocna.add(ramce.get(i));
				ramce.get(i).setSkontrolovana(true);
				String hlavnyIpPort1=ramce.get(i).getSourceIP()+ramce.get(i).getSourcePort();
				String hlavnyIpPort2=ramce.get(i).getDestinationIP()+ramce.get(i).getDestinationPort();
				for (int j = i+1; j < ramce.size(); j++) {
					String pomocnyIpPort1=ramce.get(j).getSourceIP()+ramce.get(j).getSourcePort();
					String pomocnyIpPort2=ramce.get(j).getDestinationIP()+ramce.get(j).getDestinationPort();
					if((hlavnyIpPort1.equals(pomocnyIpPort1) && hlavnyIpPort2.equals(pomocnyIpPort2)) ||
							(hlavnyIpPort1.equals(pomocnyIpPort2) && hlavnyIpPort2.equals(pomocnyIpPort1))){
						//System.out.println(ramce.get(j).getCisloRamca());
						ramce.get(j).setSkontrolovana(true);
						pomocna.add(ramce.get(j));
					}						
				}
			//}
			
			for (int j = 0; j < pomocna.size()-2 && (kompletna==null || nekompletna==null); j++) {
				if(pomocna.get(j).getSYN()=='1' && (pomocna.get(j+1).getSYN()=='1' && pomocna.get(j+1).getACK()=='1') && pomocna.get(j+2).getACK()=='1' ){
					pomocna2 = new ArrayList<Ramec>();
					pomocna2.add(pomocna.get(j));
					pomocna2.add(pomocna.get(j+1));
					pomocna2.add(pomocna.get(j+2));
					for(int k=j+3;k<pomocna.size();k++){
						if(((pomocna.get(k).getFIN()=='1' ) && k<pomocna.size()-2 && 					//&& !"Unknown".equals(pomocna.get(k).getTCP_sourcePort()) && pomocna.get(k).getACK()=='1'
								(pomocna.get(k+1).getFIN()=='1' && pomocna.get(k+1).getACK()=='1') && 
								pomocna.get(k+2).getACK()=='1')||
								((k+3<pomocna.size() && pomocna.get(k).getFIN()=='1' ) && 					
										(pomocna.get(k+1).getACK()=='1' && pomocna.get(k+2).getFIN()=='1') && 
										pomocna.get(k+3).getACK()=='1')
								|| pomocna.get(k).getRST()=='1'){
							pomocna2.add(pomocna.get(k));
							if(pomocna.get(k).getRST()!='1'){
								pomocna2.add(pomocna.get(k+1));
								pomocna2.add(pomocna.get(k+2));
							}
							if(k+3<pomocna.size() && pomocna.get(k).getFIN()=='1'  && pomocna.get(k+1).getACK()=='1' && pomocna.get(k+2).getFIN()=='1' && pomocna.get(k+3).getACK()=='1'){
								pomocna2.add(pomocna.get(k+3));
							}
							if(kompletna == null)
								kompletna = pomocna2;
							pomocna2 = null;
							break;
						}
						else{
							pomocna2.add(pomocna.get(k));
						}
					}
					if(nekompletna == null)
						nekompletna = pomocna2;			//POZOR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				break;
				}
			
				//System.out.println("Ahoj");
			}
			}
		}
	}

	
	public ArrayList<Ramec> getRamce() {
		return ramce;
	}

	public void setRamce(ArrayList<Ramec> ramce) {
		this.ramce = ramce;
	}

	public ArrayList<Ip> getIpecky1() {
		return ipecky1;
	}

	public void setIpecky1(ArrayList<Ip> ipecky1) {
		this.ipecky1 = ipecky1;
	}

	public ArrayList<String> getCb_nazvy() {
		return cb_nazvy;
	}

	public void setCb_nazvy(ArrayList<String> cb_nazvy) {
		this.cb_nazvy = cb_nazvy;
	}

	public ArrayList<String> getPackety() {
		return packety;
	}

	public void setPackety(ArrayList<String> packety) {
		this.packety = packety;
	}

	public ArrayList<Ramec> getKompletna() {
		return kompletna;
	}

	public void setKompletna(ArrayList<Ramec> kompletna) {
		this.kompletna = kompletna;
	}

	public ArrayList<Ramec> getNekompletna() {
		return nekompletna;
	}

	public void setNekompletna(ArrayList<Ramec> nekompletna) {
		this.nekompletna = nekompletna;
	}
	
	
	
}