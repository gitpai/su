package com.su.dao;

import java.util.List;

import com.su.models.Comment;
import com.su.models.Moments;

public interface MomentDao {
	
	public List<Moments> findLatestMoment();
	public List<Comment> findComments(String uuid);
	public void addMoment(Moments moment);
	public void addComment(Comment comment);
}
