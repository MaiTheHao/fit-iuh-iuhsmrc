package com.iviet.ivshs.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "power_consumption_lan_v1",
    indexes = {
        @Index(name = "idx_sensor_id_sensor_lang_code", columnList = "sensor_id, lang_code", unique = true)
    }
)
public class PowerConsumptionLanV1 extends BaseTranslationV1<PowerConsumptionV1> {
}