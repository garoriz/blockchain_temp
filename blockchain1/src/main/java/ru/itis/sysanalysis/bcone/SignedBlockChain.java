package ru.itis.sysanalysis.bcone;

import org.bouncycastle.util.encoders.Hex;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SignedBlockChain {

    private static final int BC_LENGTH = 10;
    private static List<BlockInfo> blockchain = new ArrayList<>();
    private static KeyPair keyPair;

    public static void main(String[] args) {

        try {
            keyPair = Utils.loadKeys();
            makeBlockChain();
            saveBlockChain();

            System.out.println(getString());

            System.out.println("verification result: " + verification());

            damage();

            System.out.println(getString());

            System.out.println("verification result: " + verification());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveBlockChain() {
        try {
            File myObj = new File("blockchain.txt");
            if (myObj.createNewFile()) {
                String str = getString();
                BufferedWriter writer = new BufferedWriter(new FileWriter(myObj));
                writer.write(str);

                writer.close();

                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    private static void makeBlockChain() {
        byte[] prevHash = null;

        for (int i = 0; i < BC_LENGTH; i++) {
            BlockInfo blockInfo = new BlockInfo(i);
            blockInfo.getData().add("{\"data\":\"data " + i + "\"}");
            blockInfo.getData().add("{\"timestamp\":\"" + new Date() + "\"}");
            blockInfo.setPrevHash(prevHash);

            try {
                prevHash = Utils.getHash(blockInfo);

                byte[] bytes = String.join("", blockInfo.getData()).getBytes();
                byte[] signBlock = Utils.generateRSAPSSSignature(keyPair.getPrivate(), bytes);
                blockInfo.setSignBlock(signBlock);

                blockInfo.setSign(Utils.generateRSAPSSSignature(keyPair.getPrivate(), prevHash));
            } catch (Exception e) {
                e.printStackTrace();
            }


            blockchain.add(blockInfo);
        }
    }

    private static String getString() throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < BC_LENGTH; i++) {
            BlockInfo bi = blockchain.get(i);
            string.append("===================== ").append(bi.getBlockNum()).append(" =============================\n");
            string.append("prev hash: ").append(bi.getPrevHash() != null ? new String(Hex.encode(bi.getPrevHash())) : "\n");
            for (String s : bi.getData()) string.append(s).append("\n");
            string.append("digest: ").append(new String(Hex.encode(Utils.getHash(bi)))).append("\n");
            string.append("signature: ").append(new String(Hex.encode(bi.getSign()))).append("\n");
            string.append("sign block: : ").append(new String(Hex.encode(bi.getSignBlock()))).append("\n");
        }
        return string.toString();
    }

    private static boolean verification() throws GeneralSecurityException, UnsupportedEncodingException {

        byte[] prevHash = Utils.getHash(blockchain.get(0));
        for (int i = 1; i < BC_LENGTH; i++) {
            if (!Arrays.equals(prevHash, blockchain.get(i).getPrevHash())) {
                return false;
            }

            var data = String.join("", blockchain.get(i).getData()).getBytes();
            var dataSign = Utils.generateRSAPSSSignature(keyPair.getPrivate(), data);
            if (!Arrays.equals(dataSign, blockchain.get(i).getSignBlock())) {
                System.out.println("incorrect hash");
                return false;
            }

            prevHash = Utils.getHash(blockchain.get(i));

            if (!Utils.verifyRSAPSSSignature(keyPair.getPublic(), prevHash, blockchain.get(i).getSign())) {
                return false;
            }
        }

        return true;
    }

    private static void damage() {
        blockchain.get(3).getData().remove(0);
        blockchain.get(3).getData().add(0, "{\"data\":\"damaged data\"}");
    }
}
