package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.common.FeaturedImage;
import com.softwebdevelopers.ecommerce.models.user.User;
import org.springframework.data.domain.Page;

public interface IFeaturedImageService {

    Page<FeaturedImage> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException;

    FeaturedImage getById(Long id) throws RecordNotFoundException;

    FeaturedImage create(FeaturedImage featured)  throws RecordNotFoundException;

    FeaturedImage update(FeaturedImage featured)  throws RecordNotFoundException;

    FeaturedImage publishedById(Long id) throws RecordNotFoundException;

    FeaturedImage delete(Long id) throws RecordNotFoundException;
}
