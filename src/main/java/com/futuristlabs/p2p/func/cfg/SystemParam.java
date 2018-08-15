package com.futuristlabs.p2p.func.cfg;

public class SystemParam {

    private String name;
    private String stringValue;
    private byte[] dataValue;

    public SystemParam() {
    }

    public SystemParam(String name, String stringValue, byte[] dataValue) {
        this.name = name;
        this.stringValue = stringValue;
        this.dataValue = dataValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public byte[] getDataValue() {
        return dataValue;
    }

    public void setDataValue(byte[] dataValue) {
        this.dataValue = dataValue;
    }
}
