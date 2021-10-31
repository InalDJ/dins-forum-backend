package com.java.springportfolio.service;

import com.java.springportfolio.config.WebConfig;
import com.java.springportfolio.dao.FileRepository;
import com.java.springportfolio.dto.ImageDeleteRequest;
import com.java.springportfolio.entity.FileRecord;
import com.java.springportfolio.exception.PortfolioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class MediaServiceImpl implements MediaService {

    private final FileRepository fileRepository;

    @Override
    public String saveImageToServer(MultipartFile file) {
        return copyFile(file);
    }

    @Override
    //TODO need to change
    public void deleteImageFromServer(ImageDeleteRequest imageDeleteRequest) {
        for (String path : imageDeleteRequest.getImagePathList()) {
            try {
                Files.delete(Path.of(path));
            } catch (Exception e) {
                log.error("Error while removing the file from the server! Error - {}", e.getMessage());
                throw new PortfolioException("Failed to delete a file!");
            }
        }
    }

    @Override
    public List<FileRecord> saveFileRecordsToDatabase(List<FileRecord> fileRecordEntityList) {
        return fileRepository.saveAll(fileRecordEntityList);
    }

    @Override
    public void deleteFileRecordsFromDatabase(List<FileRecord> fileEntityIdList) {
        fileRepository.deleteInBatch(fileEntityIdList);
    }

    private String copyFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        Path path = getImagePath(fileName);
        if (Files.exists(path)) {
            fileName = UUID.randomUUID() + "_copy_" + fileName;
            path = getImagePath(fileName);
        }
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, path);
            return getImageUrl(fileName) + "," + path;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error while creating a file on the server! Error - {}", e.getMessage());
            throw new PortfolioException("Failed to upload!");
        }
    }

    private static void createDirectoryIfItDoesntExist(String dir) {
        final Path path = Paths.get(dir);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.error("Cannot create a directory. Error - {}", e.getMessage());
                throw new PortfolioException("Failed to upload!");
            }
        }
    }

    private static Path getImagePath(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(WebConfig.IMAGE_FILE_BASE);
        sb.append(Year.now().getValue());

        createDirectoryIfItDoesntExist(sb.toString());
        sb.append("/");
        sb.append(YearMonth.now().getMonthValue());

        createDirectoryIfItDoesntExist(sb.toString());
        sb.append("/");
        sb.append(fileName);
        return Paths.get(sb.toString());
    }

    private static String getImageUrl(String imageFileName) {
        String baseUrl = WebConfig.BASE_URL;
        StringBuilder sb = new StringBuilder();

        sb.append(baseUrl);
//        if (baseUrl.endsWith("/")) {
//            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
//        }
        sb.append(WebConfig.IMAGE_RESOURCE_BASE);
        sb.append(getYearAndMonthUrlFragment());
        sb.append(imageFileName);
        return sb.toString();
    }

    private static String getYearAndMonthUrlFragment() {
        StringBuilder sb = new StringBuilder();
        sb.append(Year.now().getValue());
        sb.append("/");
        sb.append(YearMonth.now().getMonthValue());
        sb.append("/");
        return sb.toString();
    }
}
