package com.example.spring_boot_example.service;

import com.example.spring_boot_example.entity.Record;
import com.example.spring_boot_example.repository.RecordRepository;
import com.example.spring_boot_example.entity.RecordStatus;
import com.example.spring_boot_example.entity.dto.RecordsContainerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class RecordService {
    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository){
        this.recordRepository = recordRepository;
    }

    public RecordsContainerDto findAllRecords(String filterMode){
        List<Record> records = recordRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
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
            recordRepository.save(new Record(title));
        }
    }

    public void setRecordStatus(int id, RecordStatus newStatus){
        recordRepository.update(id, newStatus);
    }

    public void deleteRecord(int id){
        recordRepository.deleteById(id);
    }
}
