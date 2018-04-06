package com.heartgo.controller;
import com.heartgo.respository.*;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by dzkan on 2016/3/8.
 */
@Controller
public class MainController {

 //首页，展示的Transaction.jsp
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "Transaction";
    }
    //部署代码
    @RequestMapping(value = "/install", method = RequestMethod.GET)
    public @ResponseBody
    String Inatall() {

        try {
            SetupUsers setupUsers=new SetupUsers();
            setupUsers.setup();
            ConstructChannel constructChannel=new ConstructChannel();
            constructChannel.constructchannel();
            DeployChaincode deploy = new DeployChaincode();
            deploy.install();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
    //这个方法可以不执行，主要就是已经部署代码后获取部署后的peer节点信息
    @RequestMapping(value = "/reinstall", method = RequestMethod.GET)
    public @ResponseBody
    String ReInatall() {

        try {
            getUsers getusers=new getUsers();
            getusers.getusers();
            ReconstructChannel constructChannel=new ReconstructChannel();
            constructChannel.reconstructchannel();
//            DeployChaincode deploy = new DeployChaincode();
//            deploy.install();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }
    //查询
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public @ResponseBody String  query(@RequestBody String body, HttpServletRequest request, HttpServletResponse response) throws IOException {

        JSONObject requestcontent=JSONObject.fromObject(body);
        System.out.println("requestcontent:"+requestcontent);
        String id=requestcontent.getString ("id");

        System.out.println("id:"+id);
        String[] args=new String[]{"query",id };


        String result=null;
        try {
            InvokeChainCode invoke = new InvokeChainCode(args);
            result=invoke.query();
        }catch (Exception e){
            e.printStackTrace();
        }


        return result;
    }



}
