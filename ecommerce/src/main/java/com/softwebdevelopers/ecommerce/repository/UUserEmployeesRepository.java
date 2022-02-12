package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.dto.UUserEmployeeRecordDto;
import com.softwebdevelopers.ecommerce.models.user.UserEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UUserEmployeesRepository extends JpaRepository<UserEmployee, Long> {

    @Procedure(name = "UserEmployee.getEmployeeRecord")
    Object getEmployeeRecord(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM user_employees WHERE username LIKE %?1% OR firstname LIKE %?1% OR lastname LIKE %?1% OR email LIKE %?1%" ,
            countQuery = "SELECT count(*) FROM user_employees",
            nativeQuery = true)
    Page<UserEmployee> findByKeywordWithPagination(String keyword, Pageable pageable);

}
