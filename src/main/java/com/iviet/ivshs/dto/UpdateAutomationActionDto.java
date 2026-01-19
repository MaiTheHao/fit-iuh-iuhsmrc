package com.iviet.ivshs.dto;

import com.iviet.ivshs.enumeration.JobActionType;
import com.iviet.ivshs.enumeration.JobTargetType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAutomationActionDto {

	private JobTargetType targetType;

	private Long targetId;

	private JobActionType actionType;

	private String parameterValue;

	private Integer executionOrder;
}
