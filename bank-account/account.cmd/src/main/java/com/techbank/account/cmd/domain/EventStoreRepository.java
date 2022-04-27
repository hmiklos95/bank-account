package com.techbank.account.cmd.domain;

import com.techbank.cqrs.core.event.EventModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.annotation.Resource;
import java.util.List;

@Resource
public interface EventStoreRepository extends MongoRepository<EventModel, String> {

    List<EventModel> findByAggregateIdentifier(String aggregateIdentifier);
}
