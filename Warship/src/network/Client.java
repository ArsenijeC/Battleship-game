package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public Client(String host) throws IOException {
        int port = 2030;

        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader keyboard= new BufferedReader(new InputStreamReader(System.in));

        String message;
        while ((message = in.readLine()) != null) {
            if(message.equals("END")) continue;
            if(message.equals("CLEAR")) {
                try{
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (InterruptedException e) {}
            }else if(message.equals("INPUT")) {
                String input = keyboard.readLine();
                out.println(input);
            }
            else if(message.equals("GAMEOVER")){
                break;
            }
            else{
                System.out.println(message);
            }
        }
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        new Client(host);
    }

}
