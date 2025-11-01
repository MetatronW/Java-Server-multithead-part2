import java.io.BufferedReader;
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
 
	public Es2mod2 (Socket client) {
		this.client = client;
	}

	public void run() {
		try {
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "ASCII"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "ASCII"));
			this.out.println("Connessione attiva ");
			this.out.flush();

			String inputLine = in.readLine();
			while(inputLine != null){
				try {
					if(inputLine.startsWith("PRIMO")){
						int n = Integer.parseInt(inputLine.substring(6).trim()); //ok, non ci va split
						// primo funzione
						boolean primo= true; //inizializzo
						if (n <= 1) primo= false;
							if (n == 2) primo= true;
							if (n % 2 == 0) primo= false;
							for (int i = 3; i <= Math.sqrt(n); i += 2) {
								if (n % i == 0) primo= false;
							}
							primo= true;
							if (primo) {
								out.println("TRUE");
							} else {
								out.println("FALSE");
							}
					
					} else if(inputLine.equals("EXIT")){
						out.println("server chiude la connessione");
						break;
					}
				} catch (StringIndexOutOfBoundsException e) {
					System.out.println("Errore: comando PRIMO senza parametro");
					out.println("Errore: formato comando non valido");
				} catch (NumberFormatException e) {
					System.out.println("Errore: parametro non numerico per PRIMO");
					out.println("Errore: parametro non valido");
				}

				inputLine = in.readLine();//ci vuole, altrimenti loop
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
		final int PORT = 20002;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server initialized sulla  porta "+PORT );
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