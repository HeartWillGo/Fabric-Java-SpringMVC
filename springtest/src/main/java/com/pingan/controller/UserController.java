package com.pingan.controller;



import com.pingan.respository.DeployChaincode;
import com.pingan.respository.InvokeChainCode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;




/**
 * Created by zhangpeng on 2017/9/15.
 */
@Controller
@RequestMapping(value="/blockchain/user/*")
public class UserController {


    //用户登录
    @RequestMapping(value="login",method= RequestMethod.GET)
    public @ResponseBody String UserLogin(@RequestBody String body,HttpServletRequest request,HttpServletResponse response) throws IOException{

        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        String userid=requestcontent.getString("userid");
        String username=requestcontent.getString("username");
        String token=requestcontent.getString("token");
        String[] args=new String[]{"UserLogin",userid,username,token};



        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("transaction ok");
        System.out.println("result："+result);
        if(result.equals("success")){
            return "/user/index";
        }
        return null;
    }
    //用户查询当下所有产品
    @RequestMapping(value="query/product",method= RequestMethod.POST)
    public @ResponseBody ArrayList<Map<String,String>> UserQueryProduct(@RequestBody String body,HttpServletRequest request, HttpServletResponse response) throws IOException{

        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        String userid=requestcontent.getString("userid");
        String[] args=new String[]{"getUserProduct",userid};

        ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();

        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray arr=JSONArray.fromObject(result);

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();

            Map<String,String> map=new HashMap<String,String>();
            String ID=json_foo.getString("ID");
            String ProductName=json_foo.getString("ProductName");
            String ProductType=json_foo.getString("ProductType");
            String Portion=json_foo.getString("Portion");
            map.put("ID",ID);
            map.put("ProductName",ProductName);
            map.put("ProductType",ProductType);
            map.put("Portion",Portion);
            list.add(map);
        }

