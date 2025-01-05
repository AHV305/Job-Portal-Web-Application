package com.jobportapp.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jobportapp.entity.Users;
import com.jobportapp.entity.UsersType;
import com.jobportapp.services.UsersService;
import com.jobportapp.services.UsersTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UsersController {

	public final UsersTypeService usersTypeService;
	private final UsersService usersService;

	// constructor injection
	@Autowired
	public UsersController(UsersTypeService usersTypeService, UsersService usersService) {
		super();
		this.usersTypeService = usersTypeService;
		this.usersService = usersService;
	}

	@GetMapping("/register")
	public String register(Model model) {
		List<UsersType> usersTypes = usersTypeService.getAll();
		model.addAttribute("getAllTypes", usersTypes);
		model.addAttribute("user", new Users());
		return "register";
	}

	@PostMapping("/register/new")
	public String userRegistration(@Valid Users users, Model model) {

		Optional<Users> optionalUsers = usersService.getUserByEmail(users.getEmail());

		if (optionalUsers.isPresent()) {
			model.addAttribute("error", "Email already registered, try to register or login with another email");
			List<UsersType> usersTypes = usersTypeService.getAll();
			model.addAttribute("getAllTypes", usersTypes);
			model.addAttribute("user", new Users());
			return "register";
		}
		usersService.addNew(users);
		return "redirect:/dashboard/";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/logut")
	public String logut(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null) {
			new SecurityContextLogoutHandler().logout(request, response,authentication);
		}
		return "redirect:/";
	}
}
