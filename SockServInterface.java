public interface SockServInterface{
   
   public static void main(String[] args)throws IOException;
   
   pubic static void updateLocation(HashMap<String, String> locations, String request);
   
   pubic static boolean parseForUpdate(String request);
   
   pubic static void sendLocation(String request);
   
   pubic static boolean locationParse(String request)
   
   pubic static HashMap<Integer, User> loadHashMap();
   
   pubic static void updateTable(User newUser);
   
   pubic static String packageFalseResponse();
   
   pubic static String packageTrueReponse();
   
   pubic static boolean isFriend(String request, HashMap<Integer, User> hashmap);
   
   pubic static HashMap<Integer, User> addUserToHashmap(User newUser, HashMap<Integer,User> hashmap);
   
   pubic static User parseNewUser(String request);
   
   pubic static boolean httpParseRequest(String request);
}