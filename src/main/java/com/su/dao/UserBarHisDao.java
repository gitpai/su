package com.su.dao;

import com.su.models.UserBarHistory;

public interface UserBarHisDao {

		public void addBarHis(UserBarHistory userBrHistory);
		public UserBarHistory findLatestHis(String userName);
		
}
