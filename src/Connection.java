/**
 * A separate thread that is created by the server. This thread is used
 * to interact with the client.
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Connection extends Thread
{

	private static AtomicInteger count = new AtomicInteger(0); // track the connection
	
	public Connection(Socket c) {
		client = c;
		count.getAndIncrement();
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
				
				if ( (line == null) || line.equals("bye")) {
					break;
				}
				else if(line.equals("Requesting Work..")){
					
					response = "Server: Find the Key with these Parameters..\n";
					response += "Server: StartKey: | ChunkSize: | CipherText:\n";
					
					response += "Server: Closing Connection..\n";	
					//client.close();
					
					// send the response plus a return and newlines (as expected by readLine)
					networkPout.write(response+"\r\n");
					System.out.println("Server: "+response);
					
					// force the send to prevent buffering
					networkPout.flush();

					//Do Search
					
//					networkPout.write("Re-Opening Connection.. \n");
//					networkPout.write("Key Not Found - Client Available!");
					//response = FOUND OR NOT
					//IF NOT FOUND response = not found, free from task
			
				}
				else if(line.equals("Search Finished!")){
					
					line = networkBin.readLine();
					System.out.println(line);
					
					if(line.equals("Key Found!")){
						//Print Key
						//Shutdown Manager and Active Threads
					}
					else if(line.equals("Key NOT FOUND! - Client Available!")){
						
						continue;	//Look to allocate more work
					}
				}
				
//				if ( (line == null) || line.equals("bye")) {
//					break;
//				}

				
//				// send the response plus a return and newlines (as expected by readLine)
//				networkPout.write(response+"\r\n");
//				System.out.println("Server: "+response);
//				// force the send to prevent buffering
//				networkPout.flush();
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

