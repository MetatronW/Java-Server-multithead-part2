// length mi da la lunghezza dell'array di stringhe senza fare altre conversioni
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
			this.out.println("Connessione stabilita ");
			this.out.flush();

			String line = in.readLine();
			while(line != null){
				String [] parts = line.split("#");
				//poi ci vuole char mi sa
				StringBuilder response = new StringBuilder();

				for (int i = 0; i<parts.length; i++){
					if(parts[i].length() >= 5){
						response.append(parts[i]+"#");
					}
				}
				out.println("substring >= 5 " +response);
				// qui funziona xk non ho usato lista
				line = in.readLine();
			}//fine while

			
		} catch (IOException e) {
			System.out.println("Problem with the client - It will be disconnected");
			System.out.println(e);
		} finally {
		  // rimuove il thread dal registro e libera le risorse
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
		final int PORT = 36000;
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