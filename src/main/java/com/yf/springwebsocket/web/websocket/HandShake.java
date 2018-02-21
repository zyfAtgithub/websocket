package com.yf.springwebsocket.web.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 自定义握手类
 */
public class HandShake implements HandshakeInterceptor {

    /**
     * 建立握手之前的处理
     * @param request
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param attributes
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        String jspCode = ((ServletServerHttpRequest) request).getServletRequest().getParameter("jspCode");
        // 标记用户
        //String userId = (String) session.getAttribute("userId");
        if(jspCode!=null){
            attributes.put("jspCode", jspCode);
        }else{
            return false;
        }
        return true;    }

    /**
     * 握手成功之后的处理
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {

    }
}
