package com.hackucsc;

import java.io.BufferedReader;
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
	private static class User{
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
		
		public String toString(){
			StringBuilder str= new StringBuilder();
			for(String user: this.friendsList){
				str.append(user.name+","+user.number+";");
			}
			return name;
			
		}

		public int getId() {
			return this.id;
		}

		public void setId(int id) {
			this.id= id;
		}
	}
	
	public static void main(String[] args)throws IOException{
		HashMap<Integer, User> hashmap = new HashMap<>();
		ServerSocket listener = new ServerSocket(9090);
		boolean updateTable = false;
		try{
			while(true){
				if(updateTable){
					hashmap = loadHashMap();
					updateTable = false;
				}
				Socket socket = listener.accept();
				try{
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					String request = (String) ois.readObject();
					ois.close();
					
					String responseString;
					boolean addUser = httpParseRequest(request);
					if(addUser){
						User newUser = parseNewUser(request);
						if(!hashmap.containsKey(newUser.getId())){
							hashmap = addUserToHashmap(newUser, hashmap);
						}
						updateTable(newUser);
						updateTable = true;
						id++;
						continue;
					}else{
						if(hashmap.isEmpty()) continue;
						String[] searchUser = parseForSearch(request);
						if(isFriend(searchUser, hashmap)){
							responseString = packageTrueReponse();
						}else{
							responseString = packageFalseResponse();
						}
					}
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(responseString);
					oos.close();
					
				}catch(IOException e){
					System.out.println("Error");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e){
			System.out.println("Error");
		}
		listener.close();
	}

	private static HashMap<Integer, User> loadHashMap() {
		HashMap<Integer, User> hashmap = new HashMap<>();
		try{
			String myDriver = "org.gjt.mm.mysql.Driver";
			String myUrl = "jdbc:mysql://localhost/test";
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "HackUCSC$@2017");
			String query = "SELECT * FROM users";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while(rs.next()){
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String phone = rs.getString("phone_number");
				String friends = rs.getString("friends");
				ArrayList<User> list = new ArrayList<>();
				String[] friendsSplit = friends.split(";");
				for(String user:friendsSplit){
					String[] userSplit = user.split(",");
					User newUser = new User();
					newUser.setId(id);
					newUser.setName(userSplit[0]);
					newUser.setNumber(userSplit[1]);
					list.add(newUser);
					hashmap.put(newUser.id, newUser);
					
				}
			}
			st.close();
		}
		catch(Exception e)
		{
			System.out.println("Error");
		}
		return hashmap;
	}


	private static void updateTable(User newUser) {
		try{
			String myDriver = "org.gjt.mm.mysql.Driver";
			String myUrl = "jdbc:mysql://localhost/test";
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "root", "HackUCSC$@2017");
			String query = "insert into users (id, name, phone_number, friends)"
					+ " values (?, ?, ?, ?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setInt(1, newUser.id);
			preparedStmt.setString(2, newUser.name);
			preparedStmt.setString(3,newUser.number);
			StringBuilder arr = new StringBuilder();
			for(User user: newUser.friendsList){
				arr.append(user.toString());
			}
			preparedStmt.setString(4, arr.toString());
			preparedStmt.executeQuery();
			conn.close();
		}
		catch(Exception e)
		{
			System.out.println("Error");
		}
	}

	private static String packageFalseResponse() {
		return "found=false;";
	}

	// creates a string which essentially says user was found
	// string response = "found=true;"
	// encode http response with this string
	private static String packageTrueReponse() {
		return "found=true;";
	}

	/*
	 * isFriend takes two users as arguments, one user which is the key
	 * and another friend which may or may not be in the friends list of the first user
	 * Search the hash table using the user's name and get the user object
	 * associated with the user name
	 * now search the arraylist for the friend's name
	 */
	private static boolean isFriend(String[] searchUser, HashMap<Integer, User> hashmap) {
		for(Map.Entry<Integer, User> e : hashmap.entrySet()){
			Object key = e.getKey();
			if(hashmap.get(key).equals(searchUser[0])){
				return hashmap.get(key).getFriendsList()
			}
		}
		return false;
	}

	/*
	 * takes user object as argument and hashes key according to user.name
	 * value is the object itself
	 * 
	 */
	private static HashMap<Integer, User> addUserToHashmap(User newUser, HashMap<Integer,User> hashmap) {
		hashmap.put(newUser.id, newUser);
		return hashmap;
	}

	/*
	 * breaks string into user object with the following format
	 * "username=exampleUsername;number=1234567890;numberOfFriends=3;friend=friend1,1234567890;friend=friend2,2346573451;friend=friend3,6789123454;"
	 *  I suggest separating by semicolon, and when you get to the friends part strip everything before the equals and substring using comma
	 */
	
	private static User parseNewUser(String request) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * check if we are adding using or friend searching
	 * format will be "addUser=True;payloadData......endPayload";
	 * I suggest using regex or some sort of substring cutting only at the first semicolon
	 * Another way is to format using some other expression, but you'll have to tell will or keving to package it the way you want
	 */

   private static boolean httpParseRequest(String request) {
		//Need cleanup, remove useless code(loop print)
		String[] split = request.split(";");
		int len = split.length;
		Patern p = Pattern.compile("=(.*)");
		Matcher m = p.matcher(split[0]);
		if (m.find()) {
		   String check = m.group(1);
		   if(check.equals("True")==true){
		      return true;
		   }
		}
		
		return false;
	}

}
