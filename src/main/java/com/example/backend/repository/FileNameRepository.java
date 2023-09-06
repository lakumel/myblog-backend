package com.example.backend.repository;

import com.example.backend.domain.FileName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileNameRepository extends JpaRepository<FileName, Long> {
    public Optional<FileName> findFileNameByStoredFileName(String storedFileName);
}
