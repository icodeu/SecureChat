package com.icodeyou.securechat.util;

import android.os.Handler;

import com.icodeyou.securechat.R;
import com.icodeyou.securechat.model.CryptManager;
import com.icodeyou.securechat.model.PubkeyManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * 被邀请者初始化加密的线程
 */
public class InitCryptAcceptThread extends Thread {

    private String ip;
    private Handler handler;

    public static final int PORT_SERVER_CRYPT = 55555;
    public static final int PORT_CLIENT_CRYPT = 33333;

    public InitCryptAcceptThread(String ip, Handler handler) {
        this.ip = ip;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        try {
            //DH 生成 被邀请者 私钥和公钥
            /*
            Map<String, Object> map = DHUtil.initKey();
            byte[] privateKey2 = DHUtil.getPrivateKey(map);
            byte[] publicKey2 = DHUtil.getPublicKey(map);
            CryptManager.getInstance().setPrivateKeyAccept(privateKey2);
            CryptManager.getInstance().setPublicKeyAccept(publicKey2);
            */

            // 以下为新增加的 固定了DH
            Thread.sleep(5000);
            String pubkeyString = "3081e030819706092a864886f70d010301308189024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e170240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca402020180034400024100c2519fdd8529a6a1182d43fc1080777f11ddca0e1084b0953a8a48e2e3328002707d60bca85e5ced6284ab51340ee7794ec756e2dfda67d2eadbe4dbc084806d";
            String prikeyString = "3081d202010030819706092a864886f70d010301308189024100fca682ce8e12caba26efccf7110e526db078b05edecbcd1eb4a208f3ae1617ae01f35b91a47e6df63413c5e12ed0899bcd132acd50d99151bdc43ee737592e170240678471b27a9cf44ee91a49c5147db1a9aaf244f05a434d6486931d2d14271b9e35030b71fd73da179069b32e2935630e1c2062354d0da20a6c416e50be794ca4020201800433023100aa6fe81db53006e1a1ddaf4e43e9600f03dd807eae2804d7a394d1ad69deb68c2d92db2a4f6c6443647de15021d1021d";

            byte[] publicKey2 = hexStringToBytes(pubkeyString);
            byte[] privateKey2 = hexStringToBytes(prikeyString);
            CryptManager.getInstance().setPrivateKeyAccept(privateKey2);
            CryptManager.getInstance().setPublicKeyAccept(publicKey2);
            // 以上为新增加的 固定了DH

            PubkeyManager pubkeyManager = new PubkeyManager(publicKey2);
            // 被邀请者发送自己的公钥
            Socket socket = new Socket(ip, PORT_SERVER_CRYPT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(pubkeyManager);
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            byte[] buffer = ((PubkeyManager)(ois.readObject())).getPublicKey();
            //System.out.println("Client 收到 " + BytesToHex.fromBytesToHex(buffer));
            CryptManager.getInstance().setPublicKeySponsor(buffer);

            //被邀请者构建秘密密钥
            byte[] privateKey = CryptManager.getInstance().getPrivateKeyAccept();
            byte[] publicKey = CryptManager.getInstance().getPublicKeySponsor();
            byte[] secretKey = DHUtil.getSecretKey(publicKey, privateKey);
            CryptManager.getInstance().setSecret(secretKey);
            //System.out.println("Server SecretKey " + BytesToHex.fromBytesToHex(secretKey));

            CryptManager.getInstance().setSecret(secretKey);

            DebugUtil.print("InitCryptAcceptThread " + BytesToHex.fromBytesToHex(CryptManager.getInstance().getSecret()));

            handler.sendEmptyMessage(0);

            // 关闭连接 释放资源
            ois.close();
            oos.close();
            socket.close();
            socket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
