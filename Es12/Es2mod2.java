 //
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Es2mod2 extends Thread{
	private Socket client; 			// socket for the communication
	private BufferedReader in;		// input reader
	private PrintWriter out;		// output writer
 	private static List<Integer> lista = new ArrayList<>(); //vedi se funziona con lista semplice
	
	public Es2mod2 (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "UTF-8"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "UTF-8"));
			this.out.println("sei connesso ");
			this.out.flush();

			String line = in.readLine();
			while(line != null){
				if(line.startsWith("INS")){
					int num = Integer.parseInt(line.substring(4).trim());
					if (lista.contains(num)){
						out.println("num è già presente nella lista");
					} else{
						lista.add(num);
						out.println("numero inserito");
					}		
				} else if(line.equals("ORD")){
					Collections.sort(lista);
					out.println("lista ordinata");
				} else if(line.equals("LIST")){
					out.println(lista.toString());
				} else if(line.equals("END")){
					out.println("sei disconnnesso");
					break;
				}
				line = in.readLine(); //
			}//fine while
						
		}catch (IOException e) {
			System.out.println("Problem with the client - It will be disconnected");
			System.out.println(e);
		}finally {
 		  try {
			  this.in.close();
			  this.out.close();
		  }catch (IOException e) {
			  System.out.println(e);
		  }
		}
	}

	// main module
	//
	public static void main(String[] args) throws Exception{
		final int PORT = 64000;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server initialized sulla porta "+PORT);
			while(true){
				Es2mod2 sms = new Es2mod2(server.accept());
				sms.start();
				System.out.println("new user connected");
			}
		} catch (IOException e) {
			System.out.println(e);
			if (server!=null)
				server.close();
		}//end try-catch

	}

}

 