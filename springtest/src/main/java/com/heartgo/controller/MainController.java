package com.heartgo.controller;
import com.google.gson.JsonObject;
import com.heartgo.model.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import net.sf.json.*;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dzkan on 2016/3/8.
 */
@Controller
public class MainController {

    // 自动装配数据库接口，不需要再写原始的Connection来操作数据库
//    @Autowired
//    UserRepository userRepository;
    public  ClientBean foobean;
    public ClientBean barbean;
    public End2end end=new End2end();
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "admin/users";
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String getUsers(ModelMap modelMap) {
        // 查询user表中所有记录
        ArrayList<User> userList =new ArrayList<User>();
        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();
        String[] str=new String[] {"query", "1"};
        System.out.println("str:"+str.toString());
        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        System.out.println("foorun success size:"+foorun.successful.size());
        System.out.println("barrun success size:"+barrun.successful.size());

        String result_foo=foorun.SendQuryToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),str);

        String[] result_arr=result_foo.split(",");
        for(int i=0;i<result_arr.length;i++){
            String[] query_str=new String[]{"query",result_arr[i]};
            String result=foorun.SendQuryToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),query_str);
            JSONObject json_foo=JSONObject.fromObject(result_foo);
            User user=new User();
            user.setName(json_foo.getString("name"));





        }

        String reult_bar=barrun.SendQuryToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),str);
        JSONObject json_foo=JSONObject.fromObject(result_foo);


        System.out.println("name:"+json_foo.get("name"));

        //将所有记录传递给要返回的jsp页面，放在userList当中
        modelMap.addAttribute("userList", userList);

        // 返回pages目录下的admin/users.jsp页面
        return "admin/users";
    }

    // get请求，访问添加用户 页面
    @RequestMapping(value = "/admin/users/add", method = RequestMethod.GET)
    public String addUser() {
        // 返回 admin/addUser.jsp页面
        return "admin/addUser";
    }

    // post请求，处理添加用户请求，并重定向到用户管理页面
    @RequestMapping(value = "/admin/users/addP", method = RequestMethod.POST)
    public String addUserPost(@ModelAttribute("user") UserEntity userEntity ) {
        // 注意此处，post请求传递过来的是一个UserEntity对象，里面包含了该用户的信息
        // 通过@ModelAttribute()注解可以获取传递过来的'user'，并创建这个对象

        // 数据库中添加一个用户，该步暂时不会刷新缓存
        //userRepository.save(userEntity);
        String ID=userEntity.getID() ;             //用户ID
        String Name=userEntity.getName() ;          //用户名字
        int IdentificationType=userEntity.getIdentificationType(); // 证件类型
        String Identification=userEntity.getIdentification() ;  //证件号码
        int Sex=userEntity.getSex() ;               //性别
        String Birthday=userEntity.getBirthday() ;        //生日
        String BankCard=userEntity.getBankCard() ;        //银行卡号
        String PhoneNumber =userEntity.getPhoneNumber();     //手机号
        String[] str_user=new String[]{"CreateUser",ID,Name,new String().valueOf(IdentificationType), Identification,new String().valueOf(Sex),Birthday,BankCard,PhoneNumber};
        for(String s:str_user){
            System.out.println(s);
        }


        File directory = new File("");//设定为当前文件夹 
        System.out.println( );//获取标准的路径 
        System.out.println("oo"+directory.getAbsolutePath());//获取绝对路径 
        //System.out.println("str_user:"+str_user.toString());
        try {
            System.out.println("file this"+this.getClass().getResource("").toURI().getPath());
            System.out.println("file this /"+this.getClass().getResource("/").toURI().getPath());
        }catch (Exception e){
            e.printStackTrace();
        }
        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();

        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        System.out.println("foorun success size:"+foorun.successful.size());
        System.out.println("barrun success size:"+barrun.successful.size());
        foorun.SendtTansactionToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),foobean.getSampleorgs(),str_user);
        barrun.SendtTansactionToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),barbean.getSampleorgs(),str_user);
        System.out.println("transaction ok");

        // 数据库中添加一个用户，并立即刷新缓存
       // userRepository.saveAndFlush(userEntity);

        // 重定向到用户管理页面，方法为 redirect:url
        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/admin/users/install", method = RequestMethod.GET)
    public String Inatall(ModelMap modelMap) {


        end.InitRun();
        foobean=end.fooclientbean;
        barbean=end.barclientbean;
        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
//        modelMap.addAttribute("foobean",foobean);
//        modelMap.addAttribute("foobean",foobean);
        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/admin/users/transaction", method = RequestMethod.GET)
    public String Transaction() {

         RunChannel foorun=foobean.getRunchannel();
         RunChannel barrun=barbean.getRunchannel();
         String[] str=new String[]{"move","a","b","100"};

         System.out.println("str:"+str.toString());
         System.out.println("foobean:"+foobean);
         System.out.println("barbean:"+barbean);
         System.out.println("foorun success size:"+foorun.successful.size());
         System.out.println("barrun success size:"+barrun.successful.size());
         foorun.SendtTansactionToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),foobean.getSampleorgs(),str);
         barrun.SendtTansactionToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),barbean.getSampleorgs(),str);
         System.out.println("transaction ok");

        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/admin/users/query", method = RequestMethod.GET)
    public String Query() {

        RunChannel foorun=foobean.getRunchannel();
        RunChannel barrun=barbean.getRunchannel();
        String[] str=new String[] {"query", "111"};
        System.out.println("str:"+str.toString());
        System.out.println("foobean:"+foobean);
        System.out.println("barbean:"+barbean);
        System.out.println("foorun success size:"+foorun.successful.size());
        System.out.println("barrun success size:"+barrun.successful.size());

        String result_foo=foorun.SendQuryToPeers(foobean.getClient(),foobean.getChannel(),foobean.getChaincodeid(),str);
        String reult_bar=barrun.SendQuryToPeers(barbean.getClient(),barbean.getChannel(),barbean.getChaincodeid(),str);
        JSONObject json_foo=JSONObject.fromObject(result_foo);


        System.out.println("name:"+json_foo.get("name"));
        System.out.println("transaction ok");

        return "redirect:/admin/users";
    }

    // 查看用户详情
    // @PathVariable可以收集url中的变量，需匹配的变量用{}括起来
    // 例如：访问 localhost:8080/admin/users/show/1 ，将匹配 id = 1
    @RequestMapping(value = "/admin/users/show/{id}", method = RequestMethod.GET)
    public String showUser(@PathVariable("id") Integer userId, ModelMap modelMap) {

        // 找到userId所表示的用户
        //UserEntity userEntity = userRepository.findOne(userId);

        // 传递给请求页面
   //     modelMap.addAttribute("user", userEntity);
        return "admin/userDetail";
    }

    // 更新用户信息 页面
    @RequestMapping(value = "/admin/users/update/{id}", method = RequestMethod.GET)
    public String updateUser(@PathVariable("id") Integer userId, ModelMap modelMap) {

        // 找到userId所表示的用户
     //   UserEntity userEntity = userRepository.findOne(userId);

        // 传递给请求页面
       // modelMap.addAttribute("user", userEntity);
        return "admin/updateUser";
    }

    // 更新用户信息 操作
    @RequestMapping(value = "/admin/users/updateP", method = RequestMethod.POST)
    public String updateUserPost(@ModelAttribute("userP") UserEntity user) {

        // 更新用户信息
//        userRepository.updateUser(user.get, user.getFirstName(),
//                user.getLastName(), user.getPassword(), user.getId());
     //   userRepository.flush(); // 刷新缓冲区
        return "redirect:/admin/users";
    }

    // 删除用户
    @RequestMapping(value = "/admin/users/delete/{id}", method = RequestMethod.GET)
    public String deleteUser(@PathVariable("id") Integer userId) {

        // 删除id为userId的用户
    //    userRepository.delete(userId);
        // 立即刷新
      //  userRepository.flush();
        return "redirect:/admin/users";
    }
}
