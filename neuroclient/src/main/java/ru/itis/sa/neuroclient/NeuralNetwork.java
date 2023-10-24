package ru.itis.sa.neuroclient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class NeuralNetwork {

    private double w11,w12,w21,w22,v11,v12,v13,v21,v22,v23,w1,w2,w3;
    private double e;

    private static double x1[] = new double[100];
    private static double x2[] = new double[100];
    private static double y[] = new double[100];

    static {
        readTestData100();
    }

    public NeuralNetwork(double w11, double w12, double w21, double w22, double v11, double v12, double v13, double v21, double v22, double v23, double w1, double w2, double w3) {
        this.w11 = w11;
        this.w12 = w12;
        this.w21 = w21;
        this.w22 = w22;
        this.v11 = v11;
        this.v12 = v12;
        this.v13 = v13;
        this.v21 = v21;
        this.v22 = v22;
        this.v23 = v23;
        this.w1 = w1;
        this.w2 = w2;
        this.w3 = w3;
    }

    public NeuralNetwork(DataModel model) throws NumberFormatException {
        init(model);
    }

    public NeuralNetwork() {}

    public void init(DataModel model) throws NumberFormatException {
        this.w11 = Double.parseDouble(model.getW11());
        this.w12 = Double.parseDouble(model.getW12());
        this.w21 = Double.parseDouble(model.getW21());
        this.w22 = Double.parseDouble(model.getW22());
        this.v11 = Double.parseDouble(model.getV11());
        this.v12 = Double.parseDouble(model.getV12());
        this.v13 = Double.parseDouble(model.getV13());
        this.v21 = Double.parseDouble(model.getV21());
        this.v22 = Double.parseDouble(model.getV22());
        this.v23 = Double.parseDouble(model.getV23());
        this.w1 = Double.parseDouble(model.getW1());
        this.w2 = Double.parseDouble(model.getW2());
        this.w3 = Double.parseDouble(model.getW3());
    }

    private double f(double x) {
        return 1/(1 + Math.exp(-x));
    }

    public double g(double x1, double x2) {
        double h11 = f(x1*w11 + x2*w21);
        double h12 = f( x1*w12 + x2*w22);
        return f( f(h11*v11 + h12*v21)*w1 + f(h11*v12 + h12*v22)*w2 + f(h11*v13+h12*v23));
    }

    public double e() {
        double res = 0;
        for (int i = 0; i< 100; i++) {
            double yt = g(x1[i], x2[i]);
            res += (yt - y[i]) * (yt - y[i]);
        }
        return res;
    }

    private static void readTestData100() {
        List<String> lstData;
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream("D:\\IntelliJ IDEA projects\\sysanalysis\\neuroclient\\src\\main\\resources\\xdata.csv")))) {
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] s = line.split(";");
                x1[i] = Double.parseDouble(s[0]);
                x2[i] = Double.parseDouble(s[1]);
                y[i] = Double.parseDouble(s[2]);
                //System.out.println(x1[i] +";" + x2[i] +";" + y[i]);
                i++;
            }
          } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (NumberFormatException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        DataModel dm = new DataModel("1.11", "0.51", "0.53", "0.5", "0", "0.5003", "0.116", "0.007", "0.004", "0.18", "0.017", "0.102", "1.2","1","30819f300d06092a864886f70d010101050003818d0030818902818100a811365d2f3642952751029edf87c8fa2aeb6e0feafcf800190a7dd2cf750c63262f6abd8ef52b251c0e10291d5e2f7e6682de1aae1d64d4f9b242050f898744ca300a44c4d8fc8af0e7a1c7fd9b606d7bde304b29bec01fbef554df6ba1b7b1ec355e1ff68bd37f3d40fb27d1aa233fe3dd6b63f7241e734739851ce8c590f70203010001");
        System.out.println(dm.toString());
        //        NeuralNetwork nn = new NeuralNetwork(1.11, 0.11, 0.23, 1.41, 1.94, 0.0003, 2.016, 0.007,0.904, 1.58, 1.017, 0.102, 1.2);
        NeuralNetwork nn = new NeuralNetwork(dm);
        //nn.readTestData100();
        System.out.println(nn.e());

        String json = "{\"prevhash\":\"0007617fd0eca34b739ddb5fbc96da555636f645389ac84ff0083eb5b21f8c6d\",\"data\":" + dm.toString() + ",\"signature\":\"57071eedfb888c862f16288e9bad699a7fecfc2bbda97c12c8dacb808362778162c29d920a3e6fed7e925f9aa98e5aaf164274b166732ab7b5ae2e7b4bfdd31f3f010447dae3495a7357b83f49c3ae64de551052991281d780a5d6e6ffe91c9470dfab989cba790161b3e16ca18e0f79e96a29cb409402e52a743ccb28499ece\",\"nonce\":\"0\"}";
        HttpURLConnection connection =
                (HttpURLConnection) new URL("http://itislabs.ru/nbc/newblock").openConnection();
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Content-Type","application/json;charset=UTF-8");
        connection.setDoOutput(true);
        connection.getOutputStream().write(json.getBytes("UTF-8"));
        int r = connection.getResponseCode();

        String resp = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();

        System.out.println(resp);
     }

}
