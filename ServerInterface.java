public interface ServerInterface{

    //Checks for send request (true) vs reply (false)
    //String host is host number
    //String client is receiver number
    public static void Checkup(boolean send, String host, String client);

}