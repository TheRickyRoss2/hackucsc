public interface SockServInterface{
   
   public static void main(String[] args)throws IOException;
   
   public static void updateLocation(HashMap<String, String> locations, String request);
   
   public static boolean parseForUpdate(String request);
   
   public static void sendLocation(String request);
   
   public static boolean locationParse(String request)
   
   public static HashMap<Integer, User> loadHashMap();
   
   public static void updateTable(User newUser);
   
   public static String packageFalseResponse();
   
   public static String packageTrueReponse();
   
   public static boolean isFriend(String request, HashMap<Integer, User> hashmap);
   
   public static HashMap<Integer, User> addUserToHashmap(User newUser, HashMap<Integer,User> hashmap);
   
   public static User parseNewUser(String request);
   
   public static boolean httpParseRequest(String request);
}