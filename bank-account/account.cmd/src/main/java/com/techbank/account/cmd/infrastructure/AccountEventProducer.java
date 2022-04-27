package com.techbank.account.cmd.infrastructure;

import com.techbank.cqrs.core.event.BaseEvent;
import com.techbank.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountEventProducer implements EventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void produce(String topicName, BaseEvent baseEvent) {
        kafkaTemplate.send(topicName, baseEvent);
    }
}
