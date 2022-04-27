package com.techbank.account.query.infrastructure.consumers;

import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

public interface EventConsumer {

    void consume(@Payload AccountOpenedEvent accountOpenedEvent, Acknowledgment ack);
    void consume(@Payload AccountClosedEvent accountClosedEvent, Acknowledgment ack);
    void consume(@Payload FundsWithdrawnEvent fundsWithdrawnEvent, Acknowledgment ack);
    void consume(@Payload FundsDepositedEvent fundsDepositedEvent, Acknowledgment ack);
}
