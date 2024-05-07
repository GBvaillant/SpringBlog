package com.example.SpringBlog.repositories;

import com.example.SpringBlog.models.BlogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<BlogModel, UUID> {
}
