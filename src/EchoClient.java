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

	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length < 2) {
			System.err.println("Usage: java EchoClient <IP address> <Port number>");
			System.exit(0);
		}

//		/BigInteger chunkSize = new BigInteger(args[2]);			//chunkSize - # of keys
		
		//Serch Parameters..
		BigInteger bi = null;
		BigInteger chunkSize = null;
		BigInteger endKey = null;
		String cipher = null;
		int keySize = 0;
		
		BufferedReader in = null;
		PrintWriter out = null;
		Socket sock = null;

		try {
			sock = new Socket(args[0], Integer.parseInt(args[1]));		//uses this to connect to the server

			// set up the necessary communication channels
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(),true);

			
			//Initial Case
			out.println("Requesting Work..\n");
			
			String line = in.readLine();
			
			
			while(line != null){
				
				
				if(line.equals("Find the Key with these Parameters..")){
					
					line = in.readLine();
					System.out.println(line);		//Display..
					
					line = in.readLine();
					System.out.println(line);
					String[] params = line.split(" | ");
					
					out.println("Loading Client Search Parameters..");
					System.out.println("Loading Search Parameters..");
					
					//Load Parameters..
					bi = new BigInteger(params[2]);
					chunkSize = new BigInteger(params[4]);
					cipher = params[6];
					endKey = new BigInteger(params[8]);
					keySize = Integer.parseInt(params[10]);
					
					
					out.println("Parameters Loaded!");
					System.out.println("Parameters Loaded!");
				}
				else if(line.equals("Server: Closing Connection..")){
					out.println("Performing Search..");
					System.out.println("Performing Search..");
					
					//Do Search..
					out.println("..");
					System.out.println("..");
					
					byte[] cipherText = Blowfish.fromBase64(cipher);
					byte[] key = Blowfish.asByteArray(bi, keySize);
					
					 // Go into a loop where we try a range of keys starting at the given one
			        String plaintext = null;
			        BigInteger range = bi.add(chunkSize);	//from currentKey to end range
			       
			  
			        // Search from the key that will give us our desired ciphertext - currentKey + chunkSize
			        for (BigInteger i = new BigInteger(bi.toString()); i.compareTo(range) == -1; i = i.add(BigInteger.ONE)) {
			            // tell user which key is being checked
			            String keyStr = bi.toString();
			            System.out.print(keyStr);
			            Thread.sleep(100);
			            for (int j=0; j<keyStr.length();j++) {
			                System.out.print("\b");
			            }
			            // decrypt and compare to known plaintext
			            Blowfish.setKey(key);        
			            plaintext = Blowfish.decryptToString(cipherText);
			            if (plaintext.equals("May good flourish; Kia hua ko te pai")) {
			               
			            	out.println("Search Finished!");
							System.out.println("Search Finished!");
			            	
			            	System.out.println("Plaintext found!");
			                System.out.println(plaintext);
			                out.println("Plaintext found!");
			                out.println(plaintext);
			                
			                System.out.println("key is (hex) " + Blowfish.toHex(key) + " " + bi);
			                System.out.println("Key Found!");
			                out.println("key is (hex) " + Blowfish.toHex(key) + " " + bi);
			                out.println("Key Found!");
			                
			                System.exit(-1);
			            } 
			            
			            // try the next key
			            bi = bi.add(BigInteger.ONE);
			            key = Blowfish.asByteArray(bi,keySize);
			        }
			        
			        //NO KEY FOUND!
			        out.println("Search Finished!");
					System.out.println("Search Finished!");
			        
			        System.out.println("No key found!");
					
			        //REQUEST FOR MORE WORK!
			        out.println("Key NOT FOUND! - Client Available!");
			        out.println("Search Ended @ Key:" + range);
			        out.println("Requesting Work..");
					System.out.println("Requesting Work..");
					
						
				}
				
				line = in.readLine();	//Read Next Line..
				
			}
			
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
