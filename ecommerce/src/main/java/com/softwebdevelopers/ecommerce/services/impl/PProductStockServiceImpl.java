package com.softwebdevelopers.ecommerce.services.impl;

import com.softwebdevelopers.ecommerce.common.ECOMMessage;
import com.softwebdevelopers.ecommerce.common.Preconditions;
import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.product.ProductStocks;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.repository.PProductStocksRepository;
import com.softwebdevelopers.ecommerce.repository.UUserRepository;
import com.softwebdevelopers.ecommerce.services.IPProductStockService;
import com.softwebdevelopers.ecommerce.utils.ECOMSecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class PProductStockServiceImpl implements IPProductStockService {

    ECOMSecurityContextHolder securityContext = null;

    @Autowired
    PProductStocksRepository repo;

    @Autowired
    UUserRepository userRepository;

    @Override
    public Page<ProductStocks> getAll(PagingSortingAndSearchDto page) throws RecordNotFoundException {
        return null;
    }

    @Override
    public Page<ProductStocks> getAllByProductId(Long product_id, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<ProductStocks> productList = null;
        if (Preconditions.checkNotBlank(page.getKeyword())) {
            productList = repo.getAllByProductIdAndKeywordWithPagination(product_id, page.getKeyword(), indexPageWithElements);
        } else {
            productList = repo.getAllByProductIdWithPagination(product_id, indexPageWithElements);
        }
        if (Preconditions.checkNotNull(productList)) {
            log.info("The product stock list returned successfully.");
            return productList;
        } else {
            log.warn("The product stock [0] fetched failed. The product stock list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public Page<ProductStocks> getAllByProductIdAndDateRange(Long product_id, LocalDateTime startDate, LocalDateTime endDate, PagingSortingAndSearchDto page) throws RecordNotFoundException {
        Pageable indexPageWithElements = PageRequest.of(page.getPageNo(), page.getPageSize(),
                page.getOrderType().equalsIgnoreCase("asc") ? Sort.by(page.getOrderBy()).ascending() : Sort.by(page.getOrderBy()).descending());

        Page<ProductStocks> productList = null;
        if (Preconditions.checkNotBlank(page.getKeyword())) {
            productList = repo.getAllByProductIdAndKeywordWithPagination(product_id, page.getKeyword(), indexPageWithElements);
        } else {
            productList = repo.findAllByProductIdAndCreatedDateLessThanEqualAndCreatedDateGreaterThanEqualAndDeletedDateIsNull(product_id, endDate, startDate, indexPageWithElements);
        }
        if (Preconditions.checkNotNull(productList)) {
            log.info("The product stock list returned successfully.");
            return productList;
        } else {
            log.warn("The product stock [0] fetched failed. The product stock list are not found.");
            throw new RecordNotFoundException(ECOMMessage.EMPTY_RESULT);
        }
    }

    @Override
    public ProductStocks getById(Long id) throws RecordNotFoundException {
        Optional<ProductStocks> productStock = repo.findById(id);

        if (productStock.isPresent()) {
            log.info("The product stock with Id: [{}] returned successfully.", id);
            return productStock.get();
        } else {
            log.warn("The product stock [0] fetched failed. The provided Id: [{}] is not found.", id);
            throw new RecordNotFoundException("No product stock record exists for given Id: [" + id + "].");
        }
    }

    @Override
    public ProductStocks getMaxByProductId(Long productId) throws RecordNotFoundException {
        Optional<ProductStocks> product = repo.findMaxByProductId(productId);

        if (product.isPresent()) {
            log.info("The product stock with product Id: [{}] returned successfully.", productId);
            return product.get();
        } else {
            log.warn("The product stock [0] fetched failed. The provided product Id: [{}] is not found.", productId);
//            throw new RecordNotFoundException("No product stock record exists for given product Id: [" + productId + "].");
            return null;
        }
    }

    @Override
    public ProductStocks create(ProductStocks productStocks) throws RecordNotFoundException {
        securityContext = new ECOMSecurityContextHolder();
        Optional<User> user = userRepository.findByUsername(securityContext.getUsername());
        if (user.isPresent()) {
            productStocks.setCreatedUser(user.get());
            ProductStocks entity = repo.save(productStocks);

            if (entity != null) {
                log.info("The product stock [{}] created successfully.", entity.getId());
            } else {
                log.info("The product stock creation failed for product [{}].", productStocks.getProduct().getName());
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
    public ProductStocks deleteSoft(Long id) throws RecordNotFoundException {
        return null;
    }
}
