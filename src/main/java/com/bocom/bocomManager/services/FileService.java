package com.bocom.bocomManager.services;


import com.bocom.bocomManager.models.FileModel;
import com.bocom.bocomManager.repositories.FileRepository;
import com.google.common.io.Files;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String getExtensionByGuava(String filename) {
        return Files.getFileExtension(filename);
    }

    public FileModel store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtensionByGuava(fileName);
        String str = fileName.replace("." + extension, "");

        fileName = str + "_" + System.currentTimeMillis() + "." + extension;

        FileModel FileDB = new FileModel(fileName, file.getContentType(), file.getBytes());

        return fileRepository.save(FileDB);
    }

    public FileModel getFile(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public FileModel getFile(String name) {
        return fileRepository.findByName(name).orElse(null);
    }

    public Stream<FileModel> getAllFiles() {
        return fileRepository.findAll().stream();
    }

    public FileModel findByNameAndType(String name, String type) {
        Optional<FileModel> first = getAllFiles().filter(fileModel -> fileModel.getName().equalsIgnoreCase(name) && fileModel.getType().equalsIgnoreCase(type)).findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            return new FileModel();
        }
    }
}
