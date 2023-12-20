package com.pkletsko.hello.world.reactive.kafka.service;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Service
@Slf4j
public class UserInfoListener {
    private Disposable messageSubscription;

    @Getter
    private final Flux<ReceiverRecord<String, com.pkletsko.integration.model.avro.UserInfoAvro>> userInfoPublisher;

    public UserInfoListener(
            KafkaReceiver<String, com.pkletsko.integration.model.avro.UserInfoAvro> userInfoAvroKafkaReceiver,
            @Value("${subscribe.on.init}") boolean subscribeOnInit
    ) {
        userInfoPublisher = Flux.defer(userInfoAvroKafkaReceiver::receive)
                .doOnNext(kafkaMsg -> logKafkaMsgState(kafkaMsg, "msg processed"));

        if (subscribeOnInit) {
            messageSubscription = userInfoPublisher.subscribe();
        }
    }

    private static void logKafkaMsgState(ReceiverRecord<String, com.pkletsko.integration.model.avro.UserInfoAvro> kafkaMsg, String stateDescription) {
        log.info("### " + stateDescription + ": {}", kafkaMsg.value().toString());
    }

    @PreDestroy
    void cancelSubscription() {
        if (messageSubscription != null) {
            messageSubscription.dispose();
        }
    }
}
