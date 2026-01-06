package com.iviet.ivshs.dto;

import com.iviet.ivshs.enumeration.ClientType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    
    private Long id;
    private String username;
    private ClientType clientType;
    private String ipAddress;
    private String macAddress;
    private String avatarUrl;
    private Date lastLoginAt;
}
