 // OK FUNZIONA, anche con spazio tra nome e cognome
 //es con Map, se c'è tempo rifarlo da 0 , rivedi codice, un po + difficile degli altri
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


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
			this.out.println("Connessione attivata ,ES2 ec ");
			this.out.flush();

			
 			Map<String, List<String>> yearMap = new LinkedHashMap<>();

			String line;
			while ((line = in.readLine()) != null) { 
				if (line.equals("#")) break;	// 

				String[] parts = line.split("#");
				if (parts.length != 2) continue;// 

				String name = parts[0].trim();
				String year = parts[1].trim();

				yearMap.putIfAbsent(year, new ArrayList<>()); // 
				yearMap.get(year).add(name);
			}

			for (Map.Entry<String, List<String>> entry : yearMap.entrySet()) { // 
				out.println(entry.getKey() + ": " + entry.getValue());
			}

			client.close();
			System.out.println("Client disconected");			

			 
		}catch (IOException e) {
			System.out.println("Problem with the client - It will be disconnected");
			System.out.println(e);
		}finally {
	
		  try {
			  this.in.close();
			  this.out.close();
		  }catch (IOException e) {
			  System.out.println(e);
		  }
		}
	}
     
	// main module
	//
	public static void main(String[] args) throws Exception{
		final int PORT = 45000;
		ServerSocket server = null;
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server initialized sulla porta "+PORT);
			while(true){
				Es2mod2 sms = new Es2mod2(server.accept());
				sms.start();
				System.out.println("nuovo user connected");
			}
		} catch (IOException e) {
			System.out.println(e);
			if (server!=null)
				server.close();
		}//end try-catch

	}

}

/*
Scrivi il codice Java di un server multi-thread, tale che: (i) accetta connessioni TCP sulla porta di servizio 45000 e scambia messaggi in formato testo UTF-8; (ii) stabilita la connessione con un client, lo saluta con il messaggio “Connessione attivata” e poi si mette in attesa di ricevere dal client una sequenza di linee, ciascuna contenente una stringa della forma nome#anno, dove nome indica il nominativo di una persona mentre anno indica il suo anno di nascita. La seqeuenza di linee si intende terminata quando il server riceve il carattere # su una linea isolata. (iii) Il server risponde al client inviandogli una sequenza di linee, una per ogni diverso anno di nascita ricevuto dal client, che riporta l’elenco delle persone nate in quell’anno. Al termino dell’invio, chiude la connessione.

Esempio di input ricevuto dal server e di output verso il client
Input	Output
Mario Rossi#1970
Linda Bianchi#1981
Giuseppe Verdi#1970
Giulia Rossi#1972
Bruno Neri#1972
#	1970: [Mario Rossi, Giuseppe Verdi]
1981: [Linda Bianchi]
1972: [Giulia Rossi, Bruno Neri]

*/