package com.zp.sdcc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zp.sdcc.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

}
