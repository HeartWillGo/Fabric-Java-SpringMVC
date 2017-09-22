package com.pingan.controller;

import com.pingan.respository.InvokeChainCode;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value="/blockchain/product/*")
public class ProductController {
    @RequestMapping(value = "getProduct", method = RequestMethod.POST)
    public @ResponseBody
    String getProduct(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String id = requestcontent.getString("id");

        String[] args = new String[]{"getProduct", id};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @RequestMapping(value = "writeProduct", method = RequestMethod.POST)
    public @ResponseBody String writeProduct(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String ID = requestcontent.getString("ID");
        String ProductName = requestcontent.getString("ProductName");
        String ProductType = requestcontent.getString("ProductType”");
        String OrganizationID = requestcontent.getString("OrganizationID");
        String Portion = requestcontent.getString("Portion");


        String[] args = new String[]{"writeProduct", ID,ProductName,ProductType,OrganizationID,Portion};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @RequestMapping(value = "CreateProduct", method = RequestMethod.POST)
    public @ResponseBody String CreateProduct(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
//        JSONObject requestcontent = JSONObject.fromObject(body);
//        System.out.println("requestcontent:" + requestcontent);
//        String ID = requestcontent.getString("ID");
//        String ProductName = requestcontent.getString("ProductName");
//        String ProductType = requestcontent.getString("ProductType");
//        String OrganizationID = requestcontent.getString("OrganizationID");
//        String Portion = requestcontent.getString("Portion");
//         ;
//        String Price = requestcontent.getString("Price");
        String[] args = new String[]{"CreateProduct",body};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "getProductTransactionByProductID", method = RequestMethod.POST)
    public @ResponseBody String getProductTransactionByProductID(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String productid = requestcontent.getString("productid");

        ;

        String[] args = new String[]{"getProductTransactionByProductID", productid};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "getProductSaleInformation", method = RequestMethod.POST)
    public @ResponseBody String getProductSaleInformation(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String productid = requestcontent.getString("productid");

        ;

        String[] args = new String[]{"getProductSaleInformation", productid};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "getProductAllUser", method = RequestMethod.POST)
    public @ResponseBody String getProductAllUser(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String productid = requestcontent.getString("productid");

        ;

        String[] args = new String[]{"getProductAllUser", productid};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "getProductAsset", method = RequestMethod.POST)
    public @ResponseBody String getProductAsset(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String productid = requestcontent.getString("productid");

        ;

        String[] args = new String[]{"getProductAsset", productid};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

}
