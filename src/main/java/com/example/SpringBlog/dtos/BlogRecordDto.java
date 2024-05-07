package com.example.SpringBlog.dtos;

import jakarta.validation.constraints.NotBlank;

public record BlogRecordDto(@NotBlank String title, @NotBlank String description) {
}