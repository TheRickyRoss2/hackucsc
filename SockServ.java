package com.hackucsc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.sql.*;

public class SockServ {
	static int id = 1;

	private static class User {
		private int id;
		private String name;
		private String number;
		private String[] friendsList;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String[] getFriendsList() {
			return friendsList;
		}

		public void setFriendsList(String[] friendsList) {
			this.friendsList = friendsList;
		}

		public String toString() {
			StringBuilder str = new StringBuilder();
			for (String user : this.friendsList) {
				str.append(user + ";");
			}
			return name;

		}

		public int getId() {
			return this.id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}

	public static void main(String[] args) throws IOException {
		HashMap<Integer, User> hashmap = new HashMap<>();
		HashMap<String, String> locations = new HashMap<>();
		ServerSocket listener = new ServerSocket(9090);
		boolean updateTable = true;
		try {
			while (true) {
				// if(updateTable){
				// hashmap = loadHashMap();
				// updateTable = false;
				// }
				Socket socket = listener.accept();
				try {
					// ObjectInputStream ois = new
					// ObjectInputStream(socket.getInputStream());
					// String request = (String) ois.readObject();
					// ois.close();
					BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
					String request = inFromClient.readLine();
					String responseString;
					/*
					 * if(parseForUpdate(request)){ updateLocation(locations,
					 * request); continue; } if(!parseForUpdate(request)){
					 * sendLocation(request); continue; }
					 */
					if (getLocationFromServer(request)) {
						String input = request.substring(7);
						
						String location = getLocation(input, locations);
						if(location==null){
							outToClient.writeBytes("No location data available\n");
						}
						outToClient.writeBytes(location+"\n");
						continue;
					}
					
					if (pushLocationToServer(request)) {
						responseString = "Success\n";
						locations = updateLocation(locations, request);
						outToClient.writeBytes(responseString);
						continue;
					}
					
					if (httpParseRequest(request)) {
						String payload = request.substring(13, request.length());
						User newUser = parseNewUser(payload);
						if (!hashmap.containsKey(newUser.getId())) {
							hashmap = addUserToHashmap(newUser, hashmap);
						}
						// updateTable(newUser);
						// updateTable = true;
						id++;
						continue;
					} else {
						if (hashmap.isEmpty())
							continue;
						if (isFriend(request, hashmap)) {
							responseString = packageTrueReponse();
						} else {
							responseString = packageFalseResponse();
						}
					}
					/*
					 * ObjectOutputStream oos = new
					 * ObjectOutputStream(socket.getOutputStream());
					 * oos.writeObject(responseString); oos.close();
					 */
					outToClient.writeBytes(responseString);
				} catch (IOException e) {
					System.out.println("Error could not make socket");
				}
			}

		} catch (IOException e) {
			System.out.println("Error in outer while");
		}
		listener.close();
	}

	private static boolean getLocationFromServer(String request) {
	   String[] splitReq = request.split(";");
	   if(splitReq[0].equals("getLoc")){
		   return true;
	   }
		return false;
	}

	private static String getLocation(String input, HashMap<String, String> locations) {
		String[] splitIn = input.split(";");
		if(locations.containsKey(splitIn[0])){
			return(locations.get(splitIn[0]));
		}
		return null;
	}

	private static boolean pushLocationToServer(String request) {
		String[] splitReq = request.split(";");
		   if(splitReq[0].equals("pushLoc")){
			   return true;
		   }
			return false;
	}

	// store values into table
	public static HashMap<String,String> updateLocation(HashMap<String, String> locations, String request) {
		String sub = request.substring(8);
		String[] arr = sub.split(";");
		locations.put(arr[0], arr[1]);
		return locations;
	}

