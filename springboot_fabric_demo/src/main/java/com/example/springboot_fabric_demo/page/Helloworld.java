package com.example.springboot_fabric_demo.page;

import com.example.springboot_fabric_demo.fabric.FabricDemo;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.TransactionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Controller
public class Helloworld {

    private FabricDemo fabricDemo;

    Helloworld() {
        fabricDemo = new FabricDemo();
    }

    @RequestMapping(value = "/hello", produces = "text/plain;charset=UTF-8", method = RequestMethod.GET)
    public String test() {
        return "hello";
    }

    @GetMapping(value = "/extfile")
    @ResponseBody
    public String extFile() {
        final String fileName = System.getProperty("user.dir") + "/files/config.txt";

        File file = new File(fileName);
        StringBuilder stringBuilder = new StringBuilder();

        if (file.canRead()) {
            try {
                FileReader fileReader = new FileReader(file);
                char[] buf = new char[1024];
                while(fileReader.read(buf) != -1){
                    stringBuilder.append(buf);
                    System.out.println("Reading file"+ stringBuilder);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return stringBuilder.toString();
    }


    @PostMapping(value = "/hello")
    public void postMessage(String description, String action) {
        System.out.println("Receive post Message");
        System.out.println("description: " + description);

        if (action.equals("submit_incomes") || action.equals("submit_outcomes")) {
            String type_transaction = "incomes";
            if (action.equals("submit_incomes")) {
                System.out.println("incomes");
                type_transaction = "incomes";

            } else if (action.equals("submit_outcomes")) {
                System.out.println("outcomes");
                type_transaction = "outcomes";
            }
            //执行invoke
            try {
                fabricDemo.invoke("1101",
                        "0",
                        "2020_2_21",
                        "211.314",
                        type_transaction,
                        "what ever");
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (org.bouncycastle.crypto.CryptoException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (CryptoException e) {
                e.printStackTrace();
            } catch (ProposalException e) {
                e.printStackTrace();
            } catch (TransactionException e) {
                e.printStackTrace();
            }

        } else if (action.equals("query")) {
            System.out.println("query block chain");
            try {
                Map info = fabricDemo.query("mapleWalletTest", "1101");

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (CryptoException e) {
                e.printStackTrace();
            } catch (ProposalException e) {
                e.printStackTrace();
            } catch (TransactionException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (org.bouncycastle.crypto.CryptoException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (action.equals("queryTransactionByID")) {
            System.out.println("query All in block chain");
            try {
                Map info = fabricDemo.queryTransactionByID("mapleWalletTest1", "1101");

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvalidArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (CryptoException e) {
                e.printStackTrace();
            } catch (ProposalException e) {
                e.printStackTrace();
            } catch (TransactionException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (org.bouncycastle.crypto.CryptoException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}