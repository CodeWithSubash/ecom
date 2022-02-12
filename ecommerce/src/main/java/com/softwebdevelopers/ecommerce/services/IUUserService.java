package com.softwebdevelopers.ecommerce.services;

import com.softwebdevelopers.ecommerce.dto.PagingSortingAndSearchDto;
import com.softwebdevelopers.ecommerce.models.user.UserVerificationToken;
import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.exceptions.RecordNotFoundException;
import com.softwebdevelopers.ecommerce.models.user.UResetPasswordToken;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface IUUserService {

    User registerUserAccount(User user, Collection<String> strRoles);

    User updateRegisteredUser(User user);

    UserVerificationToken createVerificationToken(UserVerificationToken verificationToken);

    UserVerificationToken findByVerificationToken(String token);

    void updateVerificationTokenEnableFlag(Long id, Long userId);

    User changePassword(User user);

    UResetPasswordToken createPasswordToken(UResetPasswordToken verificationToken);

    UResetPasswordToken findByPasswordToken(String token);

    void updatePasswordEnableFlag(Long id, Long userId);

    User getUserByEmail(String email);

    User getUser() throws RecordNotFoundException;

    Page<User> getAll(PagingSortingAndSearchDto page, String userType) throws RecordNotFoundException;

//    List<User> getAllByType(String type) throws RecordNotFoundException;

    List<User> findAllByUserIdAndType(String type) throws RecordNotFoundException;

    User getById(Long id) throws RecordNotFoundException;

    User create(User user, String role)  throws RecordNotFoundException;

    User update(User user, String role)  throws RecordNotFoundException;

    User assignEmployeeRetailer(Long id, List<Long> retailerIds) throws RecordNotFoundException;

    User deleteSoft(Long id) throws RecordNotFoundException;

    User delete(Long id) throws RecordNotFoundException;
}
