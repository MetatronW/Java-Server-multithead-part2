// codice per scrivere e leggere da file esterno
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Es2mod2 extends Thread{
	private Socket client; 			// socket for the communication
	private BufferedReader in;		// input reader
	private PrintWriter out;		// output writer
 	private static final String FILE_NAME = "orders.txt";

	public Es2mod2 (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "UTF-8"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "UTF-8"));
			this.out.println("Connection accepted, send your order ");
			this.out.flush();

			String line = in.readLine();
			while (line != null){
				try {
					if (line.startsWith("ADD")){
						String entry = line.substring(4);
						try (FileWriter fw = new FileWriter(FILE_NAME, true)) {// vedi se c'è modo + semplice
							fw.write(entry + System.lineSeparator());
						}
					} else if(line.startsWith("GET")){
						String name = line.substring(4).trim();
						int total = 0;

						try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) { //vedi per bene
							String lineread = reader.readLine();
							while (lineread != null){
								String [] parts = lineread.split("#"); // : lineread si riferisce al file order.txt
								if( parts.length ==2 && parts[0].equals(name)){
									total += Integer.parseInt(parts[1]); // : ok, prende order txt e somma la seconda colonna di valori
								}
								lineread = reader.readLine(); // ci vuole come while generale, altrimenti nn legge la seguente rigale, altrimenti nn legge la seguente riga
								//ok, cosi funziona
							}
						}
						out.println(total);
					} else if (line.equals("END")){
						out.println("disconnected END");
						break;
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Errore: comando senza parametri");
					out.println("Errore: formato comando non valido");
				} catch (NumberFormatException e) {
					System.out.println("Errore: formato numero non valido nel file");
					out.println("Errore: dato non valido nel file");
				} catch (IOException e) {
					System.out.println("Errore: problema con il file " + FILE_NAME);
					out.println("Errore: problema di I/O con il file");
				}
				//vedi se fa loop infinito // senza ha fatto ADD molte volte sul txt
				line = in.readLine();
				//client.close();
			}// fine while		
			client.close();
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
		final int PORT = 25001;
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