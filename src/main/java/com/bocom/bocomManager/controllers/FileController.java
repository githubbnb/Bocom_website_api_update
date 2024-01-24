package com.bocom.bocomManager.controllers;

import com.bocom.bocomManager.dto.ResponseFile;
import com.bocom.bocomManager.dto.ResponseMessage;
import com.bocom.bocomManager.exceptions.ApiError;
import com.bocom.bocomManager.interfaces.hasRoleFileAdd;
import com.bocom.bocomManager.models.FileModel;
import com.bocom.bocomManager.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("files")
@CrossOrigin("*")
public class FileController {

    @Autowired
    private FileService storageService;

    @PostMapping("/upload")
//    @hasRoleFileAdd
    public ResponseEntity<ResponseFile> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileModel store = storageService.store(file);

        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/model/")
                .path(store.getId() + "")
                .toUriString();

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseFile(
                store.getName(),
                "/model/" + store.getId() + "",
                store.getType(),
                store.getData().length));
    }

    @PostMapping("/upload_for_download")
//    @hasRoleFileAdd
    public ResponseEntity<ResponseFile> uploadFileForDownload(@RequestParam("file") MultipartFile file) {
        try {
            FileModel store = storageService.store(file);

            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(store.getId() + "")
                    .toUriString();

            ResponseFile responseFile = new ResponseFile(
                    store.getName(),
                    "" + fileDownloadUri,
                    store.getType(),
                    store.getData().length);

            return ResponseEntity.status(HttpStatus.OK).body(responseFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    @PostMapping("/upload_files_for_download")
//    @hasRoleFileAdd
    public ResponseEntity<List<ResponseFile>> uploadFileForDownload(@RequestParam("files") List<MultipartFile> files) {
        try {
            List<ResponseFile> responseMessages = new ArrayList<>();
            for (MultipartFile file: files) {
                FileModel store = storageService.store(file);

                String fileDownloadUri = ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(store.getId() + "")
                        .toUriString();

                responseMessages.add(new ResponseFile(
                        store.getName(),
                        "" + fileDownloadUri,
                        store.getType(),
                        store.getData().length));
            }

            return ResponseEntity.status(HttpStatus.OK).body(responseMessages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ArrayList<>());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/all/")
                    .path(dbFile.getId() + "")
                    .toUriString();

            return new ResponseFile(
                    dbFile.getName(),
                    "/all/" + dbFile.getId() + "",
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        FileModel fileDB = storageService.getFile(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", fileDB.getName());
        httpHeaders.add(CONTENT_DISPOSITION,"attachment;File-Name=\"" + fileDB.getName                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           () + "\"");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(fileDB.getData());
    }

    @GetMapping("findByName/{name}")
    public ResponseEntity<byte[]> getFile(@PathVariable String name) {
        FileModel fileDB = storageService.getFile(name);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", fileDB.getName());
        httpHeaders.add(CONTENT_DISPOSITION,"attachment;File-Name=\"" + fileDB.getName                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           () + "\"");
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(fileDB.getData());
    }

    @GetMapping("/model/{id}")
    public FileModel getOne(@PathVariable Long id) {
        FileModel fileDB = storageService.getFile(id);
        return fileDB;
    }
}
