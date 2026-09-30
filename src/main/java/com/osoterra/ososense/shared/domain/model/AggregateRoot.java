package com.osoterra.ososense.shared.domain.model;

import com.osoterra.ososense.shared.domain.events.DomainEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for aggregate roots. Holds the aggregate's identity and accumulates the
 * domain events raised by its business methods until they are pulled and published by
 * the application layer.
 *
 * @param <ID> the type of the aggregate's identifier
 */
public abstract class AggregateRoot<ID> {

    private final transient List<DomainEvent> domainEvents = new ArrayList<>();

    protected final ID id;

    protected AggregateRoot(ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    /**
     * Returns the events raised so far and clears them. Intended to be called once,
     * right after the aggregate has been persisted, by the code that publishes them.
     */
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }
}
