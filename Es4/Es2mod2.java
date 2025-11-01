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
			this.out.println("Connessione accettata ");
			this.out.flush();

			String line = in.readLine();
			while(line != null){
				try {
					String [] parts = line.split("#");
					int N = Integer.parseInt(parts[0]);

					StringBuilder result = new StringBuilder();
					for (int i=1; i<parts.length; i++){// attento, il for parte da 1,cioè dopo secondo elemento
						int value = Integer.parseInt(parts[i]);
						if(value> N){//potevi creare nuova variabile

							result.append(value +"#");
						} 

					}
					out.println(result);
				} catch (NumberFormatException e) {
					System.out.println("Errore: formato numero non valido");
					out.println("Errore: uno o più valori non sono numeri validi");
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Errore: formato input non valido");
					out.println("Errore: formato non valido");
				}

				line = in.readLine();

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
		final int PORT = 50001;
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