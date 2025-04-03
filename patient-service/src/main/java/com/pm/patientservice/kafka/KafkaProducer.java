package com.pm.patientservice.kafka;

import com.pm.patientservice.entity.PatientEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import patient.events.PatientEvent;


@RequiredArgsConstructor
@Component
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(PatientEntity patient){
        String topic = "patient";
        PatientEvent patientEvent = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();
        try {
            kafkaTemplate.send(topic, patientEvent.toByteArray());
        }catch (Exception e){
            log.info("Patient event sent to Kafka failed: {}", patientEvent);
            log.error("Error sending event to Kafka: {}", e.getMessage());
        }

    }
}
