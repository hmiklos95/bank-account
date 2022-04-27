package com.techbank.cqrs.core.domain;

import com.techbank.cqrs.core.event.BaseEvent;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate is an entity or a group of entities that is always kept in a consistent state
 * Aggregate root is the entity within the aggregate that is responsible for maintaining this consistent state
 */
public abstract class AggregateRoot {

    protected String id;
    private int version = -1;

    private final List<BaseEvent> changes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


    public List<BaseEvent> getUncommittedChanges() {
        return this.changes;
    }

    public void markChangesCommitted() {
        this.changes.clear();
    }

    protected void applyChange(BaseEvent event, Boolean isNewEvent) {
        //TODO avoid using reflection
        try {
            Method method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (NoSuchMethodException e) {
            //log.error("The apply method with the required type not found");
        } catch (Exception e) {
            //log.error("Error occurred while invoking");
        }

        if (isNewEvent) {
            changes.add(event);
        }
    }

    public void raiseEvent(BaseEvent event) {
        applyChange(event, true);
    }

    public void replayEvents(Iterable<BaseEvent> events) {
        //not new events, recreate the state of the aggregate
        events.forEach(event -> applyChange(event, false));
    }
}
