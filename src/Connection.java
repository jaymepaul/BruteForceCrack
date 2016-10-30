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
				
				if(line == null){
					
					long stopTime = System.nanoTime();
					long totalTime = stopTime - startTime;
					
					System.out.println("KEY HAS BEEN FOUND - MANAGER SHUT DOWN!");
					System.out.println("Elapsed Time: " + totalTime + "ns\n");

					System.exit(-1);
				}
				
				else if ( ( key.equals(endKey)) ) {
					
					System.out.println("Searched all possible solutions!");
					
					long stopTime = System.nanoTime();
					long totalTime = stopTime - startTime;
					
					System.out.println("Elapsed Time: " + totalTime + "ns\n");
					
					System.out.println("Key was not found! -- Manager will shut down!");
					System.exit(-1);
					break;
				}
				
				else if(line.equals("Requesting Work..")){
					
					response = "Find the Key with these Parameters..\n";
					response += "Server: StartKey | ChunkSize | CipherText | EndKey | KeySize | StartTime \n";
					response += "Server: | " + key + " | " + chunkSize + " | " + cipherText + " | " + endKey + " | " + keySize + " | " + startTime + "\n";
					
					response += "Server: Closing Connection..\n";	
					
					//client.connect("localhost")
					
					// send the response plus a return and newlines (as expected by readLine)
					networkPout.write(response+"\r\n");
					System.out.println("Server: "+response);
					// force the send to prevent buffering
					networkPout.flush();


			
				}
				else if(line.equals("Search Finished!")){
					
					line = networkBin.readLine();
					System.out.println(line);
					
	
					if(line.equals("Key Found!")){
						//Print Key
						//Shutdown Manager and Active Threads
						System.exit(-1);
					}
					else if(line.equals("Key NOT FOUND! - Client Available!")){
						
						line = networkBin.readLine();
						System.out.println(line);
						
						String[] rn = line.split(":");
						System.out.println(rn[1]);
						key = new BigInteger(rn[1]);
						continue;	//Look to allocate more work
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

