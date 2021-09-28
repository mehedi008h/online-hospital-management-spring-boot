package com.online.hospital.managment.controller;

import com.online.hospital.managment.helper.Message;
import com.online.hospital.managment.model.Blog;
import com.online.hospital.managment.model.LocationState;
import com.online.hospital.managment.model.User;
import com.online.hospital.managment.repository.UserRepository;
import com.online.hospital.managment.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private BlogService blogService;

    @RequestMapping("/index")
    public String home(Model model)
    {
        model.addAttribute("title","Online Hospital Management");
        return "normal/index";
    }
    
 // show blog
    @RequestMapping("/blog/{page}")
    public String blog(@PathVariable("page") Integer page, Model model, @Param("keyword") String keyword)
    {
        model.addAttribute("title","Blog - Online Hospital Management");
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<Blog> blogs = blogService.listAll(keyword, pageable);
        model.addAttribute("blogs",blogs);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", blogs.getTotalPages());
        model.addAttribute("totalComment", blogs.getTotalPages());
        return "normal/blog";
    }

    //signup
    @RequestMapping("/signup")
    public String signup(Model model)
    {
        model.addAttribute("title","Signup");
        model.addAttribute("user",new User());
        return "normal/signup";
    }

    //signup process
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String doRegister(@ModelAttribute("user") User user, Model model,
                             @RequestParam("profileImage") MultipartFile file,Principal principal, HttpSession session)
    {
        try {
            //processing file upload
            if(file.isEmpty())
            {
                user.setImage("");
            }
            else
            {
                user.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            user.setRole("ROLE_USER");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User result = this.userRepository.save(user);
            model.addAttribute("user",new User());
            session.setAttribute("message", new Message("Successfully Registered !!", "alert-success"));
            return "normal/signup";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user",user);
            session.setAttribute("message", new Message("Something went wrong !!" + e.getMessage(), "alert-danger"));
            return "normal/signup";
        }
    }

    //login
    @RequestMapping("/login")
    public String login(Model model)
    {
        model.addAttribute("title","Login");
        return "normal/login";
    }

    //covid -19
    @GetMapping("/corona")
    public String corona(Model model) {
        return "normal/corona";
    }


}
