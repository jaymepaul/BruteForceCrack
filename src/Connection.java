/**
 * A separate thread that is created by the server. This thread is used
 * to interact with the client.
 */

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Connection extends Thread
{

	private static AtomicInteger count = new AtomicInteger(0); // track the connection
	
	//Search Paramters..
	BigInteger key;
	BigInteger chunkSize;
	BigInteger endKey;
	String cipherText;
	int keySize;
	long startTime;
	
	public Connection(Socket c, BigInteger key, int keySize, BigInteger chunkSize, String cipherText, BigInteger endKey, long startTime) {
		client = c;
		count.getAndIncrement();
		
		this.key = key;
		this.chunkSize = chunkSize;
		this.endKey = endKey;
		this.cipherText = cipherText;
		this.keySize = keySize;
		this.startTime = startTime;
	} 



 
	/**
	 * this method is invoked as a separate thread
	 */
	public void run() {
		BufferedReader networkBin = null;
		OutputStreamWriter networkPout = null;
		
		try {
			/**
			 * get the input and output streams associated with the socket.
			 */
			networkBin = new BufferedReader(new InputStreamReader(client.getInputStream()));
			networkPout = new OutputStreamWriter(client.getOutputStream());

			/**
			 * the following successively reads from the input stream and returns
			 * what was read. The loop terminates with ^D or with the string "bye\r\n"
			 * from the  input stream.
			 */
			String response = null;
			while (true) {
				
				String line = networkBin.readLine();
				System.out.println("Client "+count+": "+line);
				
				//Condition: Check to see if we have exhausted ALL key-spaces
				//If so, then report time it took and shut down manager
				if ( ( key.equals(endKey)) ) {
					
					System.out.println("\nServer: Key Space has been exhausted!");
					System.out.println("Server: Searched all possible solutions! - No work left to be done!");
					
					long stopTime = System.nanoTime();
					long totalTime = stopTime - startTime;
					
					System.out.println("Total Search Time: " + totalTime + "ns\n");
					
					System.out.println("Key was not found! -- Manager will shut down!");
					System.exit(-1);
					break;
				}
				//Condition: When a client requests for work.. 
				//Feed it the appropriate search parameters..
				else if(line.equals("Requesting Work..")){
					
					
					response = "Find the Key with these Parameters..\n";
					response += "Server: StartKey | ChunkSize | CipherText | EndKey | KeySize | StartTime \n";
					response += "Server: | " + key + " | " + chunkSize + " | " + cipherText + " | " + endKey + " | " + keySize + " | " + startTime + "\n";
					
					response += "Server: Sending Search Parameters..\n";	
					
					// send the response plus a return and newlines (as expected by readLine)
					networkPout.write(response+"\r\n");
					System.out.println("\nServer: "+response);
					// force the send to prevent buffering
					networkPout.flush();
			
				}
				//Condition: When a client indicates they have finished executing their task
				//Check to see if key found or not - allocate more work | shutdown
				else if(line.equals("Search Finished!")){
					
					line = networkBin.readLine();
					System.out.println();
					
					if(line.equals("Key Found!")){
						//Print Key
						//Shutdown Manager and Active Threads
						long stopTime = System.nanoTime();
						long totalTime = stopTime - startTime;
						
						line = networkBin.readLine(); line = networkBin.readLine();
						
						//PlainText
						System.out.println("Server: PlainText: " + line);
						//Key
						line = networkBin.readLine();
						System.out.println("Server: "+line);
						//Client Time
						line = networkBin.readLine();
						System.out.println("Server: "+line);
						
						System.out.println("Server: KEY HAS BEEN FOUND - MANAGER SHUT DOWN!");
						System.out.println("Server: Total Search Time: " + totalTime + "ns");
						
						System.exit(-1);
					}
					else if(line.equals("KEY NOT FOUND! - Client Available!")){
						
						line = networkBin.readLine();
						System.out.println(line);
						
						String[] rn = line.split(":");
						key = new BigInteger(rn[1]);
						
						//Check to see if current key has exhausted key space
						if(key.equals(endKey)){
							System.out.println("\nServer: Key Space has been exhausted!");
							System.out.println("Server: Searched all possible solutions! - No work left to be done!");
							
							long stopTime = System.nanoTime();
							long totalTime = stopTime - startTime;
							
							System.out.println("Total Search Time: " + totalTime + "ns\n");
							
							System.out.println("Key was not found! -- Manager will shut down!");
							System.exit(-1);
							break;
						}
						else{
							line = "Requesting Work..";	//Look to allocate more work
						}
					}
				}
				
				

			}
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			try {
				if (networkBin != null)
					networkBin.close();
				if (networkPout != null)
					networkPout.close();
				if (client != null)
					client.close();
			}
			catch (IOException ioee) {
				System.err.println(ioee);
			}
		}
	}

	private Socket client;
}

