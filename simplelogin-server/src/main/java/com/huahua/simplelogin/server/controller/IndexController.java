package com.huahua.simplelogin.server.controller;


import com.huahua.simplelogin.client.constant.AuthConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model){
    model.addAttribute(AuthConstant.CLIENT_URL,request.getParameter(AuthConstant.CLIENT_URL));
    return "index";
    }

    @RequestMapping("/success")
    public String success(){
        return "success";
    }
}
