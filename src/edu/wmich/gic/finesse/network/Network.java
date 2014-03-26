package edu.wmich.gic.finesse.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

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
import edu.wmich.gic.finesse.drawable.GameGrid;

public class Network {
	private boolean init = false;
	public boolean client = false;
	public boolean server = false;
	public ServerSocket serv = null;
	public Socket connection = null;
	public InputStream in;
	public OutputStream out;
	public BufferedReader reader;
	public PrintStream writer;
	public String state = "connecting";
	private String address = "";

	public Network(final Game game,final GameGrid gameGrid){
		if(MainFinesse.useNetwork && MainFinesse.commandLineArgs.length > 0){
			if(MainFinesse.commandLineArgs[0].compareTo("brodie") == 0){
				System.out.println("Init Network");
				new Thread(new Runnable() {public void run() {
//					System.out.println("Thread init");
					try {
						address = (String)JOptionPane.showInputDialog(null,"I.P. Address","Address",
								JOptionPane.PLAIN_MESSAGE,null,null,InetAddress.getLocalHost().getHostAddress());
						serv = new ServerSocket();
						serv.bind(new InetSocketAddress(address, 9876));
						gameGrid.popupMessage = address;
						gameGrid.playingState = 4;
						System.out.println("Created Server");
						connection = serv.accept();
						in = connection.getInputStream();
						out = connection.getOutputStream();
						reader = new BufferedReader(new InputStreamReader(in));
						writer = new PrintStream(out, true);
						state = "connected";
						server = true;
						for (int i = 0; i < gameGrid.rows; i++) {
							for (int j = 0; j < gameGrid.columns; j++) {
								gameGrid.send(new Object[]{"map",i,j,gameGrid.mapArray[i][j].walkable});
							}
						}
					} catch (IOException e1) {

					}

					try {
						if (state == "connecting") {
							// Open a connection as a client.
							address = (String)JOptionPane.showInputDialog(null,"I.P. Address","Address",
									JOptionPane.PLAIN_MESSAGE,null,null,InetAddress.getLocalHost().getHostAddress());
							System.out.println("Connecting");
							connection = new Socket(address,9876);
							in = connection.getInputStream();
							out = connection.getOutputStream();
							reader = new BufferedReader(new InputStreamReader(in));
							writer = new PrintStream(out, true);
							state = "connected";
							client = true;
							System.out.println("Created Client");
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
//									writer.println("alive");
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
							System.out.println(e);
//							e.printStackTrace();
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
}
