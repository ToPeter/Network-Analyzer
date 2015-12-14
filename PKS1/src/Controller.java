import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;
import javax.swing.JTextArea;

import GUI.Okno;
import Objects.ICMP;
import Objects.Ip;
import Objects.Ramec;
import Objects.Velkost;


public class Controller {
	private Okno okno; 
	private HlavnaTrieda hlavna;
	private ArrayList<Ramec> vyfiltrovane = new ArrayList<Ramec>();
	private ArrayList<ICMP> ipecky2 = new ArrayList<ICMP>();
	
	public static final int POCET_INTERVALOV = 20;
	
	
	public Controller(HlavnaTrieda hlavna, Okno okno){
		this.okno=okno;
		this.hlavna=hlavna;
		
		this.okno.getBtn_analyzuj().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.this.hlavna.getRamce().clear();
				Controller.this.hlavna.getIpecky1().clear();
				if(Controller.this.hlavna.getPackety()!=null)
					Controller.this.hlavna.getPackety().clear();
				Controller.this.okno.getTextovaOblast().setText("");
				Controller.this.hlavna.setFile(Controller.this.okno.getCesta().getText());
				Controller.this.hlavna.nacitaj();
				
				uloha1();
				if(!((String)Controller.this.okno.getKomboBox().getSelectedItem()).equals("ALL")){
					Controller.this.okno.getTextovaOblast().setText("");
					uloha3((String)Controller.this.okno.getKomboBox().getSelectedItem());
				}
			}
		});
		
		this.okno.getBtn_vyber().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fch = new JFileChooser();
				int volba = fch.showOpenDialog(Controller.this.okno);
				if(volba == 0){
					Controller.this.okno.getCesta().setText(fch.getSelectedFile().getPath());
				}
				//System.out.println(volba);
				
			}
		});
		
	}
	
	public void uloha1(){
		JTextArea textovaOblast = okno.getTextovaOblast();
		ArrayList<Ramec> ramce = hlavna.getRamce();
		ArrayList<Ip> ipecky1 = hlavna.getIpecky1();
		for(Ramec a: ramce)
			textovaOblast.append(a.zakladnyVypis()+"\n");
			//System.out.println(a.zakladnyVypis());
		
		textovaOblast.append("====================================================="+"\n");
		
		if(ipecky1.size()!=0){
			textovaOblast.append("\nIP adresy vysielajucich uzlov: "+"\n");
			for(Ip a: ipecky1){
				textovaOblast.append(a.getAdresa()+"\n");
			}
			Collections.sort(ipecky1);
			textovaOblast.append("\nNajviac bytov odoslala IP adresa "+ipecky1.get(0).getAdresa()+" ("+ipecky1.get(0).getPocet()+")\n");
			textovaOblast.append("====================================================="+"\n");
		}
	}
	
	public void uloha3(String typ){
		ArrayList<Ramec> ramce = hlavna.getRamce();
		ArrayList<Ramec> kompletna = null;
		ArrayList<Ramec> nekompletna = null;
		JTextArea textovaOblast = okno.getTextovaOblast();
		typ=typ.toUpperCase();
		vyfiltrovane.clear();
		if(!typ.equalsIgnoreCase("TFTP")){
			for(Ramec a: ramce){
				if(a.getFilter()!=null && typ.toUpperCase().equals(a.getFilter().toUpperCase())){
					//System.out.println(a.getCisloRamca());
					vyfiltrovane.add(a);
				}
			}
			vypis(vyfiltrovane);
		}
		else{
			int cislo=-1;
			for(int i=0;i<ramce.size();i++){
				if(ramce.get(i).getFilter()!=null && typ.toUpperCase().equals(ramce.get(i).getFilter().toUpperCase())){
					//System.out.println(a.getCisloRamca());
					vyfiltrovane.add(ramce.get(i));
					cislo++;
					vyfiltrovane.get(vyfiltrovane.size()-1).setTFTPcislo(cislo);
					for(int j=i+1;j<ramce.size();j++){
						if("UDP".equalsIgnoreCase(ramce.get(j).getIP_protocol()) && jeRovnakaIP(ramce.get(i), ramce.get(j)) && ramce.get(i).getSourcePort()==ramce.get(j).getDestinationPort() && ramce.get(j).getSkontrolovana()!=true){
							vyfiltrovane.add(ramce.get(j));
							ramce.get(j).setSkontrolovana(true);
							vyfiltrovane.get(vyfiltrovane.size()-1).setTFTPcislo(cislo);
							for(int k=j+1;k<ramce.size();k++){
								if("UDP".equalsIgnoreCase(ramce.get(k).getIP_protocol()) && jeKomunikacia(ramce.get(j), ramce.get(k))){
									vyfiltrovane.add(ramce.get(k));
									vyfiltrovane.get(vyfiltrovane.size()-1).setTFTPcislo(cislo);
									ramce.get(k).setSkontrolovana(true);
								}
							}
						}	
					}
				}
			}
			/*for(int i=0;i<vyfiltrovane.size();i++){
				System.out.println(vyfiltrovane.get(i));
			}
			System.out.println("teraz");*/
		}
		
		//vypis();
		
		if(vyfiltrovane!=null && vyfiltrovane.size()!=0 && "TCP".equals(vyfiltrovane.get(0).getIP_protocol())){
			
			//if(typ.equals("HTTP") || typ.equals("HTTPS") || typ.equals("TELNET") || typ.equals("HTTPS"))
			//	textovaOblast.setText("");
			
			hlavna.kompletna(vyfiltrovane);
			
			kompletna = hlavna.getKompletna();
			nekompletna = hlavna.getNekompletna();
			
			if(kompletna!=null || nekompletna!=null)
				textovaOblast.setText("");
			
			if(vyfiltrovane!=null && vyfiltrovane.size()!=0){
				textovaOblast.append("================================================================="+"\n");
				if(kompletna == null){
					textovaOblast.append("Nenasla sa kompletna komunikacia\n");
				}
				else{
					textovaOblast.append("Nasla sa kompletna komunikacia\n");
					Ramec pom = kompletna.get(0);
					if("unknown".equals(pom.getSourcePortName())){
						textovaOblast.append("Klient: "+pom.getSourceIP()+": " + pom.getSourcePort());
						textovaOblast.append("\tServer: "+pom.getDestinationIP()+": " + pom.getDestinationPort() +" (" +pom.getDestinationPortName()+")\n");
					}
					else{
						textovaOblast.append("Klient: "+pom.getDestinationIP()+": " + pom.getDestinationPort());
						textovaOblast.append("\tServer: "+pom.getSourceIP()+": " + pom.getSourcePort() +" (" +pom.getSourcePortName()+")\n");
					}
					/*for(Ramec a: kompletna){
						textovaOblast.append(a.zakladnyVypis()+"\n");
					}*/
					vypis(kompletna);
					textovaOblast.append("================================================================="+"\n");
					textovaOblast.append("Štatistika dĺžky rámcov v bajtoch:\n");
					
					ArrayList<Velkost> velkosti = new ArrayList<Velkost>(50);
					velkosti.add(new Velkost());
					velkosti.get(0).setSpodna(0);
					velkosti.get(0).setHorna(19);
					for(int i=1;i<POCET_INTERVALOV;i++){
						velkosti.add(new Velkost());
						velkosti.get(i).setSpodna(velkosti.get(i-1).getHorna()+1);
						velkosti.get(i).setHorna(velkosti.get(i).getSpodna()*2-1);
					}
					
					int max=0;
					for(Ramec a: kompletna){
						for(Velkost b: velkosti){
							if(b.getSpodna()<=a.getVelkostDriver() && b.getHorna()>=a.getVelkostDriver()){
								b.pridaj();
								if(max<velkosti.indexOf(b)){
									max=velkosti.indexOf(b);
								}
							}
						}
					}
					for(int i=0;i<=max;i++){
						textovaOblast.append(velkosti.get(i).getSpodna()+" - "+velkosti.get(i).getHorna()+":\t"+velkosti.get(i).getPocet()+"\n");
					}
				}
				textovaOblast.append("================================================================="+"\n");
				if(nekompletna == null){
					textovaOblast.append("Nenasla sa nekompletna komunikacia\n");
				}
				else{
					textovaOblast.append("Nasla sa nekompletna komunikacia\n");
					Ramec pom = nekompletna.get(0);
					if("unknown".equals(pom.getSourcePortName())){
						textovaOblast.append("Klient: "+pom.getSourceIP()+": " + pom.getSourcePort());
						textovaOblast.append("\tServer: "+pom.getDestinationIP()+": " + pom.getDestinationPort() +" (" +pom.getDestinationPortName()+")\n");
					}
					else{
						textovaOblast.append("Klient: "+pom.getDestinationIP()+": " + pom.getDestinationPortName());
						textovaOblast.append("\tServer: "+pom.getSourceIP()+": " + pom.getSourcePort() +" (" +pom.getSourcePortName()+")\n");
					}
					/*for(Ramec a: nekompletna){
						textovaOblast.append(a.zakladnyVypis());
					}*/
					vypis(nekompletna);
				}
			}
		}
		if(vyfiltrovane!=null && vyfiltrovane.size()!=0 && "ARP".toUpperCase().equals(vyfiltrovane.get(0).getFilter().toUpperCase())){
			textovaOblast.setText("");
			int cislo = 0;
			for(int i=0;i<vyfiltrovane.size();i++){
				if(!vyfiltrovane.get(i).getSkontrolovana() && "request".equals(vyfiltrovane.get(i).getARP_type())){
					vyfiltrovane.get(i).setSkontrolovana(true);
					textovaOblast.append("================================================================="+"\n");
					textovaOblast.append("Komunikacia #"+(++cislo)+"\n");
					textovaOblast.append("ARP-request, IP adresa: "+vyfiltrovane.get(i).getDestinationIP()+", MAC adresa: ???\n");
					textovaOblast.append("Zdrojová IP: "+vyfiltrovane.get(i).getSourceIP()+", Cieľová IP: "+vyfiltrovane.get(i).getDestinationIP()+"\n");
					textovaOblast.append(vyfiltrovane.get(i).zakladnyVypis()+"\n");
					for(int j=i+1;j<vyfiltrovane.size();j++){
						if("reply".equals(vyfiltrovane.get(j).getARP_type()) && !vyfiltrovane.get(j).getSkontrolovana() && jeKomunikacia(vyfiltrovane.get(i), vyfiltrovane.get(j))){
							textovaOblast.append("Komunikacia #"+(cislo)+"\n");
							textovaOblast.append("ARP-reply, IP adresa: "+vyfiltrovane.get(j).getSourceIP()+", MAC adresa: "+vyfiltrovane.get(j).getARP_sourceMAC()+"\n");
							textovaOblast.append("Zdrojová IP: "+vyfiltrovane.get(j).getSourceIP()+", Cieľová IP: "+vyfiltrovane.get(j).getDestinationIP()+"\n");
							textovaOblast.append(vyfiltrovane.get(j).zakladnyVypis()+"\n");
							vyfiltrovane.get(j).setSkontrolovana(true);
							break;
						}
						if("request".equals(vyfiltrovane.get(j).getARP_type()) && !vyfiltrovane.get(j).getSkontrolovana() && jeKomunikacia(vyfiltrovane.get(i), vyfiltrovane.get(j))){
							textovaOblast.append("Komunikacia #"+(cislo)+"\n");
							textovaOblast.append("ARP-request, IP adresa: "+vyfiltrovane.get(j).getDestinationIP()+", MAC adresa: ???\n");
							textovaOblast.append("Zdrojová IP: "+vyfiltrovane.get(j).getSourceIP()+", Cieľová IP: "+vyfiltrovane.get(j).getDestinationIP()+"\n");
							textovaOblast.append(vyfiltrovane.get(j).zakladnyVypis()+"\n");
							vyfiltrovane.get(j).setSkontrolovana(true);
						}
					}
				}
				else{
					if(!vyfiltrovane.get(i).getSkontrolovana()){
						textovaOblast.append("Komunikacia #"+(cislo++)+"\n"+"ARP-request, IP adresa: "+vyfiltrovane.get(i).getDestinationIP()+", MAC adresa: "+vyfiltrovane.get(i).getARP_sourceMAC()+"\n");
						textovaOblast.append("Zdrojová IP: "+vyfiltrovane.get(i).getSourceIP()+", Cieľová IP: "+vyfiltrovane.get(i).getDestinationIP()+"\n");
						textovaOblast.append(vyfiltrovane.get(i).zakladnyVypis()+"\n");
					}
				}
			}
		}
		if(vyfiltrovane!=null && vyfiltrovane.size()!=0 && "TFTP".toUpperCase().equals(vyfiltrovane.get(0).getFilter().toUpperCase())){
			int cislo=1,i;
			textovaOblast.append("Komunikacia #"+(cislo++)+"\n");
			for(i=1;i<vyfiltrovane.size();i++){
				textovaOblast.append(vyfiltrovane.get(i-1).zakladnyVypis()+"\n");
				if(vyfiltrovane.get(i).getTFTPcislo()!=vyfiltrovane.get(i-1).getTFTPcislo()){
					textovaOblast.append("================================================================="+"\n");
					textovaOblast.append("Komunikacia #"+(cislo++)+"\n");
				}
			}
			textovaOblast.append(vyfiltrovane.get(i-1).zakladnyVypis()+"\n");
		}
		if(vyfiltrovane!=null && vyfiltrovane.size()!=0 && "ICMP".toUpperCase().equals(vyfiltrovane.get(0).getFilter().toUpperCase())){
			System.out.println("Tu som");
			int cislo=1,i;
			for(i=0;i<vyfiltrovane.size();i++){
				textovaOblast.append(vyfiltrovane.get(i).toString()+"\n");
				pridajICMP(vyfiltrovane.get(i).getSourceIP(), vyfiltrovane.get(i).getETH_sourceMAC(), 1);
			}
			textovaOblast.append("=================================================================\n");
			textovaOblast.append("Doimplementovane na cviku: \n");
			for(ICMP a: ipecky2){
				textovaOblast.append("IP: "+a.getAdresa()+" MAC: "+a.getMac()+"Pocet: "+a.getPocet()+"\n");
			}
		}
	}
		
	public boolean jeKomunikacia(Ramec a1,Ramec a2){
		String hlavnyIpPort1=a2.getSourceIP()+a2.getSourcePort();
		String hlavnyIpPort2=a2.getDestinationIP()+a2.getDestinationPort();
		String pomocnyIpPort1=a1.getSourceIP()+a1.getSourcePort();
		String pomocnyIpPort2=a1.getDestinationIP()+a1.getDestinationPort();
		if((hlavnyIpPort1.equals(pomocnyIpPort1) && hlavnyIpPort2.equals(pomocnyIpPort2)) ||
				(hlavnyIpPort1.equals(pomocnyIpPort2) && hlavnyIpPort2.equals(pomocnyIpPort1))){
			return true;
		}
		return false;
	}
	
	public boolean jeRovnakaIP(Ramec a1, Ramec a2){
		String hlavnyIpPort1=a2.getSourceIP();
		String hlavnyIpPort2=a2.getDestinationIP();
		String pomocnyIpPort1=a1.getSourceIP();
		String pomocnyIpPort2=a1.getDestinationIP();
		if((hlavnyIpPort1.equals(pomocnyIpPort1) && hlavnyIpPort2.equals(pomocnyIpPort2)) ||
				(hlavnyIpPort1.equals(pomocnyIpPort2) && hlavnyIpPort2.equals(pomocnyIpPort1))){
			return true;
		}
		return false;
	}
	
	public void vypis(ArrayList<Ramec> vyfiltrovane){
		
		//for(int i=0;i<vyfiltrovane.size();i++)
		//	okno.getTextovaOblast().append(vyfiltrovane.get(i).zakladnyVypis()+"\n");
		
		if(vyfiltrovane.size()>20){
			for(int i=0;i<10;i++)
				okno.getTextovaOblast().append(vyfiltrovane.get(i).zakladnyVypis()+"\n");			//zmenit na pokrocily vypis
			for(int i=vyfiltrovane.size()-10;i<vyfiltrovane.size();i++)
				okno.getTextovaOblast().append(vyfiltrovane.get(i).zakladnyVypis()+"\n");			//zmenit na pokrocily vypis
		}
		else{
			for(int i=0;i<vyfiltrovane.size();i++)
				okno.getTextovaOblast().append(vyfiltrovane.get(i).zakladnyVypis()+"\n");
		}
	}
	
	public void pridajICMP(String SA, String MAC, int velkost){
		
		for(ICMP a: ipecky2){
			if(a.getAdresa().equalsIgnoreCase(SA) && a.getMac().equalsIgnoreCase(MAC)){
				a.setPocet(a.getPocet()+velkost);
				return;
			}
		}
		ipecky2.add(new ICMP(SA,MAC,velkost));
		
	}
	
}
