//package io.chan.springsecurityresources.init;
//
//import io.chan.springsecurityresources.signature.RsaPublicKeySecuritySigner;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.OutputStreamWriter;
//import java.nio.charset.Charset;
//import java.security.*;
//import java.security.cert.Certificate;
//
//@Component
//@RequiredArgsConstructor
//public class RsaKeyExtractor implements ApplicationRunner {
//
//    private final RsaPublicKeySecuritySigner rsaPublicKeySecuritySigner;
//
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        String path = "E:\\project\\spring-security-oauth2\\src\\main\\resources\\certs\\";
//        File file = new File(path + "publicKey.txt");
//
//        FileInputStream is = new FileInputStream(path + "apiKey.jks");
//        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//        keystore.load(is, "pass1234".toCharArray());
//        String alias = "apiKey";
//        Key key = keystore.getKey(alias, "pass1234".toCharArray());
//
//        if (key instanceof PrivateKey) {
//
//            Certificate certificate = keystore.getCertificate(alias);
//            PublicKey publicKey = certificate.getPublicKey();
//            KeyPair keyPair = new KeyPair(publicKey, (PrivateKey) key);
////            rsaPublicKeySecuritySigner.setPrivateKey(keyPair.getPrivate());
//
//            if (!file.exists()) {
//                String publicStr = java.util.Base64.getMimeEncoder().encodeToString(publicKey.getEncoded());
//                publicStr = "-----BEGIN PUBLIC KEY-----\r\n" + publicStr + "\r\n-----END PUBLIC KEY-----";
//
//                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), Charset.defaultCharset());
//                writer.write(publicStr);
//                writer.close();
//            }
//        }
//        is.close();
//    }
//}
