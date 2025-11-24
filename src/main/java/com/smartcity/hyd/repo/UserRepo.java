package com.smartcity.hyd.repo;

import com.smartcity.hyd.model.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserModel,Integer>{
	
	UserModel findByUmailAndUpassword(String umail,String upassword);

}
