package io.chan.springcoresecurity.aopsecurity;

import io.chan.springcoresecurity.domain.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AopSecurityController {

    private final AopMethodService aopMethodService;

    @GetMapping("/preAuthorize")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostAuthorize("#accountDto.username == principal.username")
    public String preAuthorize(
            AccountDto accountDto,
            Model model,
            Principal principal
    ) {
        model.addAttribute("method", "Success @PreAuthorize");
        return "aop/method";
    }

    @GetMapping("/methodSecured")
    public String methodSecured(
            AccountDto accountDto,
            Model model,
            Principal princimpal
    ) {
        aopMethodService.methodSecured();
        model.addAttribute("method", "Success MethodSecured");
        return "aop/method";
    }
}
