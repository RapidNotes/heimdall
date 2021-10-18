package com.rapidnotes.heimdall.dao;

import com.rapidnotes.heimdall.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, String> {
}
