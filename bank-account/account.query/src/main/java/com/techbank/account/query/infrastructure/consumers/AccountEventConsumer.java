package com.techbank.account.query.infrastructure.consumers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.account.query.infrastructure.handlers.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer implements EventConsumer {

    @Autowired
    private EventHandler eventHandler;

    //ha egy topicra ugyanaz a group id tobb microservice-szel, akkor loadbalance-olt
    //ha nem, akkor egymástol fuggetlenul mukodnek
    @KafkaListener(topics = "AccountOpenedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountOpenedEvent accountOpenedEvent, Acknowledgment ack) {
        eventHandler.on(accountOpenedEvent);

        //csak akkor ac, ha sikeresen lementjuk a db-be
        ack.acknowledge();
    }

    @KafkaListener(topics = "AccountClosedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(AccountClosedEvent accountClosedEvent, Acknowledgment ack) {
        eventHandler.on(accountClosedEvent);

        ack.acknowledge();
    }

    @KafkaListener(topics = "FundsWithdrawnEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsWithdrawnEvent fundsWithdrawnEvent, Acknowledgment ack) {
        eventHandler.on(fundsWithdrawnEvent);
        ack.acknowledge();
    }

    @KafkaListener(topics = "FundsDepositedEvent", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(FundsDepositedEvent fundsDepositedEvent, Acknowledgment ack) {
        eventHandler.on(fundsDepositedEvent);
        ack.acknowledge();
    }
}
