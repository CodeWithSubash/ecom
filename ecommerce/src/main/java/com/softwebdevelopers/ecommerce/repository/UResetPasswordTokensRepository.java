package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.user.UResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UResetPasswordTokensRepository extends JpaRepository<UResetPasswordToken, Long> {

    @Query(value = "SELECT * FROM user_reset_password_token WHERE token = ? AND expired = false", nativeQuery = true)
    UResetPasswordToken findByToken(String token);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_reset_password_token SET expired = true"
            + " WHERE id != ?"
            + "	AND advisor_id = ? AND expired = false", nativeQuery = true)
    void updateExpiryFlag(Long id, Long advisorId);
}
