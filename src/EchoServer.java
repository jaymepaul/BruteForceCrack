/**
 * An echo server listening on port passed as an argument. This server reads 
 * from the client and echoes back the result. When the client enters the 
 * string "bye", the server closes the connection.
 *
 */

import java.net.*;
import java.util.Scanner;
import java.io.*;
import java.math.*;
import java.math.BigInteger;

public class  EchoServer 
{
	public static void main(String[] args) throws IOException {

//=================== INITIALIZE KEYS, SIZE & CIPHERTEXT ===============================		
		
		BigInteger key = new BigInteger(args[0]);
		int keySize = Integer.parseInt(args[1]);
      
		BigInteger var = new BigInteger("256");
		BigInteger endKey = var.pow(keySize);
		
		String cipherText = args[2];
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Total Number of Clients:");
		
		BigInteger noClients = new BigInteger(sc.next());
		BigInteger chunkSize = endKey.divide(noClients);
		
//================================================================================		
		ServerSocket sock = null;

		try {
			// establish the socket
			sock = new ServerSocket(0);
			System.out.println("Waiting for connections on " + sock.getLocalPort());

			/**
			 * listen for new connection requests.
			 * when a request arrives, pass the socket to
			 * a separate thread and resume listening for
			 * more requests. 
			 * creating a separate thread for each new request
			 * is known as the "thread-per-message" approach.
			 */
			while (true) {
				// now listen for connections
				Socket client = sock.accept();	

				//Adjust chunkSize accordingly
				if(key.add(chunkSize).compareTo(endKey) == 1){
					//Compute difference between current key to finish
					BigInteger diff = endKey.subtract(key);
					chunkSize = diff.divide(noClients);
				}
				
				// service the connection in a separate thread
				Connection c = new Connection(client, key, keySize, chunkSize, cipherText, endKey);
				
				key = key.add(chunkSize);		//Increment start key
				c.start();
				
				
			}
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		}
		finally {
			if (sock != null)
				sock.close();
		}
	}
}
