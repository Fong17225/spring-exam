// src/main/java/com/example/repository/UserRepository.java
package com.example.spring.exam.Repository;


import com.example.spring.exam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name); // Kiểm tra tên đã tồn tại chưa
    List<User> findByNameContainingIgnoreCase(String name); // Tìm kiếm gần đúng, không phân biệt hoa thường
}