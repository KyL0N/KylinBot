package top.kylinbot.demo.controller;

import top.kylinbot.demo.modle.osuUser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class HttpServer implements Runnable {

    private ServerSocket serverSocket;

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server端口" + port + "将一直等待你的到来");
    }

    public int Run(osuUser user) {

        try {
            System.out.println("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("远程主机地址：" + server.getRemoteSocketAddress());
            HttpRequestHandler request = new HttpRequestHandler(server);
            user.setCode(request.handle());
            //关闭socket连接
            server.close();
            //关闭ServerSocket监听
            serverSocket.close();
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

    @Override
    public void run() {

    }

    public void stop(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
