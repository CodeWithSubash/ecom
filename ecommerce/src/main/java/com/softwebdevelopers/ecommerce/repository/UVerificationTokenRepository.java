package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.user.User;
import com.softwebdevelopers.ecommerce.models.user.UserVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long> {

    @Query(value = "SELECT * FROM user_verification_token WHERE token = ? AND expired = false", nativeQuery = true)
    UserVerificationToken findByToken(String token);

    UserVerificationToken findByUser(User user);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_verification_token SET expired = true"
            + " WHERE id != ?"
            + "	AND user_id = ? AND expired = false", nativeQuery = true)
    void updateExpiryFlag(Long id, Long advisorId);
}
