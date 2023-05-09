package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String homeLogin(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }
        // 세션관리자에 저장된 회원 정보 조회
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);


        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "login/loginHome";
    }

}