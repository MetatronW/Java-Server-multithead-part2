//dizionario [matricola, esami]
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Es2mod extends Thread{
	private Socket client; 			// socket for the communication
	private BufferedReader in;		// input reader
	private PrintWriter out;		// output writer
 	private static Map<String, Integer> studentExams = Collections.synchronizedMap(new HashMap<>());
	 
	public Es2mod (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "UTF-8"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "UTF-8"));
			this.out.println("Connection accepted   ,ES2 ec ");
			this.out.flush();

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				try {
					if (inputLine.startsWith("PUT")) {
						String[] parts = inputLine.substring(4).split("#");
						if (parts.length == 2) {
							String matr = parts[0].trim(); //  
							int exams = Integer.parseInt(parts[1].trim()); //  
							studentExams.put(matr, exams); // 
						}
					} else if (inputLine.startsWith("GET")) {
						int threshold = Integer.parseInt(inputLine.substring(4).trim());
						StringBuilder result = new StringBuilder();
						synchronized (studentExams) {
							for (Map.Entry<String, Integer> entry : studentExams.entrySet()) { //: vedi sintassi,  
								if (entry.getValue() < threshold) {
									if (result.length() > 0) result.append(",");
									result.append(entry.getKey()); //  
								}
							}
						}
						out.println("[" + result.toString() + "]"); //  
					} else if (inputLine.equals("END")) {
						out.println("Goodbye");
						break;
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Errore: comando senza parametri sufficienti");
					out.println("Errore: formato comando non valido");
				} catch (NumberFormatException e) {
					System.out.println("Errore: parametro numerico non valido");
					out.println("Errore: parametro non valido");
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Errore: formato parametri non valido per PUT");
					out.println("Errore: formato input non valido");
				}
			}

			client.close();
			System.out.println("Cliente desconectado");
			
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
			  if (this.client != null && !this.client.isClosed())
			      this.client.close();
		  } catch (IOException e) {
			  System.out.println(e);
		  }
		}
	}
     
	// main module
	//
	public static void main(String[] args) throws Exception{
		final int PORT = 55000;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server initialized sulla porta "+PORT);
			while(true){
				Es2mod sms = new Es2mod(server.accept());
				sms.start();
				System.out.println("nuovo user connected");
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