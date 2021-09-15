package com.online.hospital.managment.controller;

import com.online.hospital.managment.helper.Message;
import com.online.hospital.managment.model.*;
import com.online.hospital.managment.model.comment.BlogComment;
import com.online.hospital.managment.repository.BlogRepository;
import com.online.hospital.managment.repository.BloodPostRepository;
import com.online.hospital.managment.repository.DonerRepository;
import com.online.hospital.managment.repository.HospitalRepository;
import com.online.hospital.managment.repository.UserRepository;
import com.online.hospital.managment.repository.comment.BlogCommentRepository;
import com.online.hospital.managment.service.*;

import org.dom4j.rule.Mode;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BloodPostRepository bloodPostRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BloodPostService bloodPostService;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private UserService userService;

    @Autowired
    private AmbulanceService ambulanceService;

    @Autowired
    private HospitalRepository hospitalRepository;
    
    @Autowired
    private BlogCommentRepository blogCommentRepository;
    
    @Autowired
    private DonerRepository donerRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal)
    {
        String userName = principal.getName();
        System.out.println("USERNAME "+userName);
        User user = userRepository.getUserByUserName(userName);
        System.out.println("USER "+user);
        model.addAttribute("user", user);
    }

    //user dashbord
    @RequestMapping("/index")
    public String home(Model model)
    {
        model.addAttribute("title","Home - Online Hospital Management");
        return "user/index";
    }

    //user profile
    @RequestMapping("/profile")
    public String profile(Model model,Principal principal)
    {
        model.addAttribute("title","Profile");
        return "user/profile-details";
    }

    //update user
    @RequestMapping("/update-profile")
    public String updateUser(Model model)
    {
        model.addAttribute("title","Edit Profile - Online Hospital Management");
        return "user/edit_profile";
    }

    //update handler
    @RequestMapping(value = "/process-update", method = RequestMethod.POST)
    public String updateHandler(@ModelAttribute User user, @RequestParam("profileImage") MultipartFile file,
                                Model model,HttpSession session, Principal principal)
    {
        try {
            User olduser = this.userRepository.findById(user.getId()).get();
            if(!file.isEmpty())
            {
                //delete old photo
                File deletefile = new ClassPathResource("static/image").getFile();
                File oldfile = new File(deletefile,olduser.getImage());
                oldfile.delete();
                //update photo
                File saveFile = new ClassPathResource("static/image").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                user.setImage(file.getOriginalFilename());
            }
            else
            {
                user.setImage(olduser.getImage());
            }
            this.userRepository.save(user);
            session.setAttribute("message", new Message("Your Account is updated..", "success"));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return "redirect:/user/update-profile";
    }
    
    // blood donate request
    @GetMapping("/blood-donate-request")
    public String bloodDonateRequest(Model model, Principal principal) {
    	model.addAttribute("title", "Blood Donate Request - Online Hospital management");
    	String username = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(username);
        List<Doner> doners = currentUser.getDoners();
        model.addAttribute("doners", doners);
        return "user/blood_donate_request";
    }

    //change password
    @GetMapping("/password")
    public String changePassword(Model model)
    {
        model.addAttribute("title", "Change Password - Online Hospital management");
        return "user/changepassword";
    }

    // change password handler
    @PostMapping("/change-password")
    public String changePasswordHandler(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword, Principal principal,HttpSession session)
    {
        String username = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(username);
        if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
        {
            currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(currentUser);
            session.setAttribute("message", new Message("Your password is changed", "success"));
        }
        else
        {
            session.setAttribute("message", new Message("Wrong!!Old Password", "danger"));
            return "redirect:/user/password";
        }
        return "redirect:/user/index";
    }

    //blood post
    @RequestMapping("/blood/{page}")
    public String blood(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title","Blood - Online Hospital Management");
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<BloodPost> posts = bloodPostService.listAll(keyword, pageable);
        model.addAttribute("posts",posts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", posts.getTotalPages());
        return "user/blood";
    }

    //processing blood post
    @RequestMapping(value = "/process-post", method = RequestMethod.POST)
    public String bloodPost(@ModelAttribute BloodPost bloodPost, Principal principal, HttpSession httpSession)
    {
        try {
            String username = principal.getName();
            User user = userRepository.getUserByUserName(username);
            bloodPost.setTime(Instant.now());
            bloodPost.setUser(user);
            user.getBloodPosts().add(bloodPost);
            this.userRepository.save(user);
            httpSession.setAttribute("message", new Message("Your post is added!!", "success"));
        }catch (Exception e)
        {
            e.printStackTrace();
            httpSession.setAttribute("message", new Message("Something went wrong!! Try again..", "danger"));
        }
        return "redirect:/user/blood/0";
    }

    // show blood post details
    @RequestMapping("/blood-post-details/{id}")
    public String bloodPostDetails(@PathVariable("id") Integer id, Model model)
    {
        BloodPost bloodPost = bloodPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("bloodPost",bloodPost);
        return "/user/bloodpost_details";
    }

    // Edit blood post
    @GetMapping("/update-post/{id}")
    public String updatePost(@PathVariable(name = "id") Integer id,Model model)
    {
        BloodPost bloodPost = bloodPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("bloodPost",bloodPost);
        return "/user/edit_bloodpost";
    }

    //process edit blood post
    @PostMapping("/update/{id}")
    public String processUpdatePost(@PathVariable("id") Integer id,@ModelAttribute("post") BloodPost post,Principal principal,HttpSession session)
    {
        User user = this.userRepository.getUserByUserName(principal.getName());
        post.setUser(user);
        bloodPostRepository.save(post);
        session.setAttribute("message", new Message("Your post is updated..", "success"));
        return "redirect:/user/update-post/"+post.getId();
    }
    
  //process edit blog post
    @PostMapping("/process-doner/{id}")
    public String processDonateBlood(@PathVariable("id") Integer id,@ModelAttribute("doner") Doner doner,Principal principal,HttpSession session)
    {
        User user = this.userRepository.getUserByUserName(principal.getName());
        doner.setUser(user);
        BloodPost bloodPost = this.bloodPostRepository.getBloodPostByBloodPostId(id);
        doner.setBloodPost(bloodPost);
        doner.setApply(true);
        donerRepository.save(doner);
        session.setAttribute("message", new Message("Your comment is added", "success"));
        return "redirect:/user/blood-post-details/"+doner.getId();
    }

    // Delete blood post
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Integer id,Model model,HttpSession session,Principal principal)
    {
        BloodPost bloodPost = this.bloodPostRepository.findById(id).get();
        bloodPost.setUser(null);
        User user = this.userRepository.getUserByUserName(principal.getName());
        user.getBloodPosts().remove(bloodPost);
        this.userRepository.save(user);
        session.setAttribute("message", new Message("Post deleted successfully..", "success"));
        return "redirect:/user/profile";
    }
    
  //my blood post
    @RequestMapping("/my-blood-post/{page}")
    public String myBloodPost(@PathVariable("page") Integer page, Model model,Principal principal)
    {
        String username = principal.getName();
        User user = this.userRepository.getUserByUserName(username);
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page,5,sort);
        Page<BloodPost> posts = this.bloodPostRepository.findBloodPostByUser(user.getId(),pageable);
        model.addAttribute("title","My Blood Post || Online Hospital Management");
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", posts.getTotalPages());
        return "user/my-blood-post";
    }


    //user
    @RequestMapping("/member/{page}")
    public String user(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title","User - Online Hospital Management");
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<User> users = userService.listAll(keyword, pageable);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", users.getTotalPages());
        return "user/user";
    }

    // show member details
    @RequestMapping("/member-details/{id}")
    public String memberDetails(@PathVariable("id") Integer id, Model model)
    {
        User member = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("member",member);
        return "/user/member_details";
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
        return "user/blog";
    }

    // show blog details
    @RequestMapping("/blog-details/{id}")
    public String blogDetails(@PathVariable("id") Integer id, Model model)
    {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        List<BlogComment> blogComment = blog.getBlogComment();
        model.addAttribute("blog",blog);
        model.addAttribute("blogComment",blogComment);
        return "/user/blog_details";
    }
    
    //process edit blog post
    @PostMapping("/blogPost-Comment/{id}")
    public String blogPostComment(@PathVariable("id") Integer id,@ModelAttribute("comment") BlogComment comment,Principal principal,HttpSession session)
    {
        User user = this.userRepository.getUserByUserName(principal.getName());
        comment.setUser(user);
        Blog blog = this.blogRepository.getBlogByBlogId(id);
        comment.setBlog(blog);
        blogCommentRepository.save(comment);
        session.setAttribute("message", new Message("Your comment is added", "success"));
        return "redirect:/user/blog-details/"+comment.getId();
    }

    // Hospital Service
    @RequestMapping("/hospital/{page}")
    public String hospital(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title","Hospital - Online Hospital Management");
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, 5, sort);
        Page<Hospital> hospitals = hospitalService.listAll(keyword,pageable);
        model.addAttribute("hospitals",hospitals);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", hospitals.getTotalPages());
        return "/user/hospital";
    }

    // show hospital details
    @RequestMapping("/hospital-details/{id}")
    public String hospitalDetails(@PathVariable("id") Integer id, Model model)
    {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(0,10,sort);
        Page<Hospital> hospitals = this.hospitalRepository.findAll(pageable);
        model.addAttribute("hospital",hospital);
        model.addAttribute("hospitals", hospitals);
        return "/user/hospital_details";
    }

    // Ambulance Service
    @RequestMapping("/ambulance/{page}")
    public String ambulance(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title","Ambulance - Online Hospital Management");
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Ambulance> ambulances = ambulanceService.listAll(keyword, pageable);
        model.addAttribute("ambulances",ambulances);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", ambulances.getTotalPages());
        return "/user/ambulance";
    }
}
