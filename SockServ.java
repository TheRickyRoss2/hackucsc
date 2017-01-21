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

import java.util.ArrayList;
import java.sql.*;

public class SockServ {
	
	private class User{
		private String name;
		private String number;
		private ArrayList<User> friendsList;
		
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

		public ArrayList<User> getFriendsList() {
			return friendsList;
		}

		public void setFriendsList(ArrayList<User> friendsList) {
			this.friendsList = friendsList;
		}
		
	}
	
	public static void main(String[] args)throws IOException{
		HashMap<String, User> hashmap = new HashMap<>();
		ServerSocket listener = new ServerSocket(9090);
		try{
			while(true){
				Socket socket = listener.accept();
				try{
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					String request = (String) ois.readObject();
					ois.close();
					
					String responseString;
					boolean addUser = httpParseRequest(request);
					if(addUser){
						User newUser = parseNewUser(request);
						if(!hashmap.containsKey(newUser.getName())){
							hashmap = addUserToHashmap(newUser, hashmap);
						}
						continue;
					}else{
						if(hashmap.isEmpty()) continue;
						User[] searchUser = parseForSearch(request);
						if(isFriend(searchUser, hashmap)){
							responseString = packageTrueReponse();
						}else{
							responseString = packageFalseResponse();
						}
					}
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(responseString);
					oos.close();
					dumpHashMap(hashmap);
					
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
	
	private static HashMap<String, User> loadHashMap() {
		// TODO Auto-generated method stub
		return null;
	}


	private static void dumpHashMap(HashMap<String, User> hashmap) {
		// TODO Auto-generated method stub
		
	}

	private static String packageFalseResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	// creates a string which essentially says user was found
	// string response = "found=true;"
	// encode http response with this string
	private static String packageTrueReponse() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * isFriend takes two users as arguments, one user which is the key
	 * and another friend which may or may not be in the friends list of the first user
	 * Search the hash table using the user's name and get the user object
	 * associated with the user name
	 * now search the arraylist for the friend's name
	 */
	private static boolean isFriend(User[] searchUser, HashMap<String, User> hashmap) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * breaks string into two users.
	 * formatting is "user=exampleUser;friend=exampleFriend;"
	 * take the two names and store them in the array with
	 * user[0] being the exampleUser and user[1]=exampleFriend
	 */
	private static User[] parseForSearch(String request) {
		User[] userAndFriend = new User[2];
		// TODO Auto-generated method stub
		return userAndFriend;
	}

	/*
	 * takes user object as argument and hashes key according to user.name
	 * value is the object itself
	 * 
	 */
	private static HashMap<String, User> addUserToHashmap(User newUser, HashMap<String,User> hashmap) {
		return hashmap;
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return false;
	}
}
