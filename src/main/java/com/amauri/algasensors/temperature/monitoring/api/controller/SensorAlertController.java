package com.amauri.algasensors.temperature.monitoring.api.controller;

import com.amauri.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.amauri.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.amauri.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.amauri.algasensors.temperature.monitoring.domain.model.SensorId;
import com.amauri.algasensors.temperature.monitoring.domain.model.SensorMonitoring;
import com.amauri.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
@RequiredArgsConstructor
public class SensorAlertController {

    private final SensorAlertRepository sensorAlertRepository;

    @GetMapping
    public SensorAlertOutput getDetail(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Sensor alert not found"));

        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .minTemperature(sensorAlert.getMinTemperature())
                .build();
    }

    @PutMapping
    public SensorAlertOutput createOrUpdate(@PathVariable TSID sensorId,
                                            @RequestBody SensorAlertInput sensorAlertInput) {

        SensorAlert sensorAlert = findByIdOrDefault(sensorId);
        sensorAlert.setMinTemperature(sensorAlertInput.getMinTemperature());
        sensorAlert.setMaxTemperature(sensorAlertInput.getMaxTemperature());

        sensorAlert = sensorAlertRepository.saveAndFlush(sensorAlert);

        return SensorAlertOutput.builder()
                .id(sensorAlert.getId().getValue())
                .maxTemperature(sensorAlert.getMaxTemperature())
                .minTemperature(sensorAlert.getMinTemperature())
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable TSID sensorId) {
        SensorAlert sensorAlert = sensorAlertRepository.findById(new SensorId(sensorId)).orElse(null);
        if (sensorAlert == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor alert not found");
        }
        sensorAlertRepository.delete(sensorAlert);
    }



    private SensorAlert findByIdOrDefault(TSID sensorId) {
        return sensorAlertRepository.findById(new SensorId(sensorId))
                .orElse(SensorAlert.builder()
                        .id(new SensorId(sensorId))
                        .minTemperature(null)
                        .maxTemperature(null)
                        .build());
    }


}
