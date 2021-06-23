package com.online.hospital.managment.controller;

import com.online.hospital.managment.helper.Message;
import com.online.hospital.managment.model.LocationState;
import com.online.hospital.managment.model.User;
import com.online.hospital.managment.repository.UserRepository;
import com.online.hospital.managment.service.CoronaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
    private CoronaDataService coronaDataService;

    @RequestMapping("/")
    public String home(Model model)
    {
        model.addAttribute("title","Online Hospital Management");
        return "normal/index";
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
        List<LocationState> allStats = coronaDataService.getLocationStates();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        return "normal/corona";
    }


}
