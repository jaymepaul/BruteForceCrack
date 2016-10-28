/**
 * An echo server listening on port passed as an argument. This server reads 
 * from the client and echoes back the result. When the client enters the 
 * string "bye", the server closes the connection.
 *
 */

import java.net.*;
import java.io.*;
import java.math.BigInteger;

public class  EchoServer 
{
	public static void main(String[] args) throws IOException {

//		BigInteger bi = new BigInteger(args[0]);
//        int keySize = Integer.parseInt(args[1]);
//        
//        byte[] key = Blowfish.asByteArray(bi, keySize);
//        byte[] ciphertext = Blowfish.fromBase64(args[2]);
//        
//        int endSize = (int) Math.pow(8 * keySize, 2.0);
//        byte[] endKey = Blowfish.asByteArray(bi, endSize);
		
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

				// service the connection in a separate thread
				Connection c = new Connection(client);
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
