package com.bocom.bocomManager.repositories;

import com.bocom.bocomManager.models.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Long> {

    Optional<FileModel> findByName(String name);
}
