package com.java.springportfolio.service;

import com.java.springportfolio.dto.ImageDeleteRequest;
import com.java.springportfolio.entity.FileRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    String saveImageToServer(MultipartFile file);

    void deleteImageFromServer(ImageDeleteRequest imageDeleteRequest);

    List<FileRecord> saveFileRecordsToDatabase(List<FileRecord> fileRecordEntityList);

    void deleteFileRecordsFromDatabase(List<FileRecord> fileEntityIdList);
}
