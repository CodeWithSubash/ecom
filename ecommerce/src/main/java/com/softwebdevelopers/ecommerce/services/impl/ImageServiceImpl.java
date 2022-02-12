package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.Image;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.ImageRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IImageService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements IImageService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    ImageRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public Image getById(Long id, String modelType) {
        Optional<Image> image = repo.getByModelId(id, modelType);
        if (image.isPresent()) {
            log.info("The image with Id: [{}] returned successfully.", id);
            return image.get();
        } else {
            log.warn("The image [0] fetched failed. The provided Id: [{}] is not found.", id);
            return null;
        }
    }

    @Override
    public List<Image> getAllById(Long id, String modelType) {
        Optional<List<Image>> image = repo.getAllByModelId(id, modelType);
        if (image.isPresent()) {
            log.info("The image with Id: [{}] returned successfully.", id);
            return image.get();
        } else {
            log.warn("The image [0] fetched failed. The provided Id: [{}] is not found.", id);
            return null;
        }
    }

    @Override
    public Image create(Image image) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            Image entity = repo.save(image);

            if (entity != null) {
                log.info("The image [{}] created successfully.", entity.getId());
            } else {
                log.info("The image [{}] creation failed.", image.getFileName());
            }
            return entity;
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }

    @Override
    public List<Image> saveAll(List<Image> images) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            List<Image> entity = repo.saveAll(images);

            if (entity != null) {
                log.info("The multiple image [{}] created successfully.");
            } else {
                log.info("The multiple image [{}] creation failed.");
            }
            return entity;
        } else {
            log.warn("The user [0] fetched failed. The provided username: [{}] is not found.",
                    securityContext.getUsername());
            throw new RecordNotFoundException(
                    "No user record exists for given username: [" + securityContext.getUsername() + "].");
        }
    }
}
