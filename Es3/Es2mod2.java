// con min e max si riferisce alla lunghezza dell'intera riga, in nessuno dei 2 casi fa split(",")
//ok funziona
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


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
			this.out.println("Connessione stabilita ,ES2 ec ");
			this.out.flush();

			List<String> strings = new ArrayList<>();
			String input;

			while ((input = in.readLine()) != null) { //  quando chiede di aggiungere + righe finendo con un PUNTO
				if (input.equals(".")) {
					break;
				}
				strings.add(input);
			}

			int k = strings.size();
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;

			for (String s : strings) {
				int len = s.length();
				if (len < min) min = len;
				if (len > max) max = len;
			}

			if (k == 0) {
				min = 0;
				max = 0;
			}

			out.println(k + "#" + min + "#" + max);            
            

			
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
		final int PORT = 32000;
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