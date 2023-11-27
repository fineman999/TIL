package io.chan.springcoresecurity.controller.admin;


import io.chan.springcoresecurity.domain.dto.ResourcesDto;
import io.chan.springcoresecurity.domain.entity.Resources;
import io.chan.springcoresecurity.domain.entity.Role;
import io.chan.springcoresecurity.repository.RoleRepository;
import io.chan.springcoresecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import io.chan.springcoresecurity.security.service.MethodSecurityService;
import io.chan.springcoresecurity.service.ResourcesService;
import io.chan.springcoresecurity.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ResourcesController {

	private final ResourcesService resourcesService;
	private final MethodSecurityService methodSecurityService;
	private final RoleRepository roleRepository;

	private final RoleService roleService;
	private final UrlFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

	@GetMapping(value="/admin/resources")
	public String getResources(Model model) {

		List<Resources> resources = resourcesService.getResources();
		model.addAttribute("resources", resources);

		return "admin/resource/list";
	}

	@PostMapping(value="/admin/resources")
	public String createResources(ResourcesDto resourcesDto) throws Exception {

		Role role = roleRepository.findByRoleName(resourcesDto.getRoleName());
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		Resources resources = Resources.from(resourcesDto);
		resources.setRoleSet(roles);

		resourcesService.createResources(resources);
		if (resourcesDto.getResourceType().equals("url")) {
			urlFilterInvocationSecurityMetadataSource.reload();
		} else {
			methodSecurityService.addMethodSecured(resourcesDto.getResourceName(), resourcesDto.getRoleName());
		}

		return "redirect:/admin/resources";
	}

	@GetMapping(value="/admin/resources/register")
	public String viewRoles(Model model) {

		List<Role> roleList = roleService.getRoles();
		model.addAttribute("roleList", roleList);

		ResourcesDto resources = new ResourcesDto();
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(new Role());
		resources.setRoleSet(roleSet);
		model.addAttribute("resources", resources);

		return "admin/resource/detail";
	}

	@GetMapping(value="/admin/resources/{id}")
	public String getResources(@PathVariable String id, Model model) {

		List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);
		Resources resources = resourcesService.getResources(Long.parseLong(id));

		ResourcesDto resourcesDto = ResourcesDto.from(resources);
		model.addAttribute("resources", resourcesDto);

		return "admin/resource/detail";
	}

	@GetMapping(value="/admin/resources/delete/{id}")
	public String removeResources(@PathVariable String id, Model model) throws Exception {

		Resources resources = resourcesService.getResources(Long.parseLong(id));
		resourcesService.deleteResources(Long.parseLong(id));
		if (resources.getResourceType().equals("url")) {
			urlFilterInvocationSecurityMetadataSource.reload();
		} else {
			methodSecurityService.removeMethodSecured(resources.getResourceName());
		}

		return "redirect:/admin/resources";
	}
}
