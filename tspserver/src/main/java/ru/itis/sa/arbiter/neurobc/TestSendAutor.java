package ru.itis.sa.arbiter.neurobc;

import org.bouncycastle.util.encoders.Hex;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Formatter;

public class TestSendAutor {
    public static void main(String[] args) {
        String KEY_ALGORITHM = "RSA";
        String SIGN_ALGORITHM = "SHA256withRSA";
        String publicKey16 = "30819f300d06092a864886f70d010101050003818d0030818902818100a811365d2f3642952751029edf87c8fa2aeb6e0feafcf800190a7dd2cf750c63262f6abd8ef52b251c0e10291d5e2f7e6682de1aae1d64d4f9b242050f898744ca300a44c4d8fc8af0e7a1c7fd9b606d7bde304b29bec01fbef554df6ba1b7b1ec355e1ff68bd37f3d40fb27d1aa233fe3dd6b63f7241e734739851ce8c590f70203010001";
        String privateKey16 = "30820277020100300d06092a864886f70d0101010500048202613082025d02010002818100a811365d2f3642952751029edf87c8fa2aeb6e0feafcf800190a7dd2cf750c63262f6abd8ef52b251c0e10291d5e2f7e6682de1aae1d64d4f9b242050f898744ca300a44c4d8fc8af0e7a1c7fd9b606d7bde304b29bec01fbef554df6ba1b7b1ec355e1ff68bd37f3d40fb27d1aa233fe3dd6b63f7241e734739851ce8c590f7020301000102818019a116533b81e0c1720f6e80dfc18b2a6b251fd1999a1e0f95c1fdd20a04e570b14005dd0651e8d46de4cf1e15f668f3b39a3d588038c7499b124df30b9d835d007173897de36ed60b741be71d9c0c92539bc5d8d8c81c217e35768afa4f5deb208adc074646747b64bddf401d24fc27d989073b811145e3f26453e50dff9391024100e8ba466033c207db1c886a385e0933bb592bd279f436dcdd809516e6c9ff2a3bce740668c9ec306f536694e79f47aac1854e59eed67ae9b04a4387ceff2fd8cb024100b8dfa8debdbd3b732a3b108b0669985f8c064bdedf219b98ddbaf3d43c57cd13caea5d247e3099bdf6d553e9182d76fc8f002a7ee7ac27277ee0010f5cc95f0502405c90f2bb02fb330ab8cf9d882c06f6f796a1328765568904e546e7a4064622b3450bab1cf4eba7dc2fcd067f1640782f9ee92a263053152e146bc5d1d478607f024100a5cd310e52ec9b9cb58b3352fbf5480aa8f21ca94d443edfe23c00e19f1cbf77b8cc67ac8adfdb825b1507a44d62b90bd8d435488b4e01ef920223f13b2dfe61024100cf1d242fb3c7bf46f6c222756122db17326894108af890d645cc787f38798b4c0e8500625bf66b548855768583ef53c21be5f8d46d9076552fe32f75c3461e53";

        String autor = "Гарипов Ризван Русланович, 11-004";

        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Hex.decode(privateKey16));
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(keySpec);

            Signature signature = Signature.getInstance(SIGN_ALGORITHM);

            signature.initSign(priKey);

            signature.update(autor.getBytes("UTF-8"));

            String sign = new String(Hex.encode(signature.sign()));

            String autorInfo = new Formatter()
                    .format("{\"autor\":\"%s\",\"sign\":\"%s\",\"publickey\":\"%s\"}", autor, sign, publicKey16).toString();

            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://itislabs.ru/nbc/autor").openConnection();
            connection.setRequestMethod("POST");
            connection.addRequestProperty("Content-Type","application/json;charset=UTF-8");
            connection.setDoOutput(true);
            connection.getOutputStream().write(autorInfo.getBytes("UTF-8"));
            int r = connection.getResponseCode();

            String resp = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
