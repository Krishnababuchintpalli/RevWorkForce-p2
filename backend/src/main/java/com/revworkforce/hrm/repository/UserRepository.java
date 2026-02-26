package com.revworkforce.hrm.repository;

import com.revworkforce.hrm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("select u from User u where str(u.id) = :employeeId")
    Optional<User> findByEmployeeId(@Param("employeeId") String employeeId);

    @Query("select u from User u where u.email = :email or str(u.id) = :employeeId")
    Optional<User> findByEmailOrEmployeeId(@Param("email") String email, @Param("employeeId") String employeeId);

    @Query("select u from User u where lower(u.fullName) like lower(concat('%',:query,'%')) or str(u.id) like concat('%',:query,'%')")
    List<User> searchByNameOrEmployeeId(@Param("query") String query);

    List<User> findByManagerId(Long managerId);
}
