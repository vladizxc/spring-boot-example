package com.example.spring_boot_example.service;

import com.example.spring_boot_example.entity.Record;
import com.example.spring_boot_example.entity.User;
import com.example.spring_boot_example.repository.RecordRepository;
import com.example.spring_boot_example.entity.RecordStatus;
import com.example.spring_boot_example.entity.dto.RecordsContainerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecordService {
    private final UserService userService;
    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository, UserService userService){
        this.userService = userService;
        this.recordRepository = recordRepository;
    }

    public RecordsContainerDto findAllRecords(String filterMode){
        User user = userService.getCurrentUser();

        List<Record> records = user.getRecords().stream()
                .sorted(Comparator.comparingInt(Record::getId))
                .collect(Collectors.toList());

        int numberOfActiveRecords = (int) records.stream().filter(record -> record.getStatus() == RecordStatus.ACTIVE).count();
        int numberOfDoneRecords = (int) records.stream().filter(record -> record.getStatus() == RecordStatus.DONE).count();
        if (filterMode == null || filterMode.isBlank()){
            return new RecordsContainerDto(user.getName(), records, numberOfDoneRecords, numberOfActiveRecords);
        };

        String filterModeInUpperCase = filterMode.toUpperCase();

        List<String> allowedFilterModes = Arrays.stream(RecordStatus.values())
                .map(Enum::name)
                .toList();
        if (allowedFilterModes.contains(filterModeInUpperCase)){
            List<Record> filteredRecords = records.stream().filter(record -> record.getStatus() == RecordStatus.valueOf(filterModeInUpperCase))
                    .toList();
            return new RecordsContainerDto(user.getName(), filteredRecords, numberOfDoneRecords, numberOfActiveRecords);
        } else {
            return new RecordsContainerDto(user.getName(), records, numberOfDoneRecords, numberOfActiveRecords);
        }
    }

    public void saveRecord(String title){
        if(title != null && !title.isBlank()) {
            recordRepository.save(new Record(title, userService.getCurrentUser()));
        }
    }

    public void setRecordStatus(int id, RecordStatus newStatus){
        recordRepository.update(id, newStatus);
    }

    public void deleteRecord(int id){
        recordRepository.deleteById(id);
    }
}
