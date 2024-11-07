import java.io.*;
import java.net.Socket;

public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8888;

    public static void main(String[] args) {
        // 发送 GET 请求
        sendGetRequest("/index.html");
        // 发送 POST 请求
        sendPostRequest("/submit", "name=John&age=30");
    }

    private static void sendGetRequest(String path) {
        try (Socket socket = new Socket(HOST, PORT)) {
            BufferedReader initialReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String connectionMessage = initialReader.readLine();
            System.out.println("Server: " + connectionMessage);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("GET " + path + " HTTP/1.1\r\n");
            bw.write("Host: " + HOST + "\r\n");
            bw.write("Connection: keep-alive\r\n");
            bw.write("\r\n");
            bw.flush();

            readResponse(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPostRequest(String path, String data) {
        try (Socket socket = new Socket(HOST, PORT)) {
            BufferedReader initialReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String connectionMessage = initialReader.readLine();
            System.out.println("Server: " + connectionMessage);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("POST " + path + " HTTP/1.1\r\n");
            bw.write("Host: " + HOST + "\r\n");
            bw.write("Content-Type: application/x-www-form-urlencoded\r\n");
            bw.write("Content-Length: " + data.length() + "\r\n");
            bw.write("Connection: keep-alive\r\n");
            bw.write("\r\n");
            bw.write(data);
            bw.write("\r\n");
            bw.flush();

            readResponse(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readResponse(Socket socket) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String responseLine;
        System.out.println("Response:");
        while ((responseLine = br.readLine()) != null) {
            System.out.println(responseLine);
        }
        System.out.println("==================================================");
    }
}