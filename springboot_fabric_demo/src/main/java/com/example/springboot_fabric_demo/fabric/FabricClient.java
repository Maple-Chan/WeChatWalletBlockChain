package com.example.springboot_fabric_demo.fabric;

import com.example.springboot_fabric_demo.user.UserContext;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class FabricClient {

    private static final Logger log = LoggerFactory.getLogger(FabricClient.class);
    private HFClient hfClient;

    public FabricClient(UserContext userContext) throws IllegalAccessException, InvocationTargetException, InvalidArgumentException, InstantiationException, NoSuchMethodException, CryptoException, ClassNotFoundException {
        hfClient = HFClient.createNewInstance();
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        hfClient.setCryptoSuite(cryptoSuite);
        hfClient.setUserContext(userContext);
    }

    /*
     *  创建Channel
     * */
    public Channel createChannel(String channelName, Orderer orderer, String txPath) throws IOException, InvalidArgumentException, TransactionException {
        ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(txPath));
        Channel channel = hfClient.newChannel(channelName, orderer, channelConfiguration, hfClient.getChannelConfigurationSignature(channelConfiguration, hfClient.getUserContext()));
        return channel;
    }

    /*
     *  安装合约
     * */
    public void installChaincode(TransactionRequest.Type lang, String chaincodeName, String chaincodeVersion, String chaincodeSourceLocation, String chaincodePath, List<Peer> peers) throws InvalidArgumentException, ProposalException {
        InstallProposalRequest installProposalRequest = hfClient.newInstallProposalRequest();
        ChaincodeID.Builder chaincodeBuilder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
        ChaincodeID chaincodeID = chaincodeBuilder.build();
        installProposalRequest.setChaincodeLanguage(lang);
        installProposalRequest.setChaincodeID(chaincodeID);
        installProposalRequest.setChaincodeSourceLocation(new File(chaincodeSourceLocation));
        installProposalRequest.setChaincodePath(chaincodePath);

        Collection<ProposalResponse> responses = hfClient.sendInstallProposal(installProposalRequest, peers);
        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("{} installed succeed", response.getPeer().getName());
            } else {
                log.error("{} installed failed", response.getMessage());
            }
        }

    }

    /**
     * 实例化合约
     */
    public void instantiateChaincode(String channelName, String chaincodeName, String chaincodeVersion, TransactionRequest.Type lang, Orderer orderer, Peer peer, String funcName, String[] args) throws TransactionException, InvalidArgumentException, ProposalException {
        Channel channel = getChannel(channelName);
        channel.addPeer(peer);
        channel.addOrderer(orderer);
        channel.initialize();

        //发送提案

        InstantiateProposalRequest instantiateProposalRequest = hfClient.newInstantiationProposalRequest();
        instantiateProposalRequest.setArgs(args);
        instantiateProposalRequest.setFcn(funcName);
        instantiateProposalRequest.setChaincodeLanguage(lang);

        ChaincodeID.Builder chaincodeBuilder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
        ChaincodeID chaincodeID = chaincodeBuilder.build();
        instantiateProposalRequest.setChaincodeID(chaincodeID);
        Collection<ProposalResponse> responses = channel.sendInstantiationProposal(instantiateProposalRequest);

        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("{} installed succeed", response.getPeer().getName());
            } else {
                log.info("{} installed failed", response.getPeer().getName());
            }
        }

        //发给Orderer的Transaction进行上块
        channel.sendTransaction(responses);

    }

    /**
     * 升级合约
     */
    public void upgradeChaincode(String channelName, String chaincodeName, String chaincodeVersion, TransactionRequest.Type lang, Orderer orderer, Peer peer, String funcName, String[] args) throws TransactionException, InvalidArgumentException, ProposalException, IOException, ChaincodeEndorsementPolicyParseException {
        Channel channel = getChannel(channelName);
        channel.addPeer(peer);
        channel.addOrderer(orderer);
        channel.initialize();

        //发送提案
        UpgradeProposalRequest upgradeProposalRequest = hfClient.newUpgradeProposalRequest();
        upgradeProposalRequest.setArgs(args);
        upgradeProposalRequest.setFcn(funcName);
        upgradeProposalRequest.setChaincodeLanguage(lang);

        //编写背书策略
        ChaincodeEndorsementPolicy chaincodeEndorsementPolicy = new ChaincodeEndorsementPolicy();
        chaincodeEndorsementPolicy.fromYamlFile(new File("D:\\BlockChainWithJava\\maplechancodetest\\src\\main\\resources\\chaincode\\src\\basicCopyInfo\\chaincodeendorsementpolicy.yaml"));

        //修改背书策略
        upgradeProposalRequest.setChaincodeEndorsementPolicy(chaincodeEndorsementPolicy);


        ChaincodeID.Builder chaincodeBuilder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
        ChaincodeID chaincodeID = chaincodeBuilder.build();
        upgradeProposalRequest.setChaincodeID(chaincodeID);
        Collection<ProposalResponse> responses = channel.sendUpgradeProposal(upgradeProposalRequest);

        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("{} upgrade succeed", response.getPeer().getName());
            } else {
                log.info("{} upgrade failed", response.getPeer().getName());
            }
        }

        //发给Orderer的Transaction进行上块
        channel.sendTransaction(responses);

    }


    /**
     * 触发合约
     *
     * @param channelName
     * @param chaincodeName
     * @param chaincodeVersion
     * @param lang
     * @param orderer
     * @param peers
     * @param funcName
     * @param args
     */
    public void invoke(String channelName, String chaincodeName, String chaincodeVersion, TransactionRequest.Type lang, Orderer orderer, List<Peer> peers, String funcName, String[] args) throws TransactionException, InvalidArgumentException, ProposalException {
        Channel channel = getChannel(channelName);
        channel.addOrderer(orderer);

        for (Peer peer : peers) {
            channel.addPeer(peer);
        }

        channel.initialize();
        /*
        上块处理
         */
        TransactionProposalRequest transactionProposalRequest = hfClient.newTransactionProposalRequest();

        transactionProposalRequest.setChaincodeLanguage(lang);

        transactionProposalRequest.setArgs(args);

        transactionProposalRequest.setFcn(funcName);

        ChaincodeID.Builder chaincodeBuilder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion);
        ChaincodeID chaincodeID = chaincodeBuilder.build();
        transactionProposalRequest.setChaincodeID(chaincodeID);

        Collection<ProposalResponse> responses = channel.sendTransactionProposal(transactionProposalRequest, peers);

        /**
         * 返回信息判断，提示报错。
         */
        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("{} invoke {} succeed", response.getPeer().getName(), funcName);
            } else {
                log.error("{} invoke {} failed", response.getMessage(), funcName);
            }
        }

        channel.sendTransaction(responses);

    }


    public Map query(List<Peer> peers, String channelName, TransactionRequest.Type lang, String chaincodeName, String funcName, String queryArgs[]) throws TransactionException, InvalidArgumentException, ProposalException {
        Channel channel = getChannel(channelName);

        for (Peer peer : peers) {
            channel.addPeer(peer);
        }
        channel.initialize();

        QueryByChaincodeRequest queryByChaincodeRequest = hfClient.newQueryProposalRequest();

        ChaincodeID.Builder chaincodeBuilder = ChaincodeID.newBuilder().setName(chaincodeName);
        ChaincodeID chaincodeID = chaincodeBuilder.build();
        queryByChaincodeRequest.setChaincodeID(chaincodeID);

        queryByChaincodeRequest.setFcn(funcName);
        queryByChaincodeRequest.setArgs(queryArgs);
        queryByChaincodeRequest.setChaincodeLanguage(lang);

        Collection<ProposalResponse> responses = channel.queryByChaincode(queryByChaincodeRequest);
        HashMap map = new HashMap();

        /**
         * 返回信息判断，提示报错。
         */
        for (ProposalResponse response : responses) {
            if (response.getStatus().getStatus() == 200) {
                log.info("data is {}", response.getProposalResponse().getPayload());
                map.put(response.getStatus().getStatus(), new String(response.getProposalResponse().getResponse().getPayload().toByteArray()));
                return map;
            } else {
                log.error("data get error {}", response.getMessage());
                map.put(response.getStatus().getStatus(), response.getMessage());
                return map;
            }
        }
        map.put("code", "404");
        return map;

    }


    /*
     *  获取Orderer信息
     * */
    public Orderer getOrder(String tlsFilePath, String ordererName, String grpcURL) throws InvalidArgumentException {
        Properties properties = new Properties();
        properties.setProperty("pemFile", tlsFilePath);

        Orderer orderer = hfClient.newOrderer(ordererName, grpcURL, properties);
        return orderer;
    }

    /*
     *  获取Peer信息
     * */
    public Peer getPeer(String tlsFilePath, String peerName, String grpcURL) throws InvalidArgumentException {
        Properties properties = new Properties();
        properties.setProperty("pemFile", tlsFilePath);
        Peer peer = hfClient.newPeer(peerName, grpcURL, properties);
        return peer;

    }

    /*
     *  获取Channel
     * */
    public Channel getChannel(String channelName) throws InvalidArgumentException, TransactionException {
        Channel channel = hfClient.newChannel(channelName);
        return channel;
    }
}
