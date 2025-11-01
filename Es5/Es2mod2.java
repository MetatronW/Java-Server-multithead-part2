import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.event.ListDataEvent;

public class Es2mod2 extends Thread{
	private Socket client; 			// socket for the communication
	private BufferedReader in;		// input reader
	private PrintWriter out;		// output writer
	private static LinkedList<Integer> list = new LinkedList<Integer>(); //

	public Es2mod2 (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "UTF-8"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "UTF-8"));
			this.out.println("Connection Connected ");
			this.out.flush();

			String line = in.readLine();
			while (line != null){
				try {
					if(line.startsWith("ADD")){
						int k = Integer.parseInt(line.substring(4).trim());//
						synchronized(list) {
							if (list.contains(k)){
								out.println("numero già presente nella lista");
							}else{//
								list.add(k);
								out.println("numero aggiunto");
							}
						}
					} else
					if (line.startsWith("FIND")) {
						int k = Integer.parseInt(line.substring(5).trim());
						synchronized(list) {
							if (list.contains(k)){
								out.println("numero  presente nella lista "+k);
							}else{// entrambi fanno for-each; mi sa che per le liste non posso usare for classico
								int result =0;
								for (int num : list) {//
									if(num < k && num>result){
										result=num;
									}							
								}
								out.println("numero + vicino"+result);
							}
						}					
					} else
					if (line.equals("EXIT")){
						out.println("fine connessione");
						break;
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Errore: comando senza parametro");
					out.println("Errore: formato comando non valido");
				} catch (NumberFormatException e) {
					System.out.println("Errore: parametro non numerico");
					out.println("Errore: parametro non valido");
				}

				line = in.readLine();//
			}//fine while

			
		} catch (IOException e) {
			System.out.println("Problem with the client - It will be disconnected");
			System.out.println(e);
		} finally {
		  try {
			  if (this.in != null)
			      this.in.close();
		  } catch (IOException e) {
			  System.out.println(e);
		  }
		  try {
			  if (this.out != null)
			      this.out.close();
		  } catch (Exception e) {
			  System.out.println(e);
		  }
		  try {
			  if (this.client != null)
			      this.client.close();
		  } catch (IOException e) {
			  System.out.println(e);
		  }
		}
	}

	// main module
	//
	public static void main(String[] args) throws Exception{
		final int PORT = 22001;
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
		} finally {
			if (server!=null) {
				try {
					server.close();
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}//end try-catch

	}

}