package com.su.dao;

import java.util.List;

import com.su.models.Moments;

public interface MomentDao {
	
	public List<Moments> findLatestMoment();

	public void addMoment(Moments moment);
}
