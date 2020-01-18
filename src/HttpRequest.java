//Student Name: Aditee Dnyaneshwar Dakhane
//UTA ID: 1001745502
//Site referred : Programming Assignment 1_reference_Java.pdf provided by Professor on Canvas - https://uta.instructure.com/courses/28912/assignments/474421
//Site referred : http://www.shubhsblog.com/programming/multithreaded-webserver-java.html
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpRequest implements Runnable {
	final static String CRLF = "\r\n";            								 //For convenience
	Socket socket;
	public HttpRequest(Socket socket) throws Exception             			     //Construct object to process HTTP request message        
	{
		this.socket = socket;
	}
	
	public void run()															// Implement the run() method of the Runnable interface.
	{
		try {
			processRequest();													//Call to processRequest method
		} catch (Exception e) {
			System.out.println(e);                                              //Catch if any exception is there
		}
	}
	private void processRequest() throws Exception
	{
		InputStream is = socket.getInputStream();                                // Set up input stream filter to read the data
		DataOutputStream os = new DataOutputStream(socket.getOutputStream());    //Creates a new output stream to write data
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));       //Creates an object of bufferedreader to read data from input stream
		String requestLine = br.readLine();                                      //Read a line from bufferedreader
		System.out.println();                                                    //Echoes request line out to screen
		System.out.println(requestLine);


		
		InetAddress incomingAddress = socket.getInetAddress();                   //The following obtains the IP address of the incoming connection.
		String ipString= incomingAddress.getHostAddress();


		System.out.println("The incoming address is:   " + ipString);
		
		StringTokenizer tokens = new StringTokenizer(requestLine);               //String Tokenizer is used to extract file name from this class.
		tokens.nextToken(); 													 // skip over the method, which should be “GET”
		String fileName = tokens.nextToken();


		
		fileName = "." + fileName;												// Prepend a “.” so that file request is within the current directory.
		String headerLine = null;
		while ((headerLine = br.readLine()).length() != 0) { 					//While the header still has text, print it
			System.out.println(headerLine);
		}


		
		FileInputStream fis = null;                                            // Open the requested file.
		boolean fileExists = true;
		try {
			fis = new FileInputStream(fileName);                               //Create a file object for the requested file
		} catch (FileNotFoundException e) {
			fileExists = false;
		}

		//Construct the response message
		String statusLine = null; 											//Set initial value of statusline to null
		String contentTypeLine = null;                                      //Set initial value of statusline to null
		String entityBody = null;                                           //Set initial value of statusline to null
		if (fileExists) {
			statusLine = "HTTP/1.1 200 OK: ";                              //HTTP status 200 if file is found
			contentTypeLine = "Content-Type: " +
					contentType(fileName) + CRLF;
		} else {
			statusLine = "HTTP/1.1 404 Not Found: ";                       //HTTP status 404 if file is not found
			contentTypeLine = "Content-Type: text/html" + CRLF;
			entityBody = "<HTML><BODY>HTTP/1.1 404 Not Found</BODY></HTML>";     //Display the HTTP status
		}
		//End of response message construction
		
		
		
		os.writeBytes(statusLine);											// Send the status line.
		os.writeBytes(contentTypeLine);										// Send the content type line.
		os.writeBytes(CRLF);											    // Send a blank line to indicate the end of the header lines.
		if (fileExists) { 												    // Send a blank line to indicate the end of the header lines.
			sendBytes(fis, os);
			fis.close();
		} else {
			os.writeBytes(entityBody);
		}
		os.close();															 //Close output stream 
		br.close();															 //Close Bufferedreader
		socket.close();														//Close the socket connection
	}


	
	private static void sendBytes(FileInputStream fis, OutputStream os)     //Need this method for sendBytes function called in processRequest
			throws Exception
	{
		
		byte[] buffer = new byte[1024];										//Need this one for sendBytes function called in processRequest
		int bytes = 0;														//Set initial value of bytes to null
		while((bytes = fis.read(buffer)) != -1 ) {                         	// Copy requested file into the socket’s output stream.
			os.write(buffer, 0, bytes);
		}
	}


	private static String contentType(String fileName)
	{
		if(fileName.endsWith(".htm") || fileName.endsWith(".html"))       //File can have .htm or .html extension
			return "text/html";
		if(fileName.endsWith(".jpg"))                                     //File can have .jpg extension
			return "text/jpg";
		if(fileName.endsWith(".gif"))                                     //File can have .gif extension
			return "text/gif";
		return "application/octet-stream";
	}
}
