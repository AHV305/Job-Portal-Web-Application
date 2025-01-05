package com.jobportapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobportapp.entity.UsersType;

@Repository
public interface UsersTypeRepository extends JpaRepository<UsersType, Integer> {

}