        return list;
    }
    //用户查询某机构下的产品
    @RequestMapping(value="query/product/org",method= RequestMethod.POST)
    public @ResponseBody ArrayList<Map<String,String>> getUserProductbyOrg(@RequestBody String body,HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        String userid=requestcontent.getString ("userid");
        String org_id=requestcontent.getString("org_id");
        String[] args=new String[]{"getUserProductbyOrg",userid,org_id};

        ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();



        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray arr=JSONArray.fromObject(result);

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();

            Map<String,String> map=new HashMap<String,String>();
            String ID=json_foo.getString("ID");
            String ProductName=json_foo.getString("ProductName");
            String ProductType=json_foo.getString("ProductType");
            String Portion=json_foo.getString("Portion");
            map.put("ID",ID);
            map.put("ProductName",ProductName);
            map.put("ProductType",ProductType);
            map.put("Portion",Portion);
            list.add(map);
        }

        return list;
    }
    //查询用户交易
    @RequestMapping(value="query/transaction/org",method= RequestMethod.GET)
    public @ResponseBody ArrayList<Map<String,String>> getUserTransactionbyOrg(@RequestBody String body,HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        String userid=requestcontent.getString("userid");
       // String org_id=request.getParameter("org_id");
        String[] args=new String[]{"getTransactionByUserID",userid};

        ArrayList<Map<String,String>> list=new ArrayList<Map<String,String>>();



        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONArray arr=JSONArray.fromObject(result);

        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_foo = (JSONObject) iterator.next();

            Map<String,String> map=new HashMap<String,String>();
            String ID=json_foo.getString("ID");
            String ProductId=json_foo.getString("ProductID");
            String Product=json_foo.getString("Product");
            String Trans_type=json_foo.getString("Trans_type");
            String ToID=json_foo.getString("ToID");
            String Account=json_foo.getString("Account");
            String TransDate=json_foo.getString("TransDate");
            map.put("ID",ID);
            map.put("ProductId",ProductId);
            map.put("Product",Product);
            map.put("Trans_type",Trans_type);
            map.put("ToID",ToID);
            map.put("Account",Account);
            map.put("TransDate",TransDate);
            list.add(map);
        }

        return list;
    }
    //交易
    @RequestMapping(value="transaction",method= RequestMethod.POST)
    public @ResponseBody String  transation(@RequestBody String body,HttpServletRequest request, HttpServletResponse response) throws IOException{



        JSONArray arr=JSONArray.fromObject(body);


        for (Iterator<Object> iterator = arr.iterator(); iterator.hasNext();) {
            JSONObject json_trans = (JSONObject) iterator.next();

            String[] args = new String[]{"Transaction", json_trans.toString()};


            try {
                InvokeChainCode invoke = new InvokeChainCode(args);
                invoke.invoke();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return "success";
    }
    //创建用户
    @RequestMapping(value="CreateUser",method= RequestMethod.POST)
    public @ResponseBody String  CreateUser(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("body:"+body);
//        JSONObject requestcontent=JSONObject.fromObject(body);
//        System.out.println("requestcontent:"+requestcontent);
//        String id=requestcontent.getString ("id");
//        String name=requestcontent.getString("name");
//        String identificationType=requestcontent.getString("identificationType");
//        String identification=requestcontent.getString("identification");
//        String sex=requestcontent.getString("sex");
//        String birthday=requestcontent.getString("birthday");
//        String bankcard=requestcontent.getString("bankcard");
//        String phonoumber=requestcontent.getString("phonoumber");
//        String token=requestcontent.getString("token");
//        System.out.println("token:"+token);
//        System.out.println("name:"+name);
//        System.out.println("id:"+id);
        String[] args=new String[]{"CreateUser",body};




        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }





        return "success";
    }
    //修改个人信息
    //创建用户
    @RequestMapping(value="updateinfo",method= RequestMethod.POST)
    public @ResponseBody String  WriteUser(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        System.out.println("requestcontent:"+requestcontent);
        String id=requestcontent.getString ("id");
        String name=requestcontent.getString("name");
        String identificationType=requestcontent.getString("identificationType");
        String identification=requestcontent.getString("identification");
        String sex=requestcontent.getString("sex");
        String birthday=requestcontent.getString("birthday");
        String bankcard=requestcontent.getString("bankcard");
        String phonoumber=requestcontent.getString("phonoumber");
//        String token=requestcontent.getString("token");
//        System.out.println("token:"+token);
        System.out.println("name:"+name);
        System.out.println("id:"+id);
        String[] args=new String[]{"WriteUser",id,name,identificationType,identification,sex,birthday,bankcard,phonoumber};




        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }



        return "success";
    }



    @RequestMapping(value = "query", method = RequestMethod.POST)
    public @ResponseBody String  query(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException{

        JSONObject requestcontent=JSONObject.fromObject(body);
        System.out.println("requestcontent:"+requestcontent);
        String id=requestcontent.getString ("id");

        System.out.println("id:"+id);
        String[] args=new String[]{"query",id };


        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }

    //查询个人总资产
    @RequestMapping(value="getUserAsset ",method= RequestMethod.POST)
    public @ResponseBody Map<String,String>  getUserAsset(@RequestBody String body,HttpServletRequest request, HttpServletResponse response) throws IOException{
        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        String id=requestcontent.getString("id");


        String[] args=new String[]{"getUserAsset",id};





        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("result :"+result);
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
    @RequestMapping(value = "getUser", method = RequestMethod.POST)
    public @ResponseBody String getUser(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String id = requestcontent.getString("id");

        String[] args = new String[]{"getUser", id};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "getUserAllProduct", method = RequestMethod.POST)
    public @ResponseBody String getUserAllProduct(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String id = requestcontent.getString("id");

        String[] args = new String[]{"getUserAllProduct", id};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "getUserOrgProductid", method = RequestMethod.POST)
    public @ResponseBody String getUserOrgProductid(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String id = requestcontent.getString("id");

        String[] args = new String[]{"getUserOrgProductid", id};


        String result = null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result = invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    @RequestMapping(value = "getUserFromOrganizationAsset", method = RequestMethod.POST)
    public @ResponseBody String getUserFromOrganizationAsset(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:" + body);
        JSONObject requestcontent = JSONObject.fromObject(body);
        System.out.println("requestcontent:" + requestcontent);
        String organizationid = requestcontent.getString("organizationid");
        String productid = requestcontent.getString("productid");
        String userid = requestcontent.getString("userid");

        String[] args = new String[]{"getUserOrgProductid", organizationid,productid,userid};


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
