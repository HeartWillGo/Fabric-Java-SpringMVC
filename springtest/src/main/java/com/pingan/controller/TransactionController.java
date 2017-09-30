package com.pingan.controller;

import com.pingan.respository.InvokeChainCode;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

@Controller
@RequestMapping(value="/blockchain/transaction/*")
public class TransactionController
{
    @RequestMapping(value="getTransactionByID",method= RequestMethod.POST)
    public @ResponseBody String  transation(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("body:"+body);
    JSONObject requestcontent=JSONObject.fromObject(body);
    System.out.println("requestcontent:"+requestcontent);
    String id=requestcontent.getString ("id");

    String[] args=new String[]{"getTransactionByID",id};


    String result=null;
    try {
        InvokeChainCode invoke = new InvokeChainCode(args);
        result=invoke.invoke();
    }catch (Exception e){
        e.printStackTrace();
    }


    return result;
    }
    @RequestMapping(value="getTransactionByUserID",method= RequestMethod.POST)
    public @ResponseBody String  getTransactionByUserID(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        System.out.println("requestcontent:"+requestcontent);
        String userid=requestcontent.getString ("userid");

        String[] args=new String[]{"getTransactionByUserID",userid};


        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }
    @RequestMapping(value="getTransactionByRange",method= RequestMethod.POST)
    public @ResponseBody String  getTransactionByRange(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        System.out.println("requestcontent:"+requestcontent);
        String userid=requestcontent.getString ("userid");
        String start=requestcontent.getString ("start");
        String end=requestcontent.getString ("end");


        String[] args=new String[]{"getTransactionByUserID",userid};
        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONArray jsonarr=JSONArray.fromObject(result);

        JSONArray jsonresult=new JSONArray();
        for (int i=0;i<jsonarr.size();i++) {

            JSONObject json_trans = jsonarr.getJSONObject(i);

            JSONObject recode=(JSONObject)json_trans.get("Record");
            String time= recode.getString("transactiondate");

            if((Integer.valueOf(time)>=Integer.valueOf(start))&&(Integer.valueOf(time)<Integer.valueOf(end))){
                jsonresult.add(json_trans);
            }
        }

        return jsonresult.toString();
    }

    @RequestMapping(value="getTransactionByTransactionidRange",method= RequestMethod.POST)
    public @ResponseBody String  getTransactionByTransactionidRange(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        System.out.println("requestcontent:"+requestcontent);
        String start=requestcontent.getString ("start");
        String end=requestcontent.getString ("end");

        String[] args=new String[]{"getTransactionByTransactionidRange",start,end};


        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }
    @RequestMapping(value="getTransactionByOrganizationid",method= RequestMethod.POST)
    public @ResponseBody String  getTransactionByOrganizationid(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("body:"+body);
        JSONObject requestcontent=JSONObject.fromObject(body);
        System.out.println("requestcontent:"+requestcontent);
        String organizationid=requestcontent.getString ("organizationid");

        String[] args=new String[]{"getTransactionByOrganizationid",organizationid};


        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.invoke();
        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }

}
