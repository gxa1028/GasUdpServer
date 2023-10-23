package com.shu.iot.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Msg2KafkaHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOG = LoggerFactory.getLogger(Msg2KafkaHandler.class);
    private final KafkaProducer<String, String> kafkaProducer;


    public Msg2KafkaHandler(){
        // 1. 创建kafka生产者的配置对象
        Properties kafkaProperties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kafka.properties")) {
            kafkaProperties.load(inputStream);
        } catch (IOException io) {
            throw new RuntimeException("config file error or not exists");
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        // 3. 创建kafka生产者对象
        kafkaProducer = new KafkaProducer<String, String>(kafkaProperties);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        LOG.info("receive message:" + msg);
        kafkaProducer.send(new ProducerRecord<>("quickstart-events", msg));
    }
}
