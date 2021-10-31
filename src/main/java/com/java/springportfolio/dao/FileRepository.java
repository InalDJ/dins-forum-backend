package com.java.springportfolio.dao;

import com.java.springportfolio.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileRecord, Long> {
}
