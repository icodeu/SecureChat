package com.icodeyou.securechat.util;

import com.icodeyou.securechat.model.CryptManager;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * 被邀请者初始化加密的线程
 */
public class InitCryptAcceptThreadOld extends Thread {

    private String ip;

    public static final int PORT_SERVER_CRYPT = 55555;
    public static final int PORT_CLIENT_CRYPT = 33333;

    // 负责加密初始化的ServerSocket
    ServerSocket serverSocket;
    // 负责加密初始化的Socket
    Socket socket;

    public InitCryptAcceptThreadOld(String ip) {
        this.ip = ip;
    }

    @Override
    public void run() {
        super.run();
        // todo 也许可以调换这两个方法的顺序会更好（不能换 不然一直阻塞了 除非再开线程 但这样就太不好了）

        // 被邀请者生成并发送公钥
        generateAndSend();

        // 被邀请者接收对方的公钥
        receivePublicKey();

    }

    private void generateAndSend() {
        try {
            //DH 生成 被邀请者 私钥和公钥
            Map<String, Object> map = DHUtil.initKey();
            byte[] privateKey2 = DHUtil.getPrivateKey(map);
            byte[] publicKey2 = DHUtil.getPublicKey(map);
            CryptManager.getInstance().setPrivateKeyAccept(privateKey2);
            CryptManager.getInstance().setPublicKeyAccept(publicKey2);
            // 被邀请者发送自己的公钥
            socket = new Socket(ip, PORT_SERVER_CRYPT);
            OutputStream os = socket.getOutputStream();
            os.write(publicKey2);
            os.flush();
            DebugUtil.print("InitCryptAcceptThread " + "发送DH公钥完毕");
            // 被邀请者接收发起者的公钥
            // 被邀请者根据自己的私钥和对方的公钥生成AES密钥
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket = null;
        }
    }

    private void receivePublicKey() {
        try {
            serverSocket = new ServerSocket(PORT_CLIENT_CRYPT);
            DebugUtil.print("正待等待客户端初始化加密的连接");
            Socket clientSocket = serverSocket.accept();
            System.out.println("有加密初始化客户端连接 IP为 " + clientSocket.getInetAddress().getHostAddress().toString());

            byte[] publicKey1 = new byte[1024];
            clientSocket.getInputStream().read(publicKey1);
            CryptManager.getInstance().setPublicKeySponsor(publicKey1);
            DebugUtil.print("服务器端收到发起者传来的公钥数据 >>>>>> " + new String(publicKey1));

            // 生成秘密密钥
            generateSecret();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void generateSecret() {
        try {
            byte[] privateKey = CryptManager.getInstance().getPrivateKeyAccept();
            byte[] publicKey = CryptManager.getInstance().getPublicKeySponsor();
            byte[] secretKey = DHUtil.getSecretKey(publicKey, privateKey);
            CryptManager.getInstance().setSecret(secretKey);
            // todo 生成了 secretkey   可以回调了
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
