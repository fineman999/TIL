package io.chan.springcoresecurity.controller.user;


import io.chan.springcoresecurity.domain.Account;
import io.chan.springcoresecurity.domain.AccountDto;
import io.chan.springcoresecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@GetMapping(value="/mypage")
	public String myPage() throws Exception {
		return "user/mypage";
	}

	@GetMapping("/users")
	public String createUser(){
		return "user/login/register";
	}

	@PostMapping("/users")
	public String createUser(AccountDto accountDto){

		Account account = Account.from(accountDto);
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		userService.createUser(account);


		return "redirect:/";
	}
}
