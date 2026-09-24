package com.example.spring_boot_example.service;

import com.example.spring_boot_example.entity.Record;
import com.example.spring_boot_example.dao.RecordDao;
import com.example.spring_boot_example.entity.RecordStatus;
import com.example.spring_boot_example.entity.dto.RecordsContainerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class RecordService {
    private final RecordDao recordDao;

    @Autowired
    public RecordService(RecordDao recordDao){
        this.recordDao = recordDao;
    }

    public RecordsContainerDto findAllRecords(String filterMode){
        List<Record> records = recordDao.findALlRecords();
        int numberOfActiveRecords = (int) records.stream().filter(record -> record.getStatus() == RecordStatus.ACTIVE).count();
        int numberOfDoneRecords = (int) records.stream().filter(record -> record.getStatus() == RecordStatus.DONE).count();
        if (filterMode == null || filterMode.isBlank()){
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        };

        String filterModeInUpperCase = filterMode.toUpperCase();

        List<String> allowedFilterModes = Arrays.stream(RecordStatus.values())
                .map(Enum::name)
                .toList();
        if (allowedFilterModes.contains(filterModeInUpperCase)){
            List<Record> filteredRecords = records.stream().filter(record -> record.getStatus() == RecordStatus.valueOf(filterModeInUpperCase))
                    .toList();
            return new RecordsContainerDto(filteredRecords, numberOfDoneRecords, numberOfActiveRecords);
        } else {
            return new RecordsContainerDto(records, numberOfDoneRecords, numberOfActiveRecords);
        }
    }

    public void saveRecord(String title){
        if(title != null && !title.isBlank()) {
            recordDao.saveRecord(new Record(title));
        }
    }

    public void setRecordStatus(int id, RecordStatus newStatus){
        recordDao.updateRecordStatus(id, newStatus);
    }

    public void deleteRecord(int id){
        recordDao.deleteRecord(id);
    }
}
