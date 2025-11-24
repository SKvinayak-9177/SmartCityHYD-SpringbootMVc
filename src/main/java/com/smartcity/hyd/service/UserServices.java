package com.smartcity.hyd.service;

import com.smartcity.hyd.repo.*;
import com.smartcity.hyd.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

	@Autowired
	private UserRepo repo;
	public UserModel save(UserModel u)
	{
		return repo.save(u);
		
	}
	public UserModel validate(String umail,String upassword)
	{
		UserModel user=repo.findByUmailAndUpassword(umail, upassword);
		return user;
	}
}
