package com.montojo.springkafka.controller;

import com.montojo.springkafka.dao.Message;
import com.montojo.springkafka.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MessageController {

    @Autowired
    Producer producer;

    @RequestMapping("/")
    public String testController(){

        return "Testing controller";
    }

    @RequestMapping("/submit")
    public String sendMessage(@RequestParam("text") String text){
        Message message = new Message(text);
        producer.writeToKafka(message);
        return "Saved successfully";
    }
}
