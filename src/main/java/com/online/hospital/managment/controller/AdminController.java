package com.online.hospital.managment.controller;

import com.online.hospital.managment.helper.Message;
import com.online.hospital.managment.model.*;
import com.online.hospital.managment.repository.*;
import com.online.hospital.managment.service.AmbulanceService;
import com.online.hospital.managment.service.BlogService;
import com.online.hospital.managment.service.HospitalService;
import com.online.hospital.managment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private AmbulanceService ambulanceService;

    @Autowired
    private AmbulanceRepository ambulanceRepository;

    @Autowired
    private BloodPostRepository bloodPostRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal)
    {
        String userName = principal.getName();
        System.out.println("USERNAME "+userName);
        User user = userRepository.getUserByUserName(userName);
        System.out.println("USER "+user);
        model.addAttribute("user", user);
    }

    @RequestMapping("/index")
    public String home(Model model)
    {
        model.addAttribute("title","Admin - Online Hospital Management");
        List<Blog> blogs = this.blogRepository.findAll();
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(0,10,sort);
        Page<BloodPost> bloodPosts = this.bloodPostRepository.findAll(pageable);
        List<Hospital> hospitals = this.hospitalRepository.findAll();
        List<Ambulance> ambulances = this.ambulanceRepository.findAll();
        Page<User> users = this.userRepository.findAll(pageable);
        int totalBlog = blogs.size();
        int totalHospital = hospitals.size();
        int ambulance = ambulances.size();
        model.addAttribute("totalBlog", totalBlog);
        model.addAttribute("bPost", bloodPosts);
        model.addAttribute("totalBPost", bloodPosts.getTotalElements());
        model.addAttribute("totalHospital", totalHospital);
        model.addAttribute("ambulance", ambulance);
        model.addAttribute("users", users);
        return "admin/index";
    }

    // Start User work
    //user
    @RequestMapping("/user/{page}")
    public String user(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title","User - Online Hospital Management");
        Pageable pageable = PageRequest.of(page, 5);
        Page<User> users = this.userService.listAll(keyword,pageable);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", users.getTotalPages());
        return "admin/user";
    }

    // delete user
    @RequestMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") Integer id)
    {
        this.userRepository.deleteById(id);
        return "redirect:/admin/user/0";
    }
    // End User work

    // Start blog work
    //blog
    @RequestMapping("/blog/{page}")
    public String blog(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title","Blog - Online Hospital Management");
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page,5, sort);
        Page<Blog> blogs = this.blogService.listAll(keyword, pageable);
        model.addAttribute("blogs", blogs);
        model.addAttribute("keyword",keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", blogs.getTotalPages());
        return "admin/blog";
    }

    //process blog
    @RequestMapping(value = "/process-blog",method = RequestMethod.POST)
    public String addBlog(@ModelAttribute("blog") Blog blog,HttpSession session)
    {
        try {
            blogRepository.save(blog);
            session.setAttribute("message", new Message("Successfully Added !!", "alert-success"));
        } catch (Exception e)
        {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong !!" + e.getMessage(), "alert-danger"));
        }
        return "redirect:/admin/blog/0";
    }

    // edit blog
    @GetMapping("/update-blog/{id}")
    public String updateBlog(@PathVariable(name = "id") Integer id,Model model)
    {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("blog",blog);
        return "/admin/edit_blog";
    }

    //process edit blog
    @PostMapping("/update/{id}")
    public String processUpdateBlog(@PathVariable("id") Integer id,@ModelAttribute("blog") Blog blog)
    {
        blogRepository.save(blog);
        return "redirect:/admin/update-blog/"+blog.getId();
    }

    // delete blog
    @RequestMapping("/delete-blog/{id}")
    public String deleteBlog(@PathVariable(name = "id") Integer id)
    {
        blogRepository.deleteById(id);
        return "redirect:/admin/blog/0";
    }
    // End blog work

    // Start Hospital work
    // hospital
    @RequestMapping("/hospital/{page}")
    public String hospital(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title", "Hospital - Online Hospital Management");
        Pageable pageable = PageRequest.of(page, 10);
        Page<Hospital> hospitals = hospitalService.listAll(keyword, pageable);
        model.addAttribute("hospitals", hospitals);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", hospitals.getTotalPages());
        return "admin/hospital";
    }

    //process hospital
    @RequestMapping(value = "/process-hospital", method = RequestMethod.POST)
    public String processHospital(@ModelAttribute("hospital") Hospital hospital, HttpSession session)
    {
        try {
            hospitalRepository.save(hospital);
            session.setAttribute("message", new Message("Successfully Added !!", "alert-success"));
        } catch (Exception e)
        {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong !!" + e.getMessage(), "alert-danger"));
        }
        return "redirect:/admin/hospital/0";
    }

    // edit hospital
    @GetMapping("/edit-hospital/{id}")
    public String updateHospital(@PathVariable(name = "id") Integer id,Model model)
    {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("hospital",hospital);
        return "/admin/edit_hospital";
    }

    //process edit hospital
    @PostMapping("/update-hospital/{id}")
    public String processUpdateHospital(@PathVariable("id") Integer id,@ModelAttribute("hospital") Hospital hospital)
    {
        hospitalRepository.save(hospital);
        return "redirect:/admin/edit-hospital/"+hospital.getId();
    }

    // delete hospital
    @RequestMapping("/delete-hospital/{id}")
    public String deleteHospital(@PathVariable(name = "id") Integer id)
    {
        hospitalRepository.deleteById(id);
        return "redirect:/admin/hospital/0";
    }
    // End Hospital work

    //ambulance
    @RequestMapping("/ambulance/{page}")
    public String ambulance(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title","Ambulance- Online Hospital Management");
        Pageable pageable = PageRequest.of(page, 10);
        Page<Ambulance> ambulances = ambulanceService.listAll(keyword, pageable);
        model.addAttribute("ambulances", ambulances);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", ambulances.getTotalPages());
        return "/admin/ambulance";
    }

    //process ambulance
    @RequestMapping(value = "/process-ambulance", method = RequestMethod.POST)
    public String processAdmin(@ModelAttribute("ambulance") Ambulance ambulance, HttpSession session)
    {
        try {
            ambulanceRepository.save(ambulance);
            session.setAttribute("message", new Message("Successfully Added !!", "alert-success"));
        } catch (Exception e)
        {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something went wrong !!" + e.getMessage(), "alert-danger"));
        }
        return "redirect:/admin/ambulance/0";
    }

    // edit ambulance
    @GetMapping("/edit-ambulance/{id}")
    public String updateAmbulance(@PathVariable(name = "id") Integer id,Model model)
    {
        Ambulance ambulance = ambulanceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("ambulance",ambulance);
        return "/admin/edit_ambulance";
    }

    //process edit hospital
    @PostMapping("/update-ambulance/{id}")
    public String processUpdateAmbulance(@PathVariable("id") Integer id,@ModelAttribute("ambulance") Ambulance ambulance)
    {
        ambulanceRepository.save(ambulance);
        return "redirect:/admin/edit-ambulance/"+ambulance.getId();
    }

    // delete ambulance
    @RequestMapping("/delete-ambulance/{id}")
    public String deleteAmbulance(@PathVariable(name = "id") Integer id)
    {
        ambulanceRepository.deleteById(id);
        return "redirect:/admin/ambulance/0";
    }
}
