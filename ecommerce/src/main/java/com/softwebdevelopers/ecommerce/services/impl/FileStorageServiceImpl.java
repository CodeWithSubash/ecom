package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.config.FileStorageProperties;
import com.softwebdevelopers.ecommerce.dto.FileStorageDto;
import com.softwebdevelopers.ecommerce.dto.ImageDto;
import com.softwebdevelopers.ecommerce.exceptions.FileNotFoundException;
import com.softwebdevelopers.ecommerce.exceptions.FileStorageException;
import com.softwebdevelopers.ecommerce.services.IFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements IFileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

//        String advisorPptFilePath = "/src/main/resources/static/images/category";
//        this.fileStorageLocation = Paths.get(System.getProperty("user.dir") + advisorPptFilePath).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public ImageDto storeFile(MultipartFile file, FileStorageDto fileInfo) {

        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "";

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            String fileExtension = "";

            try {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            } catch (Exception e) {
                fileExtension = "";
            }
            fileName = fileInfo.getTitle() + "_" + fileInfo.getUuid() + fileExtension;

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = Paths.get(this.fileStorageLocation.toString() + "/" + fileInfo.getDirName()).resolve(fileName);
            if (!Files.exists(targetLocation.getParent()))
                Files.createDirectories(targetLocation.getParent());

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return new ImageDto().toBuilder()
                    .fileName(fileName)
                    .imagePath(ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/downloadFile/" + fileInfo.getDirName() + "/")
                            .path(fileName)
                            .toUriString())
                    .uuid(fileInfo.getUuid())
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .fileDownloadUri(ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/downloadFile/" + fileInfo.getDirName() + "/")
                            .path(fileName)
                            .toUriString())
                    .build();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String dirName, String fileName) {
        try {
            Path filePath = Paths.get(this.fileStorageLocation.toString() + "/" + dirName).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}
