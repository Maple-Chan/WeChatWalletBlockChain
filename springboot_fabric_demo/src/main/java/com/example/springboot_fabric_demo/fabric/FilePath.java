package com.example.springboot_fabric_demo.fabric;

public interface FilePath {
    //test
/*
    String keyFolderPathOrg1 = "D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\crypto-config\\peerOrganizations\\org1.example.com\\users\\Admin@org1.example.com\\msp\\keystore";
    static final String keyFileNameOrg1 = "priv_sk";
    static final String certFolderPathOrg1 = "D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\crypto-config\\peerOrganizations\\org1.example.com\\users\\Admin@org1.example.com\\msp\\signcerts";
    static final String certFileNameOrg1 = "Admin@org1.example.com-cert.pem";
    //Org2
    static final String keyFolderPathOrg2 = "D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\crypto-config\\peerOrganizations\\org2.example.com\\users\\Admin@org2.example.com\\msp\\keystore";
    static final String keyFileNameOrg2 = "priv_sk";
    static final String certFolderPathOrg2 = "D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\crypto-config\\peerOrganizations\\org2.example.com\\users\\Admin@org2.example.com\\msp\\signcerts";
    static final String certFileNameOrg2 = "Admin@org2.example.com-cert.pem";

    static final String tlsOrderFilePath = "D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\crypto-config\\ordererOrganizations\\example.com\\tlsca\\tlsca.example.com-cert.pem";
    //    private static final String txFilePath="D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\channel1521.tx";
    static final String tlsPeerFile = "D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\crypto-config\\peerOrganizations\\org1.example.com\\tlsca\\tlsca.org1.example.com-cert.pem";
    static final String tlsPeerFileOrg2 = "D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\crypto-config\\peerOrganizations\\org2.example.com\\tlsca\\tlsca.org2.example.com-cert.pem";
*/

    //部署到服务器的目录
    public static final String keyFolderPathOrg1="/opt/gopath/src/github.com/hyperledger/fabric-samples/first-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore";
    public static final String keyFileNameOrg1="priv_sk";
    public static final String certFolderPathOrg1="/opt/gopath/src/github.com/hyperledger/fabric-samples/first-network/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts";
    public static final String certFileNameOrg1="Admin@org1.example.com-cert.pem";
    //Org2
    public static final String keyFolderPathOrg2="/opt/gopath/src/github.com/hyperledger/fabric-samples/first-network/crypto-config/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp/keystore";
    public static final String keyFileNameOrg2="priv_sk";
    public static final String certFolderPathOrg2="/opt/gopath/src/github.com/hyperledger/fabric-samples/first-network/crypto-config/peerOrganizations/org2.example.com/users/Admin@org2.example.com/msp/signcerts";
    public static final String certFileNameOrg2="Admin@org2.example.com-cert.pem";


    public static final String tlsOrderFilePath="/opt/gopath/src/github.com/hyperledger/fabric-samples/first-network/crypto-config/ordererOrganizations/example.com/tlsca/tlsca.example.com-cert.pem";
    public static final String tlsPeerFile="/opt/gopath/src/github.com/hyperledger/fabric-samples/first-network/crypto-config/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/msp/tlscacerts/tlsca.org1.example.com-cert.pem";
    public static final String tlsPeerFileOrg2="/opt/gopath/src/github.com/hyperledger/fabric-samples/first-network/crypto-config/peerOrganizations/org2.example.com/tlsca/tlsca.org2.example.com-cert.pem";

}/* /opt/gopath/src/github.com/hyperledger/fabric-samples/first-network/crypto-config*/
