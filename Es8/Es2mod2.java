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
			this.out = new PrintWriter(new OutputStreamWriter(this.client.getOutputStream(), "UTF-8"),true);
			this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream(), "UTF-8"));
			this.out.println("Connessione attivata");
			this.out.flush();

			String line = in.readLine();
			while (line != null){
				try {
					String [] pairs = line.split("#");
					StringBuilder response = new StringBuilder();

					for(int i=0; i<pairs.length ; i++){
						String [] parts = pairs[i].split("&"); // 
						if (parts.length == 2){
							String stringa = parts[0];
							int num = Integer.parseInt(parts[1]);

							int diff= Math.abs(stringa.length()-num); 

							response.append(diff+"#");


						}else {
							out.println("input invalido");
							throw new IOException("Invalid input");						
						}
					}
					out.println(response);
				} catch (NumberFormatException e) {
					System.out.println("Errore: formato numero non valido");
					out.println("Errore: parametro numerico non valido");
				} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Errore: formato coppia non valido");
					out.println("Errore: formato input non valido");
				}

			//line = in.readLine();	//forse ci vuole
			client.close();
				//ok, ci sono 2 modi: o continuo ad inserire comando	
				//	oppure chiudo dopo 1 esecuzione: dipende dal testo
				
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
		final int PORT = 35000;
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