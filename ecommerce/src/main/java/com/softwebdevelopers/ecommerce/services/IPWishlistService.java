package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.cart.Cart;
import com.softwebdevelopers.ecommerce.models.cart.CartDetail;
import com.softwebdevelopers.ecommerce.models.cart.Wishlist;

import java.util.List;

public interface IPWishlistService {

    List<Wishlist> getAll() throws RecordNotFoundException;

    List<Wishlist> getAllByUserId() throws RecordNotFoundException;

    Wishlist create(Long productId)  throws RecordNotFoundException;

    Wishlist deleteSoft(Long id) throws RecordNotFoundException;

    Wishlist deleteSoftByProductId(Long productId) throws RecordNotFoundException;
}
