package com.study.member.web;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.study.code.service.CommCodeServiceImpl;
import com.study.code.service.ICommCodeService;
import com.study.code.vo.CodeVO;
import com.study.common.vo.ResultMessageVO;
import com.study.exception.BizDuplicateKeyException;
import com.study.exception.BizNotEffectedException;
import com.study.exception.BizNotFoundException;
import com.study.member.service.IMemberService;
import com.study.member.service.MemberServiceImpl;
import com.study.member.vo.MemberSearchVO;
import com.study.member.vo.MemberVO;

@Controller
public class MemberController {
	@Inject
	IMemberService memberService;
	
	@Inject
	ICommCodeService codeService;
	// 1. codeService로 얻는 코드는 @ModelAttribute()메소드로 만들어주세요.
	// 2. VO에는 @ModelAttribute()를 붙여준다.
	// 3. @RequestMapping(Post, GetMapping 전부) return은 String
	// 4. 수정, 삭제, 등록은 Post 방식만 허용
	@ModelAttribute("jobList")
	public List<CodeVO> joblist() {
		return codeService.getCodeListByParent("JB00");
	}
	@ModelAttribute("hobbyList")
	public List<CodeVO> hobbylist() {
		return codeService.getCodeListByParent("HB00");
	}
	
	@RequestMapping(value = "/member/memberList.wow")
	public String memberList(Model model, @ModelAttribute("searchVO")MemberSearchVO searchVO) {
		List<MemberVO> memberList=memberService.getMemberList(searchVO);
		model.addAttribute("memberList", memberList);
		return "member/memberList";
	}
	
	@RequestMapping(value = "/member/memberView.wow")
	public String memberView(Model model, @RequestParam(required = true) String memId) {
		try{
			MemberVO member=memberService.getMember(memId);
			model.addAttribute("member", member);
		}catch (BizNotFoundException enf){
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "회원Notfound", "해당 회원이 없습니다",
					"/member/memberList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "member/memberView";
	}
	
	@RequestMapping("/member/memberEdit.wow")
	public String memberEdit(Model model, String memId) {
		try {
			MemberVO member = memberService.getMember(memId);
			model.addAttribute("member", member);
		} catch (BizNotFoundException enf) {
			ResultMessageVO resultMessageVO= new ResultMessageVO();
			resultMessageVO.messageSetting(false, "회원Notfound", "해당 회원이 없습니다",
					"/member/memberList.wow", "목록으로");
			model.addAttribute("resultMessageVO", resultMessageVO);
			return "common/message";
		}
		return "member/memberEdit";
	}
	
	@RequestMapping(value = "/member/memberModify.wow", method = RequestMethod.POST)
	public String memberModify(Model model, @ModelAttribute("member")MemberVO member) {
		ResultMessageVO resultMessageVO = new ResultMessageVO();
		try {
			memberService.modifyMember(member);
			resultMessageVO.messageSetting(true, "수정", "수정성공", "/member/memberList.wow", "목록으로");
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "회원NotEffected", "업데이트에 실패했습니다", "/member/memberList.wow", "목록으로");
		} catch (BizNotFoundException enf) {
			resultMessageVO.messageSetting(false, "회원Notfound", "해당 회원이 없습니다", "/member/memberList.wow", "목록으로");
		}
		model.addAttribute("resultMessageVO",resultMessageVO);
		return "common/message";
	}
	
	@PostMapping("/member/memberForm.wow")
	public String memberForm(Model model) {
		return "member/memberForm";
	}
	
	@PostMapping("/member/memberRegist.wow")
	public String memberRegist(Model model, @ModelAttribute("member")MemberVO member) {
		ResultMessageVO resultMessageVO = new ResultMessageVO();
		try {
			memberService.registMember(member);
			resultMessageVO.messageSetting(true, "등록", "등록성공", "/member/memberList.wow", "목록으로");
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "회원NotEffected", "업데이트에 실패했습니다", "/member/memberList.wow", "목록으로");
		} catch (BizDuplicateKeyException ede) {
			resultMessageVO.messageSetting(false, "회원중복아이디", "insert에 실패했습니다", "/member/memberList.wow", "목록으로");
		}
		model.addAttribute("resultMessageVO",resultMessageVO);
		return "common/message";
	}
	
	@PostMapping("/member/memberDelete.wow")
	public String memberDelete(Model model, @ModelAttribute("member")MemberVO member) {
		ResultMessageVO resultMessageVO = new ResultMessageVO();
		try {
			memberService.removeMember(member);
			resultMessageVO.messageSetting(true, "삭제", "삭제성공", "/member/memberList.wow", "목록으로");
		} catch (BizNotEffectedException ene) {
			resultMessageVO.messageSetting(false, "회원NotEffected", "업데이트에 실패했습니다", "/member/memberList.wow", "목록으로");
		} catch (BizNotFoundException enf) {
			resultMessageVO.messageSetting(false, "회원Notfound", "해당 회원이 없습니다", "/member/memberList.wow", "목록으로");
		}
		model.addAttribute("resultMessageVO",resultMessageVO);
		return "common/message";
	}
}
