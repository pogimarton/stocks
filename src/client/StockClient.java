package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import shared.*;

public class StockClient {
	private static String host = "192.168.1.3";
	private static int port = 12000;
	private Socket socket;

	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public StockClient() throws UnknownHostException, IOException {
		super();

		this.socket = new Socket(host, port);

		this.oos = null;
		this.ois = null;
	}

	@SuppressWarnings("rawtypes")
	public StockServerResponse processRequest(StockClientRequest request) {
		StockServerResponse response = null;
		try {
			// socket =
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());

			oos.writeObject(request);
			response = (StockServerResponse) ois.readObject();

			oos.close();
			ois.close();
			socket.close();

			oos = null;
			ois = null;
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return response;
	}
}
