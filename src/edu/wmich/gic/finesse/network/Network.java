package edu.wmich.gic.finesse.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import edu.wmich.gic.finesse.Game;
import edu.wmich.gic.finesse.MainFinesse;
import edu.wmich.gic.finesse.FinesseGame.ScreenType;

public class Network {
	private boolean init = false;
	private boolean client = true;
	public ServerSocket server = null;
	public Socket connection = null;
	public InputStream in;
	public OutputStream out;
	public BufferedReader reader;
	public PrintStream writer;
	private String output = "";
	public String state = "connecting";

	public Network(final Game game){
		if(MainFinesse.commandLineArgs.length > 0){
			if(MainFinesse.commandLineArgs[0].compareTo("brodie") == 0){
				System.out.println("Init Network");
				new Thread(new Runnable() {public void run() {
					System.out.println("Thread init");

//					try {
//						server = new ServerSocket();
//						server.bind(new InetSocketAddress("localhost", 9876));
//						connection = server.accept();
//						in = connection.getInputStream();
//						out = connection.getOutputStream();
//						reader = new BufferedReader(new InputStreamReader(in));
//						writer = new PrintStream(out, true);
//						state = "connected";
//					} catch (IOException e1) {
//
//					}

					try {
						if (state == "connecting") {
							// Open a connection as a client.
							System.out.println("Connecting");
							connection = new Socket("localhost",9876);
							in = connection.getInputStream();
							out = connection.getOutputStream();
							reader = new BufferedReader(new InputStreamReader(in));
							writer = new PrintStream(out, true);
							state = "connected";
						}
						if (state == "connected") {
							System.out.println("Connected");
						}
						while (state == "connected") {
							// Read one line of text from the other side of
							// the connection, and report it to the user.
							//					writer.println("Hello");
							String input = reader.readLine();
							if (input == null){
								state = "closed";
								System.out.println("Network Closed");
								Thread.currentThread().stop();
							}
							else{
//								System.out.println(input);
								if(input.compareTo("alive") != 0){
									game.receiveNetwork(input);
									writer.println("alive");
								}
								if(input != ""){
									output += input+"\n";
								}
							}
						}
					}
					catch (Exception e) {
						// An error occurred.  Report it to the user, but not
						// if the connection has been closed (since the error
						// might be the expected error that is generated when
						// a socket is closed).
						if (state != "closed"){
							System.out.println("\nNetwork Error, don't worry about it");
							state = e.toString();
						}
					}
					finally {  // Clean up before terminating the thread.

					}
				}
				}).start();
			}
		}
	}

//	@Override
//	public void render(GameContainer gc, StateBasedGame game, Graphics g)
//			throws SlickException {
//		g.setColor(Color.green);
//		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
//		g.setColor(Color.blue);
//		g.drawString("Network Menu",300,50);
//		g.drawString("State: "+state,300,100);
//		g.drawString("Client: "+client,300,150);
//		g.drawString("Output: "+output,300,200);
//	}
//
//	@Override
//	public void update(GameContainer gc, StateBasedGame game, int delta)
//			throws SlickException {
//		if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
//			if(state == "connected"){
//				writer.println("Blah");
//				System.out.println("Blah");
//			}
//		}
//	}
//
//	@Override
//	public int getID() {
//		return ScreenType.NETWORKMENU.getValue();
//	}

}
