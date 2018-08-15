package com.futuristlabs.p2p.func.cfg;

public interface SystemParametersRepository {
    byte[] getApnCertificate();
    String getApnPassword();
    boolean isApnProduction();
    void setParameter(SystemParam param);
}
