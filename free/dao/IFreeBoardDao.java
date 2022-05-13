package com.study.free.dao;

import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

@Mapper
public interface IFreeBoardDao {

	public int getTotalRowCount(FreeBoardSearchVO searchVO);
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO) ;	
  public FreeBoardVO getBoard( int boNo);  
  public int increaseHit(int boNo); 
  public int updateBoard( FreeBoardVO board);
  public int deleteBoard( FreeBoardVO board);
  public int insertBoard(FreeBoardVO board);

  
}
