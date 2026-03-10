package game;

import network.Client;
import network.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Usage: java -jar Battleship.jar server");
            System.out.println("       java -jar Battleship.jar client <ip>");
            return;
        }
        if(args[0].equals("server")){
            new Server();
        } else if(args[0].equals("client")){
            String ip = args.length > 1 ? args[1] : "localhost";
            new Client(ip);
        }
    }
}