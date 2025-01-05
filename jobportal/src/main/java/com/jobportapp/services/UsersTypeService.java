package com.jobportapp.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobportapp.entity.UsersType;
import com.jobportapp.repository.UsersTypeRepository;

@Service
public class UsersTypeService {

	
	private  UsersTypeRepository usersTypeRepository;

	//constructor injection
	@Autowired
	public UsersTypeService(UsersTypeRepository usersTypeRepository) {
		super();
		this.usersTypeRepository = usersTypeRepository;
	}
	
	public List<UsersType> getAll(){
		return usersTypeRepository.findAll();
	}

	}
