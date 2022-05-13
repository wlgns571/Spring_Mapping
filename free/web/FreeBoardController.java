package com.study.free.web;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.exception.BizPasswordNotMatchedException;
import com.study.free.service.FreeBoardServiceImpl;
import com.study.free.service.IFreeBoardService;
import com.study.free.vo.FreeBoardSearchVO;
import com.study.free.vo.FreeBoardVO;

import oracle.jdbc.proxy.annotation.Post;

@Controller
public class FreeBoardController {
	@Inject
	IFreeBoardService freeBoardService;
	@Inject 
	ICommCodeService codeService;
	
	// edit이랑 from, list에는 cateList가 공통으로 model에 담긴다....공통처리
	@ModelAttribute("cateList")
	public List<CodeVO> catelist() {
		return codeService.getCodeListByParent("BC00");
	}
	
	@RequestMapping("/free/freeList.wow")
	public String freeBoardList(Model model, @ModelAttribute("searchVO")FreeBoardSearchVO searchVO) {
		List<FreeBoardVO> freeBoardList = freeBoardService.getBoardList(searchVO);
		model.addAttribute("freeBoardList", freeBoardList);
		return "free/freeList";
	}
	
	@RequestMapping(value="/free/freeView.wow")
	public String freeBoardView(Model model,@RequestParam(required = true, name = "boNo" ) int boNo) {
		try{
			FreeBoardVO freeBoard=freeBoardService.getBoard(boNo);
			model.addAttribute("freeBoard", freeBoard);
			freeBoardService.increaseHit(boNo);
		}catch (BizNotFoundException enf){
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
					"/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}catch (BizNotEffectedException ene){
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글NotEffected", "업데이트에 실패했습니다",
					"/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "free/freeView";
	}
	
//	@RequestMapping("/free/freeEdit.wow")
//	public ModelAndView freeBoardEdit(int boNo) {
//		ModelAndView mav = new ModelAndView();
//		try{
//			FreeBoardVO freeBoard=freeBoardService.getBoard(boNo);
//			mav.addObject("freeBoard", freeBoard);
//		}catch (BizNotFoundException enf){
//			ResultMessageVO resultMessageVO= new ResultMessageVO();
//			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
//					"/free/freeList.wow", "목록으로");
//			mav.addObject("resultMessageVO", resultMessageVO);
//			mav.setViewName("common/message");
//		}
//		List<CodeVO> cateList=codeService.getCodeListByParent("BC00");
//		mav.addObject("cateList", cateList);
//		mav.setViewName("free/freeEdit");
//		
//		return mav;
//	}
	@RequestMapping("/free/freeEdit.wow")
	public String freeBoardEdit(Model model, int boNo) {
		try{
			FreeBoardVO freeBoard=freeBoardService.getBoard(boNo);
			model.addAttribute("freeBoard", freeBoard);
		}catch (BizNotFoundException enf){
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
					"/free/freeList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "free/freeEdit";
	}
	
	@RequestMapping(value = "/free/freeModify.wow", method = RequestMethod.POST)
	public String freeBoardModify(Model model, @ModelAttribute("freeBoard")FreeBoardVO freeBoard) {
		ResultMessageVO resultMessageVO=new ResultMessageVO();
		try {
			freeBoardService.modifyBoard(freeBoard);
			resultMessageVO.messageSetting(true, "수정", "수정성공"
				,"/free/freeList.wow" , "목록으로");
		} catch (BizNotFoundException enf) {
			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
					"/free/freeList.wow", "목록으로");
		} catch (BizPasswordNotMatchedException epm) {
			resultMessageVO.messageSetting(false, "비밀번호틀림", "글쓸때의 비밀번호랑 다릅니다",
					"/free/freeList.wow", "목록으로");
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "글NotEffected", "업데이트에 실패했습니다",
					"/free/freeList.wow", "목록으로");
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "common/message";
	}
	
	@PostMapping("/free/freeRegist.wow")
	public String freeBoardRegist(Model model, @ModelAttribute("freeBoard")FreeBoardVO freeBoard) {
		ResultMessageVO resultMessageVO=new ResultMessageVO();
		try {
			freeBoardService.registBoard(freeBoard);
			resultMessageVO.messageSetting(true, "등록", "등록성공", "free/freeList.wow"
					 ,"목록으로");
		} catch (BizNotEffectedException ebe) {
			resultMessageVO.messageSetting(false, "실패", "업데이트실패", "free/freeList.wow"
					 ,"목록으로");
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "common/message";
	}
	
	@RequestMapping("/free/freeForm.wow")
	public String freeBoardForm(Model model) {
		return "free/freeForm";
	}

	@PostMapping("/free/freeDelete.wow")
	public String freeBoardDelete(Model model, @ModelAttribute("freeBoard")FreeBoardVO freeBoard) {
		ResultMessageVO resultMessageVO=new ResultMessageVO();
		try {
			freeBoardService.removeBoard(freeBoard);
			resultMessageVO.messageSetting(true, "삭제", "삭제성공"
					,"/free/freeList.wow" , "목록으로");
		}catch (BizNotFoundException enf) {
			resultMessageVO.messageSetting(false, "글Notfound", "해당 글이 없습니다",
					"/free/freeList.wow", "목록으로");
		} catch (BizPasswordNotMatchedException epm) {
			resultMessageVO.messageSetting(false, "비밀번호틀림", "글쓸때의 비밀번호랑 다릅니다",
					"/free/freeList.wow", "목록으로");
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "글NotEffected", "업데이트에 실패했습니다",
					"/free/freeList.wow", "목록으로");
		}
		model.addAttribute("resultMessageVO", resultMessageVO);
		return "common/message";
	}
}
