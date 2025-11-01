 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Es2mod2 extends Thread{
	private Socket client; 			// socket for the communication
	private BufferedReader in;		// input reader
	private PrintWriter out;		// output writer
 	private static Map<String, Integer> cityMap = Collections.synchronizedMap(new HashMap<>()); //== ad altro es

	public Es2mod2 (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "UTF-8"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "UTF-8"));
			this.out.println("Connessione stabilita ");
			this.out.flush();

			String line = in.readLine();
			while(line != null){

				if(line.startsWith("PUT")){
					String [] parts = line.substring(4).split("#");
					String city = parts[0];
					int inhabitants = Integer.parseInt(parts[1]);
					cityMap.put(city, inhabitants);
					out.println("City updated");

				} else if (line.startsWith("GET")){
					int threshold = Integer.parseInt(line.substring(4).trim()) ;
					//StringBuilder result = new StringBuilder();
					List<String> result = new ArrayList<>(); //sull'altro ho usato StringBuilder
								synchronized (cityMap) {
									for (Map.Entry<String, Integer> entry : cityMap.entrySet()) {
										if (entry.getValue() > threshold) {
											result.add(entry.getKey());
										}
									}
								}
								out.println(result); //  
				} else if (line.equals("END")){
					out.println("connessione chiusa");
					break;
				}
				line = in.readLine(); // fuori faceva loop infinito
			}
			//line = in.readLine(); //
			
			
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
		final int PORT = 45000;
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

 