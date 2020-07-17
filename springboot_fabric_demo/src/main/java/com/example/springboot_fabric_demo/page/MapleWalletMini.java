package com.example.springboot_fabric_demo.page;

import com.example.springboot_fabric_demo.fabric.FabricDemo;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class MapleWalletMini {
    private FabricDemo fabricDemo;
    private static final Logger logger = LoggerFactory.getLogger(MapleWalletMini.class);
    public MapleWalletMini() {
        fabricDemo = new FabricDemo();
    }

    @RequestMapping("getInformation")
    public List<Object> getInformation() {
        System.out.println("微信小程序正在调用...");
        List<Object> items = new ArrayList<>();
        Map<String, Object> list1 = new HashMap();
        list1.put("time", "2020/2/23");
        list1.put("money", "20.3");
        list1.put("type", "incomes");
        list1.put("description", "gift");

        Map<String, Object> list2 = new HashMap();
        list2.put("time", "2020/2/23");
        list2.put("money", "224.3");
        list2.put("type", "incomes");
        list2.put("description", "korea");

        items.add(list1);
        items.add(list2);


        System.out.println("微信小程序调用完成...");
        logger.info("Maple print info ");

        return items;
    }

    @RequestMapping("query")
    public Object getLatest(String ID) {
        ID = "1101";
        Object retQuery = null;
        try {
            Map queryresult = fabricDemo.query("mapleWalletTest", ID);
            retQuery = queryresult.get(200);

        } catch (IllegalAccessException | InvalidArgumentException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException | ProposalException | TransactionException | InvalidKeySpecException | NoSuchAlgorithmException | org.bouncycastle.crypto.CryptoException | IOException e) {
            e.printStackTrace();
        }
        return retQuery;
    }

    @RequestMapping("queryAllByID")
    public List<Object> getALL(String ID) {
        ID = "1101";
        List<Object> getList = null;
        try {
            Map queryresult = fabricDemo.queryTransactionByID("mapleWalletTest", ID);
            String result = (String) queryresult.get(200);
            getList = getJsonInformation(result, "walletInfoList");

        } catch (IllegalAccessException | InvalidArgumentException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException | ProposalException | TransactionException | InvalidKeySpecException | NoSuchAlgorithmException | org.bouncycastle.crypto.CryptoException | IOException e) {
            e.printStackTrace();
        }
        return getList;
    }

    @RequestMapping(value = "submit", method = RequestMethod.GET)
    public boolean submitTransaction(@RequestParam("typeTransaction") String typeTransaction,
                                     @RequestParam("identity") String identity,
                                     @RequestParam("money") String moneyOfTransaction,
                                     @RequestParam("time") String time,
                                     @RequestParam("description") String desOfTransaction
    ) {
        System.out.println(typeTransaction);

        try {
            fabricDemo.invoke(
                    identity,
                    "0",
                    time,
                    moneyOfTransaction,
                    typeTransaction,
                    desOfTransaction
            );
            return true;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | org.bouncycastle.crypto.CryptoException | IOException | IllegalAccessException | InvalidArgumentException | InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | CryptoException | ProposalException | TransactionException e) {
            e.printStackTrace();
        }

        return false;
    }


    private List<Object> getJsonInformation(String jsonData, String HashName) {
        List<Object> info = null;
        try {
            JSONObject jsonObj = (JSONObject) (new JSONParser().parse(jsonData));
            info = (List<Object>) jsonObj.get(HashName);
            System.out.println("Info: get walletInfoList in json mode: " + jsonObj.toJSONString() + "\n" + jsonObj.getClass());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return info;
    }


    @RequestMapping("about")
    public String getTest() {
        return "Hello world! Project signed by maple！";
    }

}