	// Request location based off phone number
	// This function will search table for location
	// request = "1234567890" phone number
	/*
	 * public static String sendLocation(String request) { // TODO
	 * Auto-generated method stub return }
	 * 
	 * //This function will take in a location and phone number //Stores
	 * location based off phone number public static void locationParse(String
	 * request) { // TODO Auto-generated method stub return; } /* private static
	 * HashMap<Integer, User> loadHashMap() { =======
	 * 
	 * public static HashMap<Integer, User> loadHashMap() { HashMap<Integer,
	 * User> hashmap = new HashMap<>(); try{ String myDriver =
	 * "org.gjt.mm.mysql.Driver"; String myUrl =
	 * "jdbc:mysql://localhost/pinger"; Class.forName(myDriver); Connection conn
	 * = DriverManager.getConnection(myUrl, "root", "HackUCSC$@2017"); String
	 * query = "SELECT * FROM users"; Statement st = conn.createStatement();
	 * ResultSet rs = st.executeQuery(query); while(rs.next()){ int id =
	 * rs.getInt("id"); String name = rs.getString("name"); String phone =
	 * rs.getString("phone_number"); String friends = rs.getString("friends");
	 * String[] friendsSplit = friends.split(";"); User newUser = new User();
	 * newUser.setId(id); newUser.setName(name); newUser.setNumber(phone);
	 * 
	 * newUser.friendsList = friendsSplit.clone();
	 * 
	 * hashmap.put(newUser.id, newUser);
	 * 
	 * }
	 * 
	 * st.close(); } catch(Exception e) { System.out.println("Error"); } return
	 * hashmap; }
	 * 
	 * 
	 * public static void updateTable(User newUser) { try{ String myDriver =
	 * "org.gjt.mm.mysql.Driver"; String myUrl =
	 * "jdbc:mysql://localhost/pinger"; Class.forName(myDriver); Connection conn
	 * = DriverManager.getConnection(myUrl, "root", "HackUCSC$@2017"); String
	 * query = "insert into users (id, name, phone_number, friends)" +
	 * " values (?, ?, ?, ?)"; PreparedStatement preparedStmt =
	 * conn.prepareStatement(query); preparedStmt.setInt(1, newUser.id);
	 * preparedStmt.setString(2, newUser.name);
	 * preparedStmt.setString(3,newUser.number); StringBuilder arr = new
	 * StringBuilder(); for(String user: newUser.friendsList){
	 * arr.append(user+";"); } preparedStmt.setString(4, arr.toString());
	 * preparedStmt.executeQuery(); conn.close(); } catch(Exception e) {
	 * System.out.println(e); } } <<<<<<< HEAD
	 */
	public static String packageFalseResponse() {
		return "found=false;\n";
	}

	// creates a string which essentially says user was found
	// string response = "found=true;"
	// encode http response with this string
	private static String packageTrueReponse() {
		return "found=true;\n";
	}

	/*
	 * isFriend takes two users as arguments, one user which is the key and
	 * another friend which may or may not be in the friends list of the first
	 * user Search the hash table using the user's name and get the user object
	 * associated with the user name now search the arraylist for the friend's
	 * name
	 */
	public static boolean isFriend(String request, HashMap<Integer, User> hashmap) {
		// parse msg to id and phone number

		String msg = request.substring(17);
		int id;
		String[] message = msg.split(";");
		id = Integer.parseInt(message[0]);
		String[] str = message[1].split(",");
		String phone = str[1];
		if (hashmap.containsKey(id)) {
			User kek = hashmap.get(id);
			for (String checker : kek.friendsList) {
				if (checker.contains(phone))
					return true;
			}
		}
		return false;
	}

	/*
	 * takes user object as argument and hashes key according to user.name value
	 * is the object itself
	 * 
	 */
	public static HashMap<Integer, User> addUserToHashmap(User newUser, HashMap<Integer, User> hashmap) {
		hashmap.put(newUser.id, newUser);
		return hashmap;
	}

	/*
	 * breaks string into user object with the following format
	 * "username=exampleUsername;number=1234567890;friend=friend1,1234567890;friend=friend2,2346573451;friend=friend3,6789123454;"
	 * I suggest separating by semicolon, and when you get to the friends part
	 * strip everything before the equals and substring using comma
	 */

	public static User parseNewUser(String request) {
		User newUser = new User(); // Create New User
		newUser.id = id;

		String[] split = request.split(";"); // Split String and declare friends
												// String array
		int len = split.length;
		newUser.friendsList = new String[len - 2];

		// Field input
		Pattern p = Pattern.compile("=(.*)"); // Give name field
		Matcher m = p.matcher(split[0]);
		if (m.find()) {
			newUser.name = m.group(1);
		}
		m = p.matcher(split[1]); // Give number field
		if (m.find()) {
			newUser.number = m.group(1);
		}
		for (int i = 2; i < len; i++) { // Give friend string array field
			m = p.matcher(split[i]);
			if (m.find()) {
				newUser.friendsList[i - 2] = m.group(1);
			}
		}
		return newUser;
	}

	/*
	 * check if we are adding using or friend searching format will be
	 * "addUser=True;payloadData......endPayload"; I suggest using regex or some
	 * sort of substring cutting only at the first semicolon Another way is to
	 * format using some other expression, but you'll have to tell will or
	 * keving to package it the way you want
	 */

	public static boolean httpParseRequest(String request) {
		String[] split = request.split(";"); // Split string to array by
												// semi-colon
		int len = split.length;
		Pattern p = Pattern.compile("=(.*)");// create group after "=" to get
												// value

		Matcher m = p.matcher(split[0]);
		if (m.find()) {
			String check = m.group(1);
			if (check.equals("True") == true) {// Check if request is true or
												// false
				return true;
			}
		}

		return false;
	}

}
