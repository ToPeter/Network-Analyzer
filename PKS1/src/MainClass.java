import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JTextArea;

import GUI.GUI;
import Objects.Ip;
import Objects.Frame;

 // purpose of this class is to determine if three-way-handshake was succesfull and if not then determin what fails (specif)
// container which contains all ArrayLists witch data which I am gonna work with 

public class MainClass {
	private String file = "/home/jakub/Plocha/Test/TCPkompletnaASI2.pcap";
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	private final static String COMPLETE = "ano";
	
	private ArrayList<String> packets;
	private ArrayList<Frame> frames;
	private ArrayList<Ip> IPs1;
	private ArrayList<String> cb_names = new ArrayList<String>();
	private ArrayList<Frame> complete = null;
	private ArrayList<Frame> incomplete = null;
	
	public MainClass(){
		frames = new ArrayList<Frame>();
		IPs1 = new ArrayList<Ip>();
		//packety=new ArrayList<String>();
		/*cb_names.add("http");
		cb_names.add("https");
		cb_names.add("telnet");
		cb_names.add("ftp-data");
		cb_names.add("ftp-control");
		cb_names.add("ssh");
		cb_names.add("tftp");
		cb_names.add("ICMP");
		cb_names.add("ARP");*/
	}
	
	//public static void main(String[] args) throws FileNotFoundException {
	public void load(){
				
		packets = new ArrayList<String>();
		PacketReader.nacitajPackety(file,packets);
		if(packets!=null){
			FrameAnalyzer analyzer = new FrameAnalyzer();
			for(int i=0;i<packets.size();i++){
				frames.add(analyzer.analyze(packets.get(i),IPs1));
				frames.get(frames.size()-1).setFrameNumber(i+1);
			}
		}
		//Okno pokus = new GUI();
		
		//uloha1(pokus);
		
		/*if(KOMPLETNA.equals("ano")){
			complete(frames);			
		}*/
		
	}
	
	public void complete(ArrayList<Frame> frames){
		ArrayList<Frame>  temporary_1 = null;
		ArrayList<Frame> temporary = new ArrayList<Frame>();
		complete=null;
		incomplete=null;
		for (int i = 0; i < frames.size() && (complete==null || incomplete==null); i++) {
			if(!frames.get(i).getControlCheck()){
				temporary = new ArrayList<Frame>();
				temporary.add(frames.get(i));
				frames.get(i).setControlCheck(true);
				String mainIPport1=frames.get(i).getSourceIP()+frames.get(i).getSourcePort();
				String mainIPport2=frames.get(i).getDestinationIP()+frames.get(i).getDestinationPort();
				for (int j = i+1; j < frames.size(); j++) {
					String temporaryIPport=frames.get(j).getSourceIP()+frames.get(j).getSourcePort();
					String temporaryIPport2=frames.get(j).getDestinationIP()+frames.get(j).getDestinationPort();
					if((mainIPport1.equals(temporaryIPport) && mainIPport2.equals(temporaryIPport2)) ||
							(mainIPport1.equals(temporaryIPport2) && mainIPport2.equals(temporaryIPport))){
						//System.out.println(frames.get(j).getCisloRamca());
						frames.get(j).setControlCheck(true);
						temporary.add(frames.get(j));
					}						
				}
			//}
			
			for (int j = 0; j < temporary.size()-2 && (complete==null || incomplete==null); j++) {
				if(temporary.get(j).getSYN()=='1' && (temporary.get(j+1).getSYN()=='1' && temporary.get(j+1).getACK()=='1') && temporary.get(j+2).getACK()=='1' ){
					temporary_1 = new ArrayList<Frame>();
					temporary_1.add(temporary.get(j));
					temporary_1.add(temporary.get(j+1));
					temporary_1.add(temporary.get(j+2));
					for(int k=j+3;k<temporary.size();k++){
						if(((temporary.get(k).getFIN()=='1' ) && k<temporary.size()-2 && 					//&& !"Unknown".equals(pomocna.get(k).getTCP_sourcePort()) && pomocna.get(k).getACK()=='1'
								(temporary.get(k+1).getFIN()=='1' && temporary.get(k+1).getACK()=='1') && 
								temporary.get(k+2).getACK()=='1')||
								((k+3<temporary.size() && temporary.get(k).getFIN()=='1' ) && 					
										(temporary.get(k+1).getACK()=='1' && temporary.get(k+2).getFIN()=='1') && 
										temporary.get(k+3).getACK()=='1')
								|| temporary.get(k).getRST()=='1'){
							temporary_1.add(temporary.get(k));
							if(temporary.get(k).getRST()!='1'){
								temporary_1.add(temporary.get(k+1));
								temporary_1.add(temporary.get(k+2));
							}
							if(k+3<temporary.size() && temporary.get(k).getFIN()=='1'  && temporary.get(k+1).getACK()=='1' && temporary.get(k+2).getFIN()=='1' && temporary.get(k+3).getACK()=='1'){
								temporary_1.add(temporary.get(k+3));
							}
							if(complete == null)
								complete = temporary_1;
							temporary_1 = null;
							break;
						}
						else{
							temporary_1.add(temporary.get(k));
						}
					}
					if(incomplete == null)
						incomplete = temporary_1;			//POZOR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				break;
				}
			
				//System.out.println("Ahoj");
			}
			}
		}
	}

	
	public ArrayList<Frame> getFrames() {
		return frames;
	}

	public void setFrames(ArrayList<Frame> frames) {
		this.frames = frames;
	}

	public ArrayList<Ip> getIPs1() {
		return IPs1;
	}

	public void setIPs1(ArrayList<Ip> IPs1) {
		this.IPs1 = IPs1;
	}

	public ArrayList<String> getCb_names() {
		return cb_names;
	}

	public void setCb_names(ArrayList<String> cb_names) {
		this.cb_names = cb_names;
	}

	public ArrayList<String> getPackets() {
		return packets;
	}

	public void setPackets(ArrayList<String> packets) {
		this.packets = packets;
	}

	public ArrayList<Frame> getComplete() {
		return complete;
	}

	public void setComplete(ArrayList<Frame> complete) {
		this.complete = complete;
	}

	public ArrayList<Frame> getIncomplete() {
		return incomplete;
	}

	public void setIncomplete(ArrayList<Frame> incomplete) {
		this.incomplete = incomplete;
	}
	
	
	
}