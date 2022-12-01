package com.example.demo.src.team.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class PastUserDisc {
    private int userDiscIdx;
    private double x;
    private double y;
    private double dPercent;
    private double iPercent;
    private double sPercent;
    private double cPercent;
}
