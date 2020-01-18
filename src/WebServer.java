//Student Name: Aditee Dnyaneshwar Dakhane
//UTA ID: 1001745502
//Site referred : Programming Assignment 1_reference_Java.pdf provided by Professor on Canvas - https://uta.instructure.com/courses/28912/assignments/474421
//Site referred : http://www.shubhsblog.com/programming/multithreaded-webserver-java.html
import java.net.ServerSocket;
import java.net.Socket;
public final class WebServer {
	public static void main(String argv[]) throws Exception
	{
		int port =80;                                               //Port 80 is assigned to Server 
		ServerSocket WebSocket = new ServerSocket(port);
		System.out.println("Server started running on port 80");
		while (true) {
			
			Socket connectionSocket = WebSocket.accept();           // Listen for a TCP connection request.
			HttpRequest request = new HttpRequest(connectionSocket);//Construct object to process HTTP request message
			Thread thread = new Thread(request);                    //Construct thread object for every incoming request
			thread.start();                                         //start thread for each client request
		}
	}
}
