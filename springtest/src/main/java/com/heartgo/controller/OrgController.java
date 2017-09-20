package com.heartgo.controller;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping(value="/blockchain/org/*")
public class OrgController {public  ClientBean foobean;
    public ClientBean barbean;
    public End2end end=SingleEnd2End.getEnd2EndInstance();

    //机构查询当下所有产品
    @RequestMapping(value="query/product",method= RequestMethod.GET)
    public @ResponseBody
    ArrayList<Map<String,String>> getOrgProduct(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String orgid=request.getParameter("orgid");
        String[] args=new String[]{"getOrgProduct",orgid};

        ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();

        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();

        String result=foorun.SendtTansactionToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),foobean.getSampleorgs(),args);
        // barrun.SendQuryToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),args);
        System.out.println("transaction ok");

        JSONArray arr=JSONArray.fromObject(result);

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();


            Map<String,String> map=new HashMap<String,String>();
            String ID=json_foo.getString("ID");
            String ProductName=json_foo.getString("ProductName");
            String ProductType=json_foo.getString("ProductType");
            String ProductAsset=json_foo.getString("ProductAsset");
            String ProductContainsUserNumber=json_foo.getString("ProductContainsUserNumber");
            map.put("ID",ID);
            map.put("ProductName",ProductName);
            map.put("ProductType",ProductType);
            map.put("ProductAsset",ProductAsset);
            map.put("ProductContainsUserNumber",ProductContainsUserNumber);

            list.add(map);
        }

        return list;
    }
    //机构查询某个产品的交易情况
    @RequestMapping(value="query/productTran",method= RequestMethod.GET)
    public @ResponseBody ArrayList<Map<String,String>> geOrgProducTran(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String orgid=request.getParameter("orgid");
        String productId=request.getParameter("productId");
        String[] args=new String[]{"geOrgProducTran",orgid,productId};

        ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();

        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();

        String result=foorun.SendtTansactionToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),foobean.getSampleorgs(),args);
        // barrun.SendQuryToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),args);
        System.out.println("transaction ok");

        JSONArray arr=JSONArray.fromObject(result);

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();

            Map<String,String> map=new HashMap<String,String>();
            String ID=json_foo.getString("ID");
            String ProductID=json_foo.getString("ProductID");
            String Product=json_foo.getString("Product");
            String Trans_type=json_foo.getString("Trans_type");
            String Trans_org=json_foo.getString("Trans_org");
            String ToID=json_foo.getString("ToID");
            String Account=json_foo.getString("Account");
            String TransDate=json_foo.getString("TransDate");

            map.put("ID",ID);
            map.put("ProductID",ProductID);
            map.put("Product",Product);
            map.put("Trans_type",Trans_type);
            map.put("Trans_org",Trans_org);
            map.put("ToID",ToID);
            map.put("Account",Account);
            map.put("TransDate",TransDate);
            list.add(map);
        }

        return list;
    }
    //查询用户购买该机构产品信息
    @RequestMapping(value="query/userinfo",method= RequestMethod.GET)
    public @ResponseBody ArrayList<Map<String,String>> getOrgProductbyUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String userid=request.getParameter("userid");
        String productID=request.getParameter("productID");
        String[] args=new String[]{"getOrgProductbyUser",userid,productID};

        ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();

        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();

        String result=foorun.SendtTansactionToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),foobean.getSampleorgs(),args);
        // barrun.SendQuryToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),args);
        System.out.println("transaction ok");

        JSONArray arr=JSONArray.fromObject(result);

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();

            Map<String,String> map=new HashMap<String,String>();
            String ID=json_foo.getString("ID");
            String ProductId=json_foo.getString("ProductID");
            String Product=json_foo.getString("Product");
            String Trans_type=json_foo.getString("Trans_type");
            String Trans_org=json_foo.getString("Trans_org");
            String ToID=json_foo.getString("ToID");
            String Account=json_foo.getString("Account");
            String TransDate=json_foo.getString("TransDate");
            map.put("ID",ID);
            map.put("ProductId",ProductId);
            map.put("Product",Product);
            map.put("Trans_type",Trans_type);
            map.put("Trans_org",Trans_org);
            map.put("ToID",ToID);
            map.put("Account",Account);
            map.put("TransDate",TransDate);
            list.add(map);
        }

        return list;
    }
    //某一时间内的某个产品购买情况
    @RequestMapping(value="query/productbuyinfo",method= RequestMethod.POST)
    public @ResponseBody ArrayList<Map<String,String>>  ProductBuyInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String productId=request.getParameter("productId");
        String star=request.getParameter("star");
        String end=request.getParameter("end");


        String[] args=new String[]{"ProductBuyInfo",productId,star,end};


        ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();

        String result=foorun.SendtTansactionToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),foobean.getSampleorgs(),args);
        // barrun.SendQuryToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),args);
        System.out.println("transaction ok");

        JSONArray arr=JSONArray.fromObject(result);

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();

            Map<String,String> map=new HashMap<String,String>();
            String ID=json_foo.getString("ID");
            String ProductId=json_foo.getString("ProductID");
            String Product=json_foo.getString("Product");
            String Trans_type=json_foo.getString("Trans_type");
            String Trans_org=json_foo.getString("Trans_org");
            String ToID=json_foo.getString("ToID");
            String Account=json_foo.getString("Account");
            String TransDate=json_foo.getString("TransDate");
            map.put("ID",ID);
            map.put("ProductId",ProductId);
            map.put("Product",Product);
            map.put("Trans_type",Trans_type);
            map.put("Trans_org",Trans_org);
            map.put("ToID",ToID);
            map.put("Account",Account);
            map.put("TransDate",TransDate);
            list.add(map);
        }



        return list;
    }
    //某一时间段的某个用户购买产品情况
    @RequestMapping(value="userbuyinfo",method= RequestMethod.POST)
    public @ResponseBody ArrayList<Map<String,String>> userbuyinfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String userId=request.getParameter("userId");
        String star=request.getParameter("star");
        String end=request.getParameter("end");


        String[] args=new String[]{"ProductBuyInfo",userId,star,end};


        ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();
        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();

        String result=foorun.SendtTansactionToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),foobean.getSampleorgs(),args);
        // barrun.SendQuryToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),args);
        System.out.println("transaction ok");

        JSONArray arr=JSONArray.fromObject(result);

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();

            Map<String,String> map=new HashMap<String,String>();
            String ID=json_foo.getString("ID");
            String ProductId=json_foo.getString("ProductID");
            String Product=json_foo.getString("Product");
            String Trans_type=json_foo.getString("Trans_type");
            String Trans_org=json_foo.getString("Trans_org");
            String ToID=json_foo.getString("ToID");
            String Account=json_foo.getString("Account");
            String TransDate=json_foo.getString("TransDate");
            map.put("ID",ID);
            map.put("ProductId",ProductId);
            map.put("Product",Product);
            map.put("Trans_type",Trans_type);
            map.put("Trans_org",Trans_org);
            map.put("ToID",ToID);
            map.put("Account",Account);
            map.put("TransDate",TransDate);
            list.add(map);
        }



        return list;
    }
    //查询机构总资产
    @RequestMapping(value="asset",method= RequestMethod.POST)
    public @ResponseBody Map<String,String>  getOrgAsset(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String id=request.getParameter("id");


        String[] args=new String[]{"getOrgAsset",id};



        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();

        String result=foorun.SendtTansactionToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),foobean.getSampleorgs(),args);
        // barrun.SendQuryToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),args);
        System.out.println("transaction ok");

        JSONArray arr=JSONArray.fromObject(result);
        Map<String,String> map=new HashMap<String,String>();

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();


            String userid=json_foo.getString("id");
            String asset=json_foo.getString("asset");

            map.put(userid,asset);
        }



        return map;
    }

}
