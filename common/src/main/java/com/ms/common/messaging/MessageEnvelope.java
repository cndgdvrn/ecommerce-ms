package com.ms.common.messaging;


import java.time.Instant;
import java.util.UUID;

public class MessageEnvelope<T> {

    UUID messageId;
    String messageType;
    String aggregateType;
    String aggregateId;
    UUID correlationId;
    UUID causationId;
    Integer version;
    Instant occurredAt;
    T payload;

    public MessageEnvelope(){}


    public MessageEnvelope(String messageType, String aggregateType, String aggregateId, UUID correlationId, UUID causationId, Integer version,  T payload) {
        this.messageId = UUID.randomUUID();
        this.messageType = messageType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.version = version;
        this.occurredAt = Instant.now();
        this.payload = payload;
    }

    public MessageEnvelope(UUID messageId, String messageType, String aggregateType, String aggregateId, UUID correlationId, UUID causationId, Integer version, T payload) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.version = version;
        this.occurredAt = Instant.now();
        this.payload = payload;
    }

    public UUID getMessageId() {
        return messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public UUID getCausationId() {
        return causationId;
    }

    public Integer getVersion() {
        return version;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public T getPayload() {
        return payload;
    }
}
