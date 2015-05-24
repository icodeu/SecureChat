package com.icodeyou.securechat.util;

import com.icodeyou.securechat.model.CryptManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * 发起者初始化加密的线程
 */
public class InitCryptSponsorThreadOld extends Thread {

    private String ip;

    public static final int PORT_SERVER_CRYPT = 55555;
    public static final int PORT_CLIENT_CRYPT = 33333;

    // 负责加密初始化的ServerSocket
    ServerSocket serverSocket;
    // 负责加密初始化的Socket
    Socket socket;

    public InitCryptSponsorThreadOld(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        super.run();
        // todo 也许可以调换这两个方法的顺序会更好（不能换 不然一直阻塞了 除非再开线程 但这样就太不好了）

        // 发起者接收对方的公钥
        receivePublicKey();

        // 被邀请者生成并发送公钥
        //generateAndSend();


    }

    private void receivePublicKey() {
        try {
            serverSocket = new ServerSocket(PORT_SERVER_CRYPT);
            DebugUtil.print("正待等待客户端初始化加密的连接");
            Socket clientSocket = serverSocket.accept();
            System.out.println("有加密初始化客户端连接 IP为 " + clientSocket.getInetAddress().getHostAddress().toString());

            byte[] publicKey2 = new byte[1024];
            clientSocket.getInputStream().read(publicKey2);
            CryptManager.getInstance().setPublicKeyAccept(publicKey2);
            DebugUtil.print("服务器端收到被邀请者传来的公钥数据 >>>>>> " + new String(publicKey2));
            // 生成发起者端公钥和私钥
            generateAndSend(publicKey2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void generateAndSend(byte[] publicKey2) {
        try {
            //DH 生成 发起者 私钥和公钥
            Map<String, Object> map = DHUtil.initKey(publicKey2);
            byte[] privateKey1 = DHUtil.getPrivateKey(map);
            byte[] publicKey1 = DHUtil.getPublicKey(map);
            CryptManager.getInstance().setPrivateKeySponsor(privateKey1);
            CryptManager.getInstance().setPublicKeySponsor(publicKey1);
            // 发起者发送自己的公钥
            socket = new Socket(ip, PORT_CLIENT_CRYPT);
            OutputStream os = socket.getOutputStream();
            os.write(publicKey1);
            os.flush();
            DebugUtil.print("InitCryptSponsorThread " + "发送DH公钥完毕");
            // 发起者根据自己的私钥和对方的公钥生成AES密钥
            generateSecret();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket = null;
        }
    }


    private void generateSecret() {
        try {
            byte[] privateKey = CryptManager.getInstance().getPrivateKeySponsor();
            byte[] publicKey = CryptManager.getInstance().getPublicKeyAccept();
            byte[] secretKey = DHUtil.getSecretKey(publicKey, privateKey);
            CryptManager.getInstance().setSecret(secretKey);
            // todo 生成了 secretkey   可以回调了
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
