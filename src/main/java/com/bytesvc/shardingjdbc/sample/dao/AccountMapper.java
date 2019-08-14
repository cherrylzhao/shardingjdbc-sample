package com.bytesvc.shardingjdbc.sample.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {

	@Insert("INSERT INTO t_order (user_id, status) VALUES (#{userId}, #{status})")
	public int createAccount(@Param("userId") long userId, @Param("status") String status);

	@Delete("DELETE FROM t_order where user_id = #{userId}")
	public void deleteAccount(@Param("userId") long orderId);

	@org.apache.ibatis.annotations.Select("select user_id from t_order")
	public List<Object> findAllAcount();

}
