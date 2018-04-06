package com.heartgo.model;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author zhang peng
 */
//这是用于批量向区块链上传数据的程序
public class HttpPostUrl {

    public static String URL = "http://localhost:8080/blockchain/user/transaction";

    public static void main(String[] args) {


        JSONArray jarr=new JSONArray();
        for(int i=100;i<130;i++) {     //for循环自己定义，但不可和之前的重复，因为交易id和i相关

            JSONObject json= new JSONObject();
            json.put("SID","SID"+i);
            json.put("ReceiverSID","201709290"+i);
            json.put("OriginSID","201710230");
            json.put("RequestSerial","201710340");
            json.put("NextRequestSerial","201710340");
            json.put("Proposaltime",1206332229);
            json.put("transactionid","transactionid00"+i);
            json.put("transactiondate",1483203600+i*3000);
            json.put("parentorder","0");
            json.put("suborder","0");
            json.put("payid","0");
            json.put("transtype","0");
            json.put("fromtype",1);
            json.put("fromid","yaozhen");
            json.put("totype",1);
            json.put("toid","pDVfRJVc");
            json.put("productid","productid0");
            json.put("productinfo","wegoodi%3");
            if(i%3==0) {
                json.put("organizationid", "jinke");
            }else if(i%3==1){
                json.put("organizationid", "dahua");
            }else{
                json.put("organizationid", "pingan");
            }
            json.put("amount",4);
            json.put("price",9);
            json.put("deviceID", "112");
            json.put("channel", "channel");
            json.put("state", "0");


            jarr.add(json);
        }
        appadd(jarr);
    }


    public static void appadd(JSONArray obj) {

        try{
            //创建连接
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);

            //connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());


            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(sf.format(new Date()));

            //System.out.println(obj.toString());

            //out.writeBytes(obj.toString());//这个中文会乱码
            out.write(obj.toString().getBytes("UTF-8"));//这样可以处理中文乱码问题
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            System.out.println(sb);
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
