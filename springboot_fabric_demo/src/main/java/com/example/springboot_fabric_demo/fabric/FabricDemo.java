package com.example.springboot_fabric_demo.fabric;

import com.example.springboot_fabric_demo.user.UserContext;
import com.example.springboot_fabric_demo.user.UserUtils;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.TransactionRequest;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FabricDemo {

    private static final String keyFolderPathOrg1 = FilePath.keyFolderPathOrg1;
    private static final String keyFileNameOrg1 = FilePath.keyFileNameOrg1;
    private static final String certFolderPathOrg1 = FilePath.certFolderPathOrg1;
    private static final String certFileNameOrg1 = FilePath.certFileNameOrg1;
    //Org2
    private static final String keyFolderPathOrg2 = FilePath.keyFolderPathOrg2;
    private static final String keyFileNameOrg2 = FilePath.keyFileNameOrg2;
    private static final String certFolderPathOrg2 = FilePath.certFolderPathOrg2;
    private static final String certFileNameOrg2 = FilePath.certFileNameOrg2;

    private static final String tlsOrderFilePath = FilePath.tlsOrderFilePath;
    private static final String tlsPeerFile = FilePath.tlsPeerFile;
    private static final String tlsPeerFileOrg2 = FilePath.tlsPeerFileOrg2;

    public void invoke(
            String identity,
            String currentMoney,
            String time,
            String moneyOfTransaction,
            String typeOfTransaction,
            String desOfTransaction
    ) throws InvalidKeySpecException, NoSuchAlgorithmException, org.bouncycastle.crypto.CryptoException, IOException, IllegalAccessException, InvalidArgumentException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException, ProposalException, TransactionException { //把参数抽取好，方便调用。
        /**
         * invoke 触发合约
         */
        /*
         * 注意事项：
         *  背书策略改成两个节点都要进行背书的时候，需要对两个org都安装-相同的证书，在安装时证书必须对应匹配
         *  在进行触发的时候，只需要证书和节点对应就行，不一定执行哪个org的内容。
         */

        /*可以对输入进行检查*/


        /*
        Init
        */
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("test");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFolderPathOrg1, certFileNameOrg1);
        userContext.setEnrollment(enrollment);


//        在进行升级之前需要先对2.0版本的合约进行安装，安装完之后，再执行升级的内容。

        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer(tlsPeerFile, "peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051");
        Peer peer1 = fabricClient.getPeer(tlsPeerFile, "peer1.org1.example.com", "grpcs://peer1.org1.example.com:8051");
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);


        Orderer orderer = fabricClient.getOrder(tlsOrderFilePath, "orderer.example.com", "grpcs://orderer.example.com:7050");

        String instantiateArgs[] = {identity,
                "{\"identity\":\"" + identity + "\"," +
                        "\"currentMoney\":\"" + currentMoney + "\"," +
                        "\"time\":\"" + time + "\"," +
                        "\"moneyOfTransaction\":\"" + moneyOfTransaction + "\"," +
                        "\"typeOfTransaction\":\"" + typeOfTransaction + "\"," +
                        "\"desOfTransaction\":\"" + desOfTransaction + "\"}"};


        fabricClient.invoke("mychannel",
                "mapleWalletTest",
                "1.0",
                TransactionRequest.Type.GO_LANG,
                orderer,
                peers,
                "save",
                instantiateArgs);

        System.out.println("Invoke succeed");
    }


    public Map query(String chaincodeName, String ID) throws IllegalAccessException, InvalidArgumentException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException, ProposalException, TransactionException, InvalidKeySpecException, NoSuchAlgorithmException, org.bouncycastle.crypto.CryptoException, IOException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("test");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFolderPathOrg1, certFileNameOrg1);
        userContext.setEnrollment(enrollment);

        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer(tlsPeerFile, "peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051");
        Peer peer1 = fabricClient.getPeer(tlsPeerFile, "peer1.org1.example.com", "grpcs://peer1.org1.example.com:8051");
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);

        String queryArgs[] = {ID};

        Map map = fabricClient.query(peers, "mychannel",
                TransactionRequest.Type.GO_LANG,
                chaincodeName,
                "query",
                queryArgs);

        System.out.println("Info: get query(): " + map);
        return map;
    }

    public Map queryTransactionByID(String chaincodeName, String ID) throws IllegalAccessException, InvalidArgumentException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, CryptoException, ProposalException, TransactionException, InvalidKeySpecException, NoSuchAlgorithmException, org.bouncycastle.crypto.CryptoException, IOException {
        UserContext userContext = new UserContext();
        userContext.setAffiliation("Org1");
        userContext.setMspId("Org1MSP");
        userContext.setAccount("test");
        userContext.setName("admin");
        Enrollment enrollment = UserUtils.getEnrollment(keyFolderPathOrg1, keyFileNameOrg1, certFolderPathOrg1, certFileNameOrg1);
        userContext.setEnrollment(enrollment);

        FabricClient fabricClient = new FabricClient(userContext);
        Peer peer0 = fabricClient.getPeer(tlsPeerFile, "peer0.org1.example.com", "grpcs://peer0.org1.example.com:7051");
        Peer peer1 = fabricClient.getPeer(tlsPeerFile, "peer1.org1.example.com", "grpcs://peer1.org1.example.com:8051");
        List<Peer> peers = new ArrayList<>();
        peers.add(peer0);
        peers.add(peer1);

        String queryArgs[] = {ID};

        Map map = fabricClient.query(peers, "mychannel",
                TransactionRequest.Type.GO_LANG,
                chaincodeName,
                "queryTransactionByDay",
                queryArgs);

        System.out.println("Info: get queryTransactionByDay(): " + map);
        return map;
    }
}
