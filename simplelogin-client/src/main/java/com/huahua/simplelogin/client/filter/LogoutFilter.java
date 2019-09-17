package com.huahua.simplelogin.client.filter;

import com.huahua.simplelogin.client.constant.AuthConstant;
import com.huahua.simplelogin.client.storage.SessionStorage;
import com.huahua.simplelogin.client.util.HTTPUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogoutFilter implements Filter {
    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config=filterConfig;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();

        String logoutUrl = config.getInitParameter(AuthConstant.LOGOUT_URL);
        String token = (String) session.getAttribute(AuthConstant.TOKEN);

        // 主动注销，即子系统提供的注销请求
        if ("/logout".equals(request.getRequestURI())) {
            // 向认证中心发送注销请求
            Map<String, String> params = new HashMap<String, String>();
            params.put(AuthConstant.LOGOUT_REQUEST, token);
            HTTPUtil.post(logoutUrl, params);
            // 注销后重定向
            response.sendRedirect("/test");
            // 注销本地会话
            session = SessionStorage.INSTANCE.get(token);
            if (session != null) {
                session.invalidate();
            }
            return;
        }

        // 被动注销，即从认证中心发送的注销请求
        token = request.getParameter(AuthConstant.LOGOUT_REQUEST);
        if (token != null && !"".equals(token)) {
            session = SessionStorage.INSTANCE.get(token);
            if (session != null) {
                session.invalidate();
            }
        }
        chain.doFilter(req, res);

    }

    @Override
    public void destroy() {

    }
}
