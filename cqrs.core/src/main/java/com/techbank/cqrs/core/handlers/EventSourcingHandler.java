package com.techbank.cqrs.core.handlers;

import com.techbank.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandler<T> {

    void save(AggregateRoot aggregateRoot);

    /**
     *
     * @param id aggregate id
     * @return
     */
    T getById(String id);
}
