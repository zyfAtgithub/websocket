package com.yf.springwebsocket.web.websocket;


import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    /**
     * 存放所有的连接
     */
    public static final Map<String, WebSocketSession> userSocketSessionMap;

    static {
        userSocketSessionMap = new HashMap<String, WebSocketSession>();
    }

    /**
     * 连接建立成功后
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String jspCode = (String) session.getHandshakeAttributes().get("jspCode");
        if (userSocketSessionMap.get(jspCode) == null) {
            userSocketSessionMap.put(jspCode, session);
        }
        //broadcast(new TextMessage(new GsonBuilder().create().toJson("\"number\":\""+i+"\"")));
        session.sendMessage(new TextMessage(new GsonBuilder().create().toJson("server==>连接已建立！")));
    }

    /**
     * 处理消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //发送消息
        TextMessage retMsg = new TextMessage("server==>我收到了"+message.getPayload());
        session.sendMessage(retMsg);
    }


    /**
     * 处理错误
     * @param session
     * @param throwable
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        Iterator<Map.Entry<String, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Map.Entry<String, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(session.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                break;
            }
        }
    }

    /**
     * 连接关闭
     * @param session
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Websocket:" + session.getId() + "已经关闭");
        Iterator<Map.Entry<String, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();
        // 移除Socket会话
        while (it.hasNext()) {
            Map.Entry<String, WebSocketSession> entry = it.next();
            if (entry.getValue().getId().equals(session.getId())) {
                userSocketSessionMap.remove(entry.getKey());
                System.out.println("Socket会话已经移除:用户ID" + entry.getKey());
                break;
            }
        }
    }


    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


    public void broadcast(final TextMessage message) throws IOException {
        Iterator<Map.Entry<String, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();

        // 多线程群发
        while (it.hasNext()) {

            final Map.Entry<String, WebSocketSession> entry = it.next();

            if (entry.getValue().isOpen()) {
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            if (entry.getValue().isOpen()) {
                                entry.getValue().sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();
            }

        }
    }

    /**
     * 给所有在线用户的实时工程检测页面发送消息
     *
     * @param message
     * @throws IOException
     */
    public void sendMessageToJsp(final TextMessage message,String type) throws IOException {
        Iterator<Map.Entry<String, WebSocketSession>> it = userSocketSessionMap
                .entrySet().iterator();

        // 多线程群发
        while (it.hasNext()) {
            final Map.Entry<String, WebSocketSession> entry = it.next();
            if (entry.getValue().isOpen() && entry.getKey().contains(type)) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            if (entry.getValue().isOpen()) {
                                entry.getValue().sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        }
    }
}
