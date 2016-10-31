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

		
		//Search Parameters..
		BigInteger bi = null;
		BigInteger chunkSize = null;
		BigInteger endKey = null;
		String cipher = null;
		int keySize = 0;
		long startTime = 0;
		
		BufferedReader in = null;
		PrintWriter out = null;
		Socket sock = null;

		try {
			sock = new Socket(args[0], Integer.parseInt(args[1]));		//uses this to connect to the server
			
			
			// set up the necessary communication channels
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(),true);

			//Initial Case - When a client is created - request work
			out.println("Requesting Work..\n");
			
			//Listen for Server Response
			while(true){
				
				String line = in.readLine();
				
				//Condition: Signal from server sending the search parameters
				if(line.equals("Find the Key with these Parameters..")){
					
					line = in.readLine();
					System.out.println(line);		//Display..
					
					line = in.readLine();
					System.out.println(line);
					String[] params = line.split(" | ");
					
					out.println("Loading Client Search Parameters..");
					System.out.println("Loading Search Parameters..");
					
					//Load Search Parameters..
					bi = new BigInteger(params[2]);
					chunkSize = new BigInteger(params[4]);
					cipher = params[6];
					endKey = new BigInteger(params[8]);
					keySize = Integer.parseInt(params[10]);
					startTime = Long.parseLong(params[12]);
					
					
					out.println("Parameters Loaded!");
					System.out.println("Parameters Loaded!");
					
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
							
							System.out.println("Key Found!");
							out.println("Key Found!");
			            	
			            	System.out.println("Plaintext found!");
			                System.out.println(plaintext);
			                out.println("Plaintext found!");
			                out.println(plaintext);
			                
			                System.out.println("key is (hex) " + Blowfish.toHex(key) + " " + bi);
			                
			                out.println("key is (hex) " + Blowfish.toHex(key) + " " + bi);
			               
			                
			                long stopTime = System.nanoTime();
							long totalTime = stopTime - startTime;
							
							System.out.println("Client Search Time: " + totalTime + "ns\n");
							out.println("Client Time: " + totalTime + "ns\n");
			                
//							break outer;
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
			        out.println("KEY NOT FOUND! - Client Available!");
			        out.println("Search Ended @ Key:" + range);
			       
//			        out.println("Requesting Work..");
//					System.out.println("Requesting Work..");
				}
				else if(line.equals("Key Found!")){
					System.exit(-1);
				}
				//Condition: Signal from Server stating that connection channels will be closed 
				//indicating for client to start its search
//				else if(line.equals("Server: Closing Connection..")){
//					out.println("Performing Search..");
//					System.out.println("Performing Search..");
//					
//					//Do Search..
//					out.println("..");
//					System.out.println("..");
//					
//					byte[] cipherText = Blowfish.fromBase64(cipher);
//					byte[] key = Blowfish.asByteArray(bi, keySize);
//					
//					 // Go into a loop where we try a range of keys starting at the given one
//			        String plaintext = null;
//			        BigInteger range = bi.add(chunkSize);	//from currentKey to end range
//			       
//			  
//			        // Search from the key that will give us our desired ciphertext - currentKey + chunkSize
//			        for (BigInteger i = new BigInteger(bi.toString()); i.compareTo(range) == -1; i = i.add(BigInteger.ONE)) {
//			            // tell user which key is being checked
//			            String keyStr = bi.toString();
//			            System.out.print(keyStr);
//			            Thread.sleep(100);
//			            for (int j=0; j<keyStr.length();j++) {
//			                System.out.print("\b");
//			            }
//			            // decrypt and compare to known plaintext
//			            Blowfish.setKey(key);        
//			            plaintext = Blowfish.decryptToString(cipherText);
//			            if (plaintext.equals("May good flourish; Kia hua ko te pai")) {
//			               
//			            	
//			            	
//			            	out.println("Search Finished!");
//							System.out.println("Search Finished!");
//			            	
//			            	System.out.println("Plaintext found!");
//			                System.out.println(plaintext);
//			                out.println("Plaintext found!");
//			                out.println(plaintext);
//			                
//			                System.out.println("key is (hex) " + Blowfish.toHex(key) + " " + bi);
//			                System.out.println("Key Found!");
//			                out.println("key is (hex) " + Blowfish.toHex(key) + " " + bi);
//			                out.println("Key Found!");
//			                
//			                long stopTime = System.nanoTime();
//							long totalTime = stopTime - startTime;
//							
//							System.out.println("Elapsed Time: " + totalTime + "ns\n");
//							out.println("Elapsed Time: " + totalTime + "ns\n");
//			                
////							break outer;
//			               System.exit(-1);
//			            } 
//			            
//			            // try the next key
//			            bi = bi.add(BigInteger.ONE);
//			            key = Blowfish.asByteArray(bi,keySize);
//			        }
//			        
//			      
//			        
//			        //NO KEY FOUND!
//			        out.println("Search Finished!");
//					System.out.println("Search Finished!");
//			        
//			        System.out.println("No key found!");
//					
//			        //REQUEST FOR MORE WORK!
//			        out.println("Key NOT FOUND! - Client Available!");
//			        out.println("Search Ended @ Key:" + range);
//			        out.println("Requesting Work..");
//					System.out.println("Requesting Work..");
//					
//						
//				}
			}
			
//			outer:
//			while(line != null){
//				
//				
//				
//				line = in.readLine();	//Read Next Line..
//				
//			}
			
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
