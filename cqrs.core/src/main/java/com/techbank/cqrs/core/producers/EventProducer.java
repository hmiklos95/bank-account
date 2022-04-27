package com.techbank.cqrs.core.producers;

import com.techbank.cqrs.core.event.BaseEvent;

public interface EventProducer {

    void produce(String topicName, BaseEvent baseEvent);
}
