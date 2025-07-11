package com.amauri.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.amauri.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static com.amauri.algasensors.temperature.monitoring.infrastructure.rabbitmq.RabbitMQConfig.QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    @RabbitListener(queues = QUEUE)
    @SneakyThrows
    public void handle(@Payload TemperatureLogData temperatureLogData) {
        TSID sensorId = temperatureLogData.getSensorId();
        Double temperature = temperatureLogData.getValue();
        log.info(String.format("Temperature updated: SensorId %s Temperature %s", sensorId, temperature));

        Thread.sleep(Duration.ofSeconds(5));
    }

}
