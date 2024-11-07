import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Server {
    private static final int PORT = 8888;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server startup...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                InetAddress addr = clientSocket.getInetAddress();
                System.out.println("Client: Connection from " + addr.getHostName() + "/" + addr.getHostAddress());

                BufferedWriter initialWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                initialWriter.write("Connection established successfully.\n");
                initialWriter.flush();

                handleClientRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientRequest(Socket clientSocket) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))) {

            String requestLine = br.readLine();
            System.out.println("Request: " + requestLine);

            if (requestLine != null) {
                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String path = requestParts[1];

                if ("GET".equalsIgnoreCase(method)) {
                    handleGetRequest(bw, path);
                } else if ("POST".equalsIgnoreCase(method)) {
                    StringBuilder body = new StringBuilder();
                    while (!br.readLine().isEmpty()) {
                        // Skip headers until we reach the blank line
                    }
                    // Read POST body
                    while (br.ready()) {
                        body.append((char) br.read());
                    }
                    handlePostRequest(bw, path, body.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleGetRequest(BufferedWriter bw, String path) throws IOException {
        bw.write("HTTP/1.1 200 OK\r\n");
        bw.write("Date: " + getServerTime() + "\r\n");
        bw.write("Content-Type: text/plain\r\n");
        bw.write("\r\n");
        bw.write("GET request received for path: " + path + "\n");
        bw.flush();
    }

    private static void handlePostRequest(BufferedWriter bw, String path, String body) throws IOException {
        bw.write("HTTP/1.1 200 OK\r\n");
        bw.write("Date: " + getServerTime() + "\r\n");
        bw.write("Content-Type: text/plain\r\n");
        bw.write("\r\n");
        bw.write("POST request received for path: " + path + "\n");
        bw.write("Data: " + body);
        bw.flush();
    }

    private static String getServerTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new Date());
    }
}
