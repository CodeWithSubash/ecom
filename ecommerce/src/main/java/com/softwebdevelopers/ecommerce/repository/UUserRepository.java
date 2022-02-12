package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.brand.Brand;
import com.softwebdevelopers.ecommerce.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query(value = "SELECT * FROM users WHERE id = ?", nativeQuery = true)
    Optional<User> findById(Long id);

    @Query(value = "SELECT * FROM users WHERE user_type = ?1 AND (name LIKE %?2% OR username LIKE %?2% OR email LIKE %?2%)",
            countQuery = "SELECT count(*) FROM users WHERE user_type = ?1",
            nativeQuery = true)
    Page<User> findByKeywordWithPagination(String userType, String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE user_type = ?1",
            countQuery = "SELECT count(*) FROM users WHERE user_type = ?1",
            nativeQuery = true)
    Page<User> findAllByType(String userType, Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE (owner_user_id IS NULL OR owner_user_id = ?1) AND user_type = ?2 AND (name LIKE %?3% OR username LIKE %?3% OR email LIKE %?3%)",
            countQuery = "SELECT count(*) FROM users WHERE owner_user_id = ?1 AND user_type = ?2",
            nativeQuery = true)
    Page<User> findByOwnerAndKeywordWithPagination(Long userId, String userType, String keyword, Pageable pageable);

//    @Query(value = "SELECT * FROM users WHERE owner_user_id = ?1 AND user_type = ?2",
//            countQuery = "SELECT count(*) FROM users WHERE owner_user_id = ?1 AND user_type = ?2",
//            nativeQuery = true)
//    Page<User> findAllByOwnerAndType(Long userId, String userType, Pageable pageable);

    @Query(value = "SELECT * FROM users WHERE (owner_user_id IS NULL OR owner_user_id = ?1) AND user_type = ?2",
            countQuery = "SELECT count(*) FROM users WHERE owner_user_id = ?1 AND user_type = ?2",
            nativeQuery = true)
    Page<User> findAllByOwnerAndType(Long userId, String userType, Pageable pageable);

//    @Query(value = "SELECT * FROM users WHERE user_type = ?1 AND enabled = 1 AND deleted_date IS NULL ", nativeQuery = true)
//    List<User> findAllByType(String userType);

    @Query(value = "SELECT * FROM users WHERE (owner_user_id IS NULL OR owner_user_id = ?1) AND user_type = ?2 AND enabled = 1 AND deleted_date IS NULL ", nativeQuery = true)
    List<User> findAllByOwnerIdOrIsNullAndType( Long userId, String userType);
}
