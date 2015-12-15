import java.util.ArrayList;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;


public class PacketReader {
	// open file - save to string
	public static String getHexDump(PcapPacket p) {
        StringBuilder hex = new StringBuilder();
        for (int i = 1, size = p.size(); i <= size; i++) {
            hex.append(String.format("%02x", p.getUByte(i-1)));
            if(i%16==0){
            	hex.append("\n");
            }
            else{
	            if(i%8==0){
	            	hex.append("  ");
	            }
	            else
	            	hex.append(" ");
            }
        }
        return hex.toString();
    }

	public static void nacitajPackety(String file,final ArrayList<String> packety) {
		
		final StringBuilder errbuf = new StringBuilder(); // Chybove hlasky
		//packety = new ArrayList<String>();
		
		Pcap pcap = Pcap.openOffline(file, errbuf);

		if (pcap == null) {
			System.err.printf("Error while opening PCAP file: "+errbuf.toString());
			//return null;
                         // add if no file !! = error  
		}

		PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
			/*
			 * String enter =
			 * "00 04 76 A4 E4 8C 00 00 C0 D7 80 C2 08 00 45 00\n" +
			 * "00 28 0C 36 40 00 80 06 2B 5A 93 AF 62 EE 45 38\n" +
			 * "87 6A 04 70 00 50 7E 6C 06 32 56 7D 30 A8 50 10\n" +
			 * "44 70 97 A0 00 00 80 C2 08 0C 36 40 30 A3 23 35\n" +
			 * "A2 D5 27 81";
			 */
			//String vstup;

			@Override
			public void nextPacket(PcapPacket packet, String user) {
				//String vstup = packet.toHexdump(packet.getTotalSize(), false, false, true).trim().replaceAll("  ", " ").replaceAll("\n ", "\n");
				//System.out.println(vstup+"\n\n");
				String entryString = getHexDump(packet);	
				//System.out.println(packet.getPacketWirelen() + " " + packet.getTotalSize());
				
				packety.add(entryString);
			}
		};

		try {
			pcap.loop(Pcap.LOOP_INFINITE, jpacketHandler, "jNetPcap rocks!");
		} finally {
			pcap.close();
		}
		//return packety;
}
}
