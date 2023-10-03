package ru.itis.sysanalysis.bcone;

import java.util.ArrayList;
import java.util.List;

public class BlockInfo {

    private int blockNum;

    private List<String> data = new ArrayList<>();

    private byte[] prevHash;

    private byte[] sign;

    private byte[] signBlock;

    public BlockInfo(int blockNum) {
        this.blockNum = blockNum;
    }

    public int getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(int blockNum) {
        this.blockNum = blockNum;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public byte[] getPrevHash() {
        return prevHash;
    }

    public void setPrevHash(byte[] prevHash) {
        this.prevHash = prevHash;
    }

    public byte[] getSign() {
        return sign;
    }

    public void setSign(byte[] sign) {
        this.sign = sign;
    }

    public void setDataSign(byte[] sign) {
        this.sign = sign;
    }
}
