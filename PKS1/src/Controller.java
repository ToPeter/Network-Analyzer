import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import GUI.GUI;
import Objects.ICMP;
import Objects.Ip;
import Objects.Frame;
import Objects.Size;


public class Controller {
	private GUI window; 
	private MainClass main;
	private ArrayList<Frame> filtered = new ArrayList<Frame>();
	private ArrayList<ICMP> IPs2 = new ArrayList<ICMP>();
	
	public static final int NUMBER_OF_INTERVALS = 20;
	
	
	public Controller(MainClass main, GUI window){
		this.window=window;
		this.main=main;
		
		this.window.getBtn_analyze().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.this.main.getFrames().clear();
				Controller.this.main.getIPs1().clear();
				if(Controller.this.main.getPackets()!=null)
					Controller.this.main.getPackets().clear();
				Controller.this.window.getTextArea().setText("");
				Controller.this.main.setFile(Controller.this.window.getPath().getText());
				Controller.this.main.load();
				
				task1();
				if(!((String)Controller.this.window.getKomboBox().getSelectedItem()).equals("ALL")){
					Controller.this.window.getTextArea().setText("");
					task2((String)Controller.this.window.getKomboBox().getSelectedItem());
				}
			}
		});
		
		this.window.getBtn_selection().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fch = new JFileChooser();
				int volba = fch.showOpenDialog(Controller.this.window);
				if(volba == 0){
					Controller.this.window.getPath().setText(fch.getSelectedFile().getPath());
				}
				//System.out.println(volba);
				
			}
		});
		
	}
	
	public void task1(){
		JTextArea textArea = window.getTextArea();
		ArrayList<Frame> frame = main.getFrames();
		ArrayList<Ip> IPs1 = main.getIPs1();
		for(Frame a: frame)
			textArea.append(a.basicPrintOut()+"\n");
			//System.out.println(a.basicPrintOut());
		
		textArea.append("====================================================="+"\n");
		
		if(IPs1.size()!=0){
			textArea.append("\nIP addresses of sending nodes: "+"\n");
			for(Ip a: IPs1){
				textArea.append(a.getAddress()+"\n");
			}
			Collections.sort(IPs1);
			textArea.append("\nThe most bytes were send by IP: "+IPs1.get(0).getAddress()+" ("+IPs1.get(0).getCount()+")\n");
			textArea.append("====================================================="+"\n");
		}
	}
	
	public void task2(String type){
		ArrayList<Frame> frames = main.getFrames();
		ArrayList<Frame> complete = null;
		ArrayList<Frame> incomplete = null;
		JTextArea textArea = window.getTextArea();
		type=type.toUpperCase();
		filtered.clear();
		if(!type.equalsIgnoreCase("TFTP")){
			for(Frame a: frames){
				if(a.getFilter()!=null && type.toUpperCase().equals(a.getFilter().toUpperCase())){
					//System.out.println(a.getCisloRamca());
					filtered.add(a);
				}
			}
			printableOutput(filtered);
		}
		else{
			int number=-1;
			for(int i=0;i<frames.size();i++){
				if(frames.get(i).getFilter()!=null && type.toUpperCase().equals(frames.get(i).getFilter().toUpperCase())){
					//System.out.println(a.getCisloRamca());
					filtered.add(frames.get(i));
					number++;
					filtered.get(filtered.size()-1).setTFTPnumber(number);
					for(int j=i+1;j<frames.size();j++){
						if("UDP".equalsIgnoreCase(frames.get(j).getIP_protocol()) && sameIP(frames.get(i), frames.get(j)) && frames.get(i).getSourcePort()==frames.get(j).getDestinationPort() && frames.get(j).getControlCheck()!=true){
							filtered.add(frames.get(j));
							frames.get(j).setControlCheck(true);
							filtered.get(filtered.size()-1).setTFTPnumber(number);
							for(int k=j+1;k<frames.size();k++){
								if("UDP".equalsIgnoreCase(frames.get(k).getIP_protocol()) && thereIsCommunication(frames.get(j), frames.get(k))){
									filtered.add(frames.get(k));
									filtered.get(filtered.size()-1).setTFTPnumber(number);
									frames.get(k).setControlCheck(true);
								}
							}
						}	
					}
				}
			}
	
		}
		
	
		
		if(filtered!=null && filtered.size()!=0 && "TCP".equals(filtered.get(0).getIP_protocol())){
			

			
			main.complete(filtered);
			
			complete = main.getComplete();
			incomplete = main.getIncomplete();
			
			if(complete!=null || incomplete!=null)
				textArea.setText("");
			
			if(filtered!=null && filtered.size()!=0){
				textArea.append("================================================================="+"\n");
				if(complete == null){
					textArea.append("Complete communication was not found\n");
				}
				else{
					textArea.append("Complete communication was found\n");
					Frame pom = complete.get(0);
					if("unknown".equals(pom.getSourcePortName())){
						textArea.append("Client: "+pom.getSourceIP()+": " + pom.getSourcePort());
						textArea.append("\tServer: "+pom.getDestinationIP()+": " + pom.getDestinationPort() +" (" +pom.getDestinationPortName()+")\n");
					}
					else{
						textArea.append("Client: "+pom.getDestinationIP()+": " + pom.getDestinationPort());
						textArea.append("\tServer: "+pom.getSourceIP()+": " + pom.getSourcePort() +" (" +pom.getSourcePortName()+")\n");
					}
					/*for(Frame a: complete){
						textovaOblast.append(a.basicPrintOut()+"\n");
					}*/
					printableOutput(complete);
					textArea.append("================================================================="+"\n");
					textArea.append("Statistic of lenght of frame in bytes:\n");
					
					ArrayList<Size> sizes = new ArrayList<Size>(50);
					sizes.add(new Size());
					sizes.get(0).setDownOne(0);
					sizes.get(0).setUpperOne(19);
					for(int i=1;i<NUMBER_OF_INTERVALS;i++){
						sizes.add(new Size());
						sizes.get(i).setDownOne(sizes.get(i-1).getUpperOne()+1);
						sizes.get(i).setUpperOne(sizes.get(i).getDownOne()*2-1);
					}
					
					int max=0;
					for(Frame a: complete){
						for(Size b: sizes){
							if(b.getDownOne()<=a.getSizeOfDriver() && b.getUpperOne()>=a.getSizeOfDriver()){
								b.add();
								if(max<sizes.indexOf(b)){
									max=sizes.indexOf(b);
								}
							}
						}
					}
					for(int i=0;i<=max;i++){
						textArea.append(sizes.get(i).getDownOne()+" - "+sizes.get(i).getUpperOne()+":\t"+sizes.get(i).getCount()+"\n");
					}
				}
				textArea.append("================================================================="+"\n");
				if(incomplete == null){
					textArea.append("Complete communication was not found\n");
				}
				else{
					textArea.append("Complete communication was found\n");
					Frame pom = incomplete.get(0);
					if("unknown".equals(pom.getSourcePortName())){
						textArea.append("Client: "+pom.getSourceIP()+": " + pom.getSourcePort());
						textArea.append("\tServer: "+pom.getDestinationIP()+": " + pom.getDestinationPort() +" (" +pom.getDestinationPortName()+")\n");
					}
					else{
						textArea.append("Client: "+pom.getDestinationIP()+": " + pom.getDestinationPortName());
						textArea.append("\tServer: "+pom.getSourceIP()+": " + pom.getSourcePort() +" (" +pom.getSourcePortName()+")\n");
					}
				
					printableOutput(incomplete);
				}
			}
		}
		if(filtered!=null && filtered.size()!=0 && "ARP".toUpperCase().equals(filtered.get(0).getFilter().toUpperCase())){
			textArea.setText("");
			int number = 0;
			for(int i=0;i<filtered.size();i++){
				if(!filtered.get(i).getControlCheck() && "request".equals(filtered.get(i).getARP_type())){
					filtered.get(i).setControlCheck(true);
					textArea.append("================================================================="+"\n");
					textArea.append("Communication #"+(++number)+"\n");
					textArea.append("ARP-request, IP address: "+filtered.get(i).getDestinationIP()+", MAC address: ???\n");
					textArea.append("Source IP: "+filtered.get(i).getSourceIP()+", Cieľová IP: "+filtered.get(i).getDestinationIP()+"\n");
					textArea.append(filtered.get(i).basicPrintOut()+"\n");
					for(int j=i+1;j<filtered.size();j++){
						if("reply".equals(filtered.get(j).getARP_type()) && !filtered.get(j).getControlCheck() && thereIsCommunication(filtered.get(i), filtered.get(j))){
							textArea.append("Communication #"+(number)+"\n");
							textArea.append("ARP-reply, IP adress: "+filtered.get(j).getSourceIP()+", MAC address: "+filtered.get(j).getARP_sourceMAC()+"\n");
							textArea.append("Source IP: "+filtered.get(j).getSourceIP()+", Target IP: "+filtered.get(j).getDestinationIP()+"\n");
							textArea.append(filtered.get(j).basicPrintOut()+"\n");
							filtered.get(j).setControlCheck(true);
							break;
						}
						if("request".equals(filtered.get(j).getARP_type()) && !filtered.get(j).getControlCheck() && thereIsCommunication(filtered.get(i), filtered.get(j))){
							textArea.append("Communication #"+(number)+"\n");
							textArea.append("ARP-request, IP address: "+filtered.get(j).getDestinationIP()+", MAC address: ???\n");
							textArea.append("Source IP: "+filtered.get(j).getSourceIP()+", Target IP: "+filtered.get(j).getDestinationIP()+"\n");
							textArea.append(filtered.get(j).basicPrintOut()+"\n");
							filtered.get(j).setControlCheck(true);
						}
					}
				}
				else{
					if(!filtered.get(i).getControlCheck()){
						textArea.append("Communication #"+(number++)+"\n"+"ARP-request, IP address: "+filtered.get(i).getDestinationIP()+", MAC address: "+filtered.get(i).getARP_sourceMAC()+"\n");
						textArea.append("Source IP: "+filtered.get(i).getSourceIP()+", Target IP: "+filtered.get(i).getDestinationIP()+"\n");
						textArea.append(filtered.get(i).basicPrintOut()+"\n");
					}
				}
			}
		}
		if(filtered!=null && filtered.size()!=0 && "TFTP".toUpperCase().equals(filtered.get(0).getFilter().toUpperCase())){
			int number=1,i;
			textArea.append("Communication #"+(number++)+"\n");
			for(i=1;i<filtered.size();i++){
				textArea.append(filtered.get(i-1).basicPrintOut()+"\n");
				if(filtered.get(i).getTFTPnumber()!=filtered.get(i-1).getTFTPnumber()){
					textArea.append("================================================================="+"\n");
					textArea.append("Communication #"+(number++)+"\n");
				}
			}
			textArea.append(filtered.get(i-1).basicPrintOut()+"\n");
		}
		if(filtered!=null && filtered.size()!=0 && "ICMP".toUpperCase().equals(filtered.get(0).getFilter().toUpperCase())){
		
			int number=1,i;
			for(i=0;i<filtered.size();i++){
				textArea.append(filtered.get(i).toString()+"\n");
				addICMP(filtered.get(i).getSourceIP(), filtered.get(i).getETH_sourceMAC(), 1);
			}
			textArea.append("=================================================================\n");
			for(ICMP a: IPs2){
				textArea.append("IP: "+a.getAddress()+" MAC: "+a.getMac()+"Count: "+a.getCount()+"\n");
			}
		}
	}
		
	public boolean thereIsCommunication(Frame a1,Frame a2){
		String mainIPport1=a2.getSourceIP()+a2.getSourcePort();
		String mainIPport2=a2.getDestinationIP()+a2.getDestinationPort();
		String temporaryIPport1=a1.getSourceIP()+a1.getSourcePort();
		String temporaryIPport2=a1.getDestinationIP()+a1.getDestinationPort();
		if((mainIPport1.equals(temporaryIPport1) && mainIPport2.equals(temporaryIPport2)) ||
				(mainIPport1.equals(temporaryIPport2) && mainIPport2.equals(temporaryIPport1))){
			return true;
		}
		return false;
	}
	
	public boolean sameIP(Frame a1, Frame a2){
		String mainIPport1=a2.getSourceIP();
		String mainIPport2=a2.getDestinationIP();
		String temporaryIPport1=a1.getSourceIP();
		String temporaryIPport2=a1.getDestinationIP();
		if((mainIPport1.equals(temporaryIPport1) && mainIPport2.equals(temporaryIPport2)) ||
				(mainIPport1.equals(temporaryIPport2) && mainIPport2.equals(temporaryIPport1))){
			return true;
		}
		return false;
	}
	
	public void printableOutput(ArrayList<Frame> filtered){
		
		//for(int i=0;i<vyfiltrovane.size();i++)
		//	okno.getTextovaOblast().append(vyfiltrovane.get(i).basicPrintOut()+"\n");
		
		if(filtered.size()>20){
			for(int i=0;i<10;i++)
				window.getTextArea().append(filtered.get(i).basicPrintOut()+"\n");			//zmenit na pokrocily printableOutput
			for(int i=filtered.size()-10;i<filtered.size();i++)
				window.getTextArea().append(filtered.get(i).basicPrintOut()+"\n");			//zmenit na pokrocily printableOutput
		}
		else{
			for(int i=0;i<filtered.size();i++)
				window.getTextArea().append(filtered.get(i).basicPrintOut()+"\n");
		}
	}
	
	public void addICMP(String SA, String MAC, int size){
		
		for(ICMP a: IPs2){
			if(a.getAddress().equalsIgnoreCase(SA) && a.getMac().equalsIgnoreCase(MAC)){
				a.setCount(a.getCount()+size);
				return;
			}
		}
		IPs2.add(new ICMP(SA,MAC,size));
		
	}
	
}
