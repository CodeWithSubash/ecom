package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.common.Image;

import java.util.List;
import java.util.Optional;

public interface IImageService {

    Image getById(Long id, String modelType);

    List<Image> getAllById(Long id, String modelType);

    Image create(Image image) throws RecordNotFoundException;

    List<Image> saveAll(List<Image> image) throws RecordNotFoundException;
}
