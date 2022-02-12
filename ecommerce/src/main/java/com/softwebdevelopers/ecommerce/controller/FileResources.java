package com.softwebdevelopers.ecommerce.controller;

import com.softwebdevelopers.ecommerce.business.ImageBL;
import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Message;
import com.softwebdevelopers.ecommerce.dto.ImageDto;
import com.softwebdevelopers.ecommerce.services.IFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RestController
public class FileResources {

    @Autowired
    ImageBL imageService;

    @Autowired
    private IFileStorageService fileStorageService;

    @PostMapping("/api/auth/uploadFile/{directory}/{fileType}")
    public ResponseEntity<?> uploadFile(@PathVariable String directory, @PathVariable String fileType, @RequestParam("file") MultipartFile file) {
        ImageDto uploaded = imageService.uploadFile(directory, fileType, file);

        if(uploaded == null) {
            log.warn("The file [{}] upload failed.", file.getName());
            return new ResponseEntity<>(Message.success(ECOMMessage.CREATION_FAILED), new HttpHeaders(), HttpStatus.NOT_IMPLEMENTED);
        }
        return new ResponseEntity<>(uploaded, new HttpHeaders(), HttpStatus.OK);

//        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("/downloadFile/")
//                .path(fileName)
//                .toUriString();
//
//        return new ImageDto().toBuilder().fileName(fileName)
//                .fileDownloadUri(fileDownloadUri)
//                .fileType(file.getContentType())
//                .size(file.getSize())
//                .build();
    }

    @GetMapping("/downloadFile/{dirName}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String dirName, @PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(dirName, fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
