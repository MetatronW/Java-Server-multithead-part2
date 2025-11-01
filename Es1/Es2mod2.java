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
			this.out.println("Connessione stabilita  es");
			this.out.println("inserire 2 stringhe");
			this.out.flush();

			String njnum = in.readLine();
			String ajnum = in.readLine();

			if (njnum ==null || ajnum ==null){
				out.println("missing output");				
				return;
			}
			String [] partnj = njnum.split("#");
			String [] partaj = ajnum.split("#");

			if(partnj.length != partaj.length  ){
				out.println("lunghezza diversa delle 2 stringhe");
				throw new IOException("lunghezza diversa delle 2 stringhe"); //
				//return; //vedi se fa qualcosa, puoi lanciare eccezione
			}

			try {
				StringBuilder response = new StringBuilder(); //al posto di lista
				for (int i = 0; i<partnj.length; i++){
					int nj = Integer.parseInt(partnj[i]);
					int aj = Integer.parseInt(partaj[i]);

					int rj = nj%aj;
					response.append(rj +"#"); // 
				}
			
				//out.println(response.toString()); // ci vuole toString, vedi altri metodi
				out.println(response); // si funziona senza toString
			} catch (NumberFormatException e) {
				System.out.println("Errore: formato numero non valido");
				out.println("Errore: uno o più valori non sono numeri validi");
			} catch (ArithmeticException e) {
				System.out.println("Errore: divisione per zero nel modulo");
				out.println("Errore: divisore uguale a zero");
			}
	
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
		final int PORT = 54001;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server initialized sulla porta"+PORT);
			while(true){
				Es2mod2 sms = new Es2mod2(server.accept());
				sms.start();
				System.out.println("new user connected sulla porta "+PORT);
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