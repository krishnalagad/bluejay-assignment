package com.learnspring.bluejayassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bluejay {

    private String positionId;
    private String positionStatus;
    private String time;
    private String timeOut;
    private String timecardHours;
    private String payCycleStartDate;
    private String payCycleEndDate;
    private String employeeName;
    private String fileNumber;

}