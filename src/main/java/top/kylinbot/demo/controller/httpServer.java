package top.kylinbot.demo.controller;

import top.kylinbot.demo.modle.osuUser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class httpServer {

    private ServerSocket serverSocket;

    public httpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
//        serverSocket.setSoTimeout(10000);
        System.out.println("Server端口" + port + "将一直等待你的到来");
    }

    public int Run(osuUser user) {
//        while (isGetCode) {
        try {
            System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
            HttpRequestHandler request = new HttpRequestHandler(server);
            user.setCode(request.handle());
            server.close();
            return 0;
        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            return 1;
        } catch (IOException e) {
            System.out.println("Socket timed out!");
            e.printStackTrace();
            return 1;
        }
    }
//    }
}

class HttpRequestHandler {
    private Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }

    public String handle() throws IOException {
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
        GetString[0] = GetString[0].replace(" HTTP/1.1", "");
        GetString[0] = GetString[0].replace("GET /?", "");
        GetString[0] = GetString[0].replace("code=", "");
        GetString[0] = GetString[0].replace("&state=1579525246", "");

        System.out.println(GetString[0]);
        socket.getOutputStream().
                write(("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html; charset=utf-8\r\n" +
                        "\r\n" +
                        "<h1>现在你已经成功绑定KylinBot了</h1>\r\n" +
                        GetString[0]
                ).getBytes());
        return GetString[0];
    }
}
