package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.cart.Cart;
import com.softwebdevelopers.ecommerce.models.cart.CartDetail;
import com.softwebdevelopers.ecommerce.models.category.Category;

import java.util.List;

public interface IPCartService {

    List<Cart> getAll() throws RecordNotFoundException;

    Cart getAllByUserId() throws RecordNotFoundException;

    Cart getAllByCreatedUserId(Long id) throws RecordNotFoundException;

    CartDetail create(CartDetail cart, Long userId)  throws RecordNotFoundException;

    CartDetail update(CartDetail cart)  throws RecordNotFoundException;

    CartDetail delete(Long id) throws RecordNotFoundException;

    Cart deleteSoft(Long id) throws RecordNotFoundException;
}
