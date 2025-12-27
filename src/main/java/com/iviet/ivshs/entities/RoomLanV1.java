package com.iviet.ivshs.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "room_lan_v1",
    indexes = {
        @Index(name = "idx_room_id_room_lang_code", columnList = "room_id, lang_code", unique = true)
    }
)
public class RoomLanV1 extends BaseTranslationV1<RoomV1> {
}