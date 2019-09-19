package com.huahua.simplelogin.server.controller;

import com.huahua.simplelogin.client.constant.AuthConstant;
import com.huahua.simplelogin.client.entry.User;
import com.huahua.simplelogin.client.storage.SessionStorage;
import com.huahua.simplelogin.client.util.HTTPUtil;
import com.huahua.simplelogin.server.service.UserService;
import com.huahua.simplelogin.server.storage.ClientStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, User input, Model model){
        User user=userService.find(input);
        //登陆验证
        if(user ==null){
            model.addAttribute("error","username or password is wrong");
            return "redirect:/";
        }
        //验证成功后，授权,随机生成一个唯一的ID值作为token
        String token= UUID.randomUUID().toString();
        request.getSession().setAttribute(AuthConstant.IS_LOGIN,true);
        request.getSession().setAttribute(AuthConstant.TOKEN,token);
        //存储，用于注销
        SessionStorage.INSTANCE.set(token,request.getSession());
        //子系统跳转过来的登陆请求，授权，存储后，跳转回去
        String clientUrl=request.getParameter(AuthConstant.CLIENT_URL);
        if(clientUrl !=null && "".equals(clientUrl)){
            //存储
            ClientStorage.INSTANCE.set(token,clientUrl);
            return "redirect:"+clientUrl+"?"+AuthConstant.TOKEN+"="+token;
        }
        return "redirect:/";
    }
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        String token=request.getParameter(AuthConstant.LOGOUT_REQUEST);
        if(token!=null && !"".equals(token)){
            session=SessionStorage.INSTANCE.get(token);
        }else{
            token=(String)session.getAttribute(AuthConstant.TOKEN);
        }
        if(session!=null){
            session.invalidate();
        }
        //注销子系统
        List<String> list=ClientStorage.INSTANCE.get(token);
        if(list !=null &&list.size()>0){
            Map<String,String> params=new HashMap<>();
            params.put(AuthConstant.LOGOUT_REQUEST,token);
            for (String url :list){
                HTTPUtil.post(url,params);
            }
        }
        return "redirect:/";
    }
}
