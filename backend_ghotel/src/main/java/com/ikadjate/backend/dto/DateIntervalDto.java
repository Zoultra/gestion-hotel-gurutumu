package com.ikadjate.backend.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DateIntervalDto {
	
	    private LocalDate startDate;
	    private LocalDate endDate;

}
