package top.kylinbot.demo.controller;

import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities;
import org.apache.http.impl.bootstrap.HttpServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

public class httpServer implements Runnable {

    private ServerSocket serverSocket;

    public httpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
//        serverSocket.setSoTimeout(10000);
        System.out.println("Server端口" + port + "将一直等待你的到来");
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
//                DataInputStream in = new DataInputStream(server.getInputStream());
//                System.out.println(in.readUTF());
//                DataOutputStream out = new DataOutputStream(server.getOutputStream());
//                out.writeUTF("谢谢连接我：" + server.getLocalSocketAddress() + "\nGoodbye!");

                HttpRequestHandler request = new HttpRequestHandler(server);
                request.handle();

                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

class HttpRequestHandler {
    private Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void handle() throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        char[] charBuf = new char[1024];
        int mark;
        while ((mark = isr.read(charBuf)) != -1) {
            builder.append(charBuf, 0, mark);
            if (mark < charBuf.length) {
                break;
            }
        }
        String[] GetString = builder.toString().split("\r\n");
        GetString[0] = GetString[0].replace(" HTTP/1.1","");
        GetString[0] = GetString[0].replace("GET /?","");
        System.out.println(GetString[0]);
        socket.getOutputStream().
                write(("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html; charset=utf-8\r\n" +
                        "\r\n" +
                        "<h1>现在你已经成功绑定KylinBot了</h1>\r\n" +
                        GetString[0]
                ).getBytes());
    }
}
