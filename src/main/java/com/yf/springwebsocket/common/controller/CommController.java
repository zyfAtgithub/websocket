package com.yf.springwebsocket.common.controller;

import com.google.gson.GsonBuilder;
import com.yf.springwebsocket.web.websocket.MyWebSocketHandler;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import java.io.IOException;

@Controller
@RequestMapping("/comm")
public class CommController {

    @RequestMapping(value = "/getMsg")
    @ResponseBody
    public String getMsg(String name) {
        return "Hello, " + name;
    }


    @Resource
    MyWebSocketHandler myWebSocketHandler;

    @RequestMapping(value = "/testWebSocket", method ={RequestMethod.POST,RequestMethod.GET},
            produces = "application/json; charset=utf-8")
    @ResponseBody
    public String testWebSocket() throws IOException {
        myWebSocketHandler.sendMessageToJsp(
                new TextMessage(new GsonBuilder().create().toJson("\"number\":\""+"GarlicPriceController/testWebSocket"+"\"")),
                "AAA");
        return "1";
    }

}
