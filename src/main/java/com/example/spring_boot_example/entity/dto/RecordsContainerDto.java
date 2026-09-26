package com.example.spring_boot_example.entity.dto;

import com.example.spring_boot_example.entity.Record;
import java.util.List;

public class RecordsContainerDto {

    private final String userName;

    private final List<Record> records;

    private final int numberOfDoneRecords;

    private final int numberOfActiveRecords;

    public RecordsContainerDto(String userName, List<Record> records, int numberOfDoneRecords, int numberOfActiveRecords) {
        this.userName = userName;
        this.records = records;
        this.numberOfDoneRecords = numberOfDoneRecords;
        this.numberOfActiveRecords = numberOfActiveRecords;
    }

    public List<Record> getRecords() {
        return records;
    }

    public int getNumberOfDoneRecords() {
        return numberOfDoneRecords;
    }

    public int getNumberOfActiveRecords() {
        return numberOfActiveRecords;
    }

    public String getUserName() {
        return userName;
    }
}
