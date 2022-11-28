package com.nttdata.bc39.grupo04.movements.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class MovementsExplainDTO {
    private String number;
    private double depositAmount;
    private double withdrawlAmount;
    private Date date;
}
