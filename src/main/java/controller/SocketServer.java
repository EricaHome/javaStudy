package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Erica
 * @date 2021/1/21 21:52
 * @description BIO
 */
public class SocketServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9000);
        while (true) {
            System.out.println("等待连接。。。");
            final Socket clientSocket = serverSocket.accept();
            System.out.println("有客户端连接了。。。");
//            handler(clientSocket);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        handler(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static void handler(Socket clientSocket) throws IOException {
        byte[] bytes = new byte[1024];
        System.out.println("准备read。。。");
        //接收客户端的数据，阻塞方法，没有数据可读时就阻塞
        int read = clientSocket.getInputStream().read(bytes);
        System.out.println("read完毕。。。");
        if (read != -1) {
            System.out.println("接收到客户端的数据：" + new String(bytes));
        }
        clientSocket.getOutputStream().write("HelloClient".getBytes());
        clientSocket.getOutputStream().flush();
    }

}
