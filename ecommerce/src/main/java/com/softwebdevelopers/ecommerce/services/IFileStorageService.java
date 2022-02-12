package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.FileStorageDto;
import com.softwebdevelopers.ecommerce.dto.ImageDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

    ImageDto storeFile(MultipartFile file, FileStorageDto fileInfo);

    String storeFile(MultipartFile file);

    Resource loadFileAsResource(String dirName, String fileName);
}
