package com.nttdata.bc39.grupo04.movements.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MovementsDTO {
    private String number;
    private double amount;
    private String type;
    private Date date;
}
