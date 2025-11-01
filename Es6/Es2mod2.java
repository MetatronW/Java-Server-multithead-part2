// per ordinare dovevo usare Collections.sort(lista); //
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList; //importo collections

public class Es2mod2 extends Thread{
	private Socket client; 			// socket for the communication
	private BufferedReader in;		// input reader
	private PrintWriter out;		// output writer
 	private static LinkedList<Integer> lista = new LinkedList<>();

	public Es2mod2 (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "UTF-8"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "UTF-8"));
			this.out.println("Sei connesso ");
			this.out.flush();

			String line = in.readLine();
			while (line != null){
				try {
					if (line.startsWith("INSERT")){
						int k = Integer.parseInt(line.substring(7).trim());
						/* 
						int index = 0;
					for (Integer num : sharedList) {
						if (num > k) break;
						index++;
					}
					sharedList.add(index, k);
					*/
						synchronized(lista) {
							lista.add(k);
							Collections.sort(lista); // per ordinare semplicemente// OK
						}					
						out.println("numero inserito: " + k);
					} else if(line.equals("LIST")){
						//restituisce lista ordinata :  l'ordinamento si fa prima//new: non cambia se lo faccio dopo qui
						synchronized(lista) {
							out.println(lista.toString());
						}
						//poi devo chiudere la connessione
						break;// OK, COSI CHIUDE DOPO LIST
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Errore: comando INSERT senza parametro");
					out.println("Errore: formato comando non valido");
				} catch (NumberFormatException e) {
					System.out.println("Errore: parametro non numerico per INSERT");
					out.println("Errore: parametro non valido");
				}

				line = in.readLine();

			}// fine while

			
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
		final int PORT = 36001;
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