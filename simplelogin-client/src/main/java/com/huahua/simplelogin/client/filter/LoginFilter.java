package com.huahua.simplelogin.client.filter;

import com.huahua.simplelogin.client.constant.AuthConstant;
import com.huahua.simplelogin.client.storage.SessionStorage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    //用于在init初始化中，接受FilterConfig对象，保存web.xml的信息，以方便读取
    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config=filterConfig;
    }
    //单点登陆过滤器
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse resp= (HttpServletResponse) response;
        HttpSession session=req.getSession();
        //已经登陆，放行
        if(session.getAttribute(AuthConstant.IS_LOGIN) !=null){
            chain.doFilter(request,response);
        }
        //
        String token=req.getParameter(AuthConstant.TOKEN);
        if(token !=null){
            session.setAttribute(AuthConstant.IS_LOGIN,true);
            session.setAttribute(AuthConstant.TOKEN,token);
            //存储，用于注销
            SessionStorage.INSTANCE.set(token,session);
            chain.doFilter(request,response);
            return;
        }
        //重定向至登录页面，并附带当前请求地址
        resp.sendRedirect(config.getInitParameter(AuthConstant.LOGIN_URL)+"?"+AuthConstant.CLIENT_URL+"="+req.getRequestURL());
    }

    @Override
    public void destroy() {

    }
}
