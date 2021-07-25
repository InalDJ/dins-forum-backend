package com.java.springportfolio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeystoreGenerator {

    public static void main(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {

        char[] pwdArray = "password".toCharArray();


        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("portfolio.jks"), pwdArray);

    }
}
