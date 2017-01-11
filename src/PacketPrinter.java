import jpcap.*;

import jpcap.packet.*;
import java.io.*;


class PacketPrinter  {
 
public static void main(String[] args) throws Exception {
	
                        JpcapCaptor jpcap = JpcapCaptor.openFile("C:\\smallFlows.pcap");
                     
                          print(jpcap);
                                   
}



public static void print(JpcapCaptor jpcap3) throws Exception
{

   Packet pk;
   IPPacket ipk;


    pk= jpcap3.getPacket();
    ipk=(IPPacket)pk;
    
    FileOutputStream outputStream = new FileOutputStream("D:\\ITM.txt");
	OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
	BufferedWriter dis1 = new BufferedWriter(outputStreamWriter);
		
          do{
        	 	pk=jpcap3.getPacket();
        	 	if (pk.equals(null)) break; 
                ipk=(IPPacket)pk;
        
                          System.out.print("SOURCE HOST IP: "+ipk.src_ip.getHostAddress()+"  ");
                          dis1.write(ipk.src_ip.getHostAddress()+" ");
                          
                          System.out.print("DESTINATION HOST IP: "+ipk.dst_ip.getHostAddress()+" ");
                          dis1.write(ipk.dst_ip.getHostAddress().toString()+" ");
                          
                          System.out.print("DESTINATION HOST IP: "+(int)ipk.len);
                          dis1.write("99");
                          System.out.println(" ");
                          
                          dis1.newLine();
                          

         }while(!pk.equals(null));
         dis1.close();
}

}//class
