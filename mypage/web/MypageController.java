package com.study.mypage.web;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.login.vo.UserVO;
import com.study.member.service.IMemberService;
import com.study.member.service.MemberServiceImpl;
import com.study.member.vo.MemberVO;

@Controller
public class MypageController {
	
	@Inject
	IMemberService memberService;
	@Inject
	ICommCodeService codeService;
	
	@RequestMapping("/mypage/info.wow")
	public String info(Model model, HttpSession session) {
		UserVO user=(UserVO)session.getAttribute("USER_INFO");
		if(user==null) {
			return "redirect:"+"/login/login.wow";
		}
		try{
			MemberVO member=memberService.getMember(user.getUserId());
			model.addAttribute("member", member);
		}catch (BizNotFoundException enf){
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "회원Notfound", "해당 회원이 없습니다",
					"/", "홈으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "mypage/info";
	}
	@RequestMapping("/mypage/edit.wow")
	public String edit(Model model, HttpSession session) {
		UserVO user=(UserVO)session.getAttribute("USER_INFO");
		if(user==null) {
			return "redirect:" + "/login/login.wow";
		}
		try {
			MemberVO member = memberService.getMember(user.getUserId());
			model.addAttribute("member", member);
		} catch (BizNotFoundException enf) {
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "회원Notfound", "해당 회원이 없습니다",
					"/", "홈으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		List<CodeVO> jobList = codeService.getCodeListByParent("JB00");
		model.addAttribute("jobList", jobList);
		List<CodeVO> hobbyList = codeService.getCodeListByParent("HB00");
		model.addAttribute("hobbyList", hobbyList);

		return "mypage/edit";
	}
	@PostMapping("/mypage/modify.wow")
	public String modify(Model model, HttpSession session, @ModelAttribute("member") MemberVO member) {
		UserVO user=(UserVO)session.getAttribute("USER_INFO");
		if(user==null) {
			return "redirect:" + "/login/login.wow";
		}
		ResultMessageVO resultMessageVO = new ResultMessageVO();
		try {
			memberService.modifyMember(member);
			resultMessageVO.messageSetting(true, "수정", "수정성공", "/", " 홈으로");
			user.setUserName(member.getMemName());
			user.setUserPass(member.getMemPass());
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "회원NotEffected", "업데이트에 실패했습니다", "/", " 홈으로");
		} catch (BizNotFoundException enf) {
			resultMessageVO.messageSetting(false, "회원Notfound", "해당 회원이 없습니다", "/", " 홈으로");
		}
		model.addAttribute("resultMessageVO",resultMessageVO);
		return "common/message";
	}
}
