/**
 * An echo client. The client enters data to the server, and the
 * server echoes the data back to the client.\
 *
 * This has been simplified, what the client sends is now hardcoded.
 *
 */

import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class EchoClient
{

	public static void main(String[] args) throws IOException {
		if (args.length < 2) {
			System.err.println("Usage: java EchoClient <IP address> <Port number>");
			System.exit(0);
		}

//		/BigInteger chunkSize = new BigInteger(args[2]);			//chunkSize - # of keys
		
		BufferedReader in = null;
		PrintWriter out = null;
		Socket sock = null;

		try {
			sock = new Socket(args[0], Integer.parseInt(args[1]));		//uses this to connect to the server

			// set up the necessary communication channels
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(),true);

			
			//Initial Case
			out.println("Requesting Work..");
			
			String line = in.readLine();
			
			
			while(line != null){
				
				
				if(line.equals("Find the Key with these Parameters..")){
					out.println("Loading Client Search Parameters..");
					System.out.println("Loading Search Parameters..");
					
					//Load Parameters..
					
					out.println("Parameters Loaded!");
					System.out.println("Parameters Loaded!");
				}
				else if(line.equals("Server: Closing Connection..")){
					out.println("Performing Search..");
					
					//Do Search..
					out.println("..");
					
					//Send Result
					out.println("Search Finished!");
					//Check if key found or not
					//if(keyFound..)
						//out.println("Key Found! " + key);
					//else
						out.println("Key NOT FOUND! - Client Available!");
						out.println("Requesting Work..");
				}
				
				line = in.readLine();	//Read Next Line..
				
			}
			//System.out.println(in.readLine());
			
			//Server says key not found 
			while(in.readLine().equals("Key Not Found - Client Available!")){
				out.println("Requesting Work..");
				//Skip to readLine that shows search results
				for(int i = 0; i < 5; i++){in.readLine();}				
			}
			
		//	 send a sequence of messages and print the replies
//			out.println("Knock, knock");
//			System.out.println(in.readLine());
//			out.println("A broken pencil");
//			System.out.println(in.readLine());
//			out.println("Never mind, its pointless.");
//			System.out.println(in.readLine());
//			out.println("bye");
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
			if (sock != null)
				sock.close();
		}
	}
}
