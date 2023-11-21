package io.chan.springcoresecurity.controller.user;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MessageController {
	
	@GetMapping(value="/messages")
	public String mypage() {

		return "user/messages";
	}
}
