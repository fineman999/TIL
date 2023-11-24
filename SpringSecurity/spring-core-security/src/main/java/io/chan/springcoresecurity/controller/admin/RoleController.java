package io.chan.springcoresecurity.controller.admin;

import io.chan.springcoresecurity.domain.dto.RoleDto;
import io.chan.springcoresecurity.domain.entity.Role;
import io.chan.springcoresecurity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@GetMapping(value="/admin/roles")
	public String getRoles(Model model) {

		List<Role> roles = roleService.getRoles();
		model.addAttribute("roles", roles);

		return "admin/role/list";
	}

	@GetMapping(value="/admin/roles/register")
	public String viewRoles(Model model) {

		RoleDto role = new RoleDto();
		model.addAttribute("role", role);

		return "admin/role/detail";
	}

	@PostMapping(value="/admin/roles")
	public String createRole(RoleDto roleDto) {

		Role role = Role.from(roleDto);
		roleService.createRole(role);

		return "redirect:/admin/roles";
	}

	@GetMapping(value="/admin/roles/{id}")
	public String getRole(@PathVariable String id, Model model) throws Exception {

		Role role = roleService.getRole(Long.parseLong(id));
		RoleDto roleDto = RoleDto.from(role);
		model.addAttribute("role", roleDto);

		return "admin/role/detail";
	}

	@GetMapping(value="/admin/roles/delete/{id}")
	public String removeResources(@PathVariable String id, Model model) throws Exception {

		Role role = roleService.getRole(Long.parseLong(id));
		roleService.deleteRole(Long.parseLong(id));

		return "redirect:/admin/resources";
	}
}
