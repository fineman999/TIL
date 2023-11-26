package io.chan.springcoresecurity.controller.user;


import io.chan.springcoresecurity.domain.dto.AccountDto;
import io.chan.springcoresecurity.domain.entity.Account;
import io.chan.springcoresecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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


	@GetMapping("/order")
	public String order(){
		userService.order();
		return "user/mypage";
	}
}
