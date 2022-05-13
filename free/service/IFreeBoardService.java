package com.study.free.service;

import java.util.List;

import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

public interface IFreeBoardService {
	/**
	 *  DB에 있는 free_board테이블을 조회하는 메소드다.
	 * @since 2021.12.03
	 * @author 한창희
	 * @param  
	 * @return List<FreeBoardVO> 
	 */
	public List<FreeBoardVO> getBoardList(FreeBoardSearchVO searchVO);
	
	
	/**
	 * free_board를 조회하는데 한 row만 조회
	 * @param int boNo
	 * @return FreeBoardVO
	 * @throws BizNotFoundException
	 */
	public FreeBoardVO getBoard(int boNo) throws BizNotFoundException;
	
	public void increaseHit(int boNo) throws BizNotEffectedException;
	
	public void modifyBoard(FreeBoardVO freeBoard) 
			throws BizNotFoundException,BizPasswordNotMatchedException, BizNotEffectedException ;
	public void removeBoard(FreeBoardVO freeBoard)
			throws BizNotFoundException,BizPasswordNotMatchedException, BizNotEffectedException ;
	public void registBoard(FreeBoardVO freeBoard) throws BizNotEffectedException;

}
