package com.online.hospital.managment.controller;

import com.online.hospital.managment.helper.Message;
import com.online.hospital.managment.model.*;
import com.online.hospital.managment.model.comment.BlogComment;
import com.online.hospital.managment.model.comment.BloodPostComment;
import com.online.hospital.managment.model.comment.HospitalComment;
import com.online.hospital.managment.repository.BlogRepository;
import com.online.hospital.managment.repository.BloodPostRepository;
import com.online.hospital.managment.repository.DonerRepository;
import com.online.hospital.managment.repository.HospitalRepository;
import com.online.hospital.managment.repository.UserRepository;
import com.online.hospital.managment.repository.comment.BlogCommentRepository;
import com.online.hospital.managment.repository.comment.BloodPostCommentRepository;
import com.online.hospital.managment.repository.comment.HospitalCommentRepository;
import com.online.hospital.managment.service.*;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
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

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private BloodPostCommentRepository bloodPostCommentRepository;
    
    @Autowired
    private HospitalCommentRepository hospitalCommentRepo;

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
    	User currentUser = userRepository.getUserByUserName(principal.getName());
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String lastDonateDate = currentUser.getLastDonateDate();
        boolean avaliable;
        Long remainDate;
        if(!lastDonateDate.isEmpty()) {
        	Date newDate = new Date();
            SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDate = dateFormate.format(newDate);
            try {
    			Date lDate = sdf.parse(lastDonateDate);
    			Date cDate = sdf.parse(currentDate);
    			
    			long difference_In_Time
                = cDate.getTime() - lDate.getTime();
    			
    			long difference_In_Days
                = (difference_In_Time
                   / (1000 * 60 * 60 * 24))
                  % 365;
    			remainDate = 90 - difference_In_Days;
    			System.out.println("Different "+difference_In_Days);
    			if (difference_In_Days >= 90) {
    				avaliable = true;
    			}
    			else {
					avaliable = false;
				}
    			model.addAttribute("avaliable",avaliable);
    			model.addAttribute("remainDate",remainDate);
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        } else {
        	avaliable = true;
        	remainDate = (long) 0;
        	model.addAttribute("avaliable",avaliable);
			model.addAttribute("remainDate",remainDate);
        }
        
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
        boolean dRequest = true;
        if(doners.size() == 0) {
        	dRequest = false;
        }
        model.addAttribute("doners", doners);
        model.addAttribute("dRequest", dRequest);
        return "user/blood_donate_request";
    }
    
 // blood donate request
    @GetMapping("/confrim-donate-blood")
    public String confrimDonateBlood(Model model, Principal principal) {
    	model.addAttribute("title", "Confrim Donate Blood - Online Hospital management");
    	String username = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(username);
        List<BloodPost> bloodPosts = currentUser.getBloodPosts();
        boolean dRequest = true;
        if(bloodPosts.size() == 0) {
            dRequest = false;
        }
        model.addAttribute("bloodPosts", bloodPosts);
        model.addAttribute("dRequest", dRequest);
        return "user/confrim_donate_blood";
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
            Date newDate = new Date();
            SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
            String currentDate = dateFormate.format(newDate);
            bloodPost.setTime(currentDate);
            bloodPost.setStatus(false);
            bloodPost.setUser(user);
            user.getBloodPosts().add(bloodPost);
            this.userRepository.save(user);
            String zilla = bloodPost.getZilla();
            String title = bloodPost.getTitle();
            System.out.println(zilla);
            String subject = "Online Hospital Management";
            String message = ""
                    +"<div style='border: 1px solid #e2e2e2; padding:20px'>"
                    +"<h1>" +title  +"</h1>"
                    +"</div>";
            List<User> findByAddress = userRepository.findByAddress(zilla);
            findByAddress.forEach(email -> {
            	String to = email.getEmail();
            	System.out.println("Address email" +to);
            	emailService.sendEmail(subject, message, to);
            });
            System.out.println("Address"+findByAddress);
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
    public String bloodPostDetails(@PathVariable("id") Integer id, Model model, Principal principal)
    {
        BloodPost bloodPost = bloodPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("bloodPost",bloodPost);
        List<BloodPostComment> bloodPostComment = bloodPost.getBlogComment();
        String username = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(username);
        String address = currentUser.getAddress();
        List<BloodPost> areaPost = bloodPostRepository.findByArea(address);
        System.out.println("Area Post"+areaPost);
        boolean post;
        if(areaPost.isEmpty()) {
        	post = false;
        } else {
			post = true;
		}
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String lastDonateDate = currentUser.getLastDonateDate();
        boolean avaliable;
        Long remainDate;
        if(!lastDonateDate.isEmpty()) {
        	Date newDate = new Date();
            SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDate = dateFormate.format(newDate);
            try {
    			Date lDate = sdf.parse(lastDonateDate);
    			Date cDate = sdf.parse(currentDate);
    			
    			long difference_In_Time
                = cDate.getTime() - lDate.getTime();
    			
    			long difference_In_Days
                = (difference_In_Time
                   / (1000 * 60 * 60 * 24))
                  % 365;
    			remainDate = 90 - difference_In_Days;
    			System.out.println("Different "+difference_In_Days);
    			if (difference_In_Days >= 90) {
    				avaliable = true;
    			}
    			else {
					avaliable = false;
				}
    			model.addAttribute("avaliable",avaliable);
    			model.addAttribute("remainDate",remainDate);
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        } else {
        	avaliable = true;
        	remainDate = (long) 0;
        	model.addAttribute("avaliable",avaliable);
			model.addAttribute("remainDate",remainDate);
        }
        boolean apply;
        if(lastDonateDate.isEmpty()) {
        	lastDonateDate = "Not yet donate blood";
        	apply = false;	
        	model.addAttribute("apply", apply);
        } else {
        	apply = true;
        	model.addAttribute("apply", apply);
        }
        model.addAttribute("lastDonateDate", lastDonateDate);
        model.addAttribute("areaPost", areaPost);
        model.addAttribute("post", post);
        model.addAttribute("bloodPostComment",bloodPostComment);
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
    public String processDonateBlood(@PathVariable("id") Integer id,@ModelAttribute("doner") Doner doner,Model model, Principal principal,HttpSession session)
    {
        User user = this.userRepository.getUserByUserName(principal.getName());
        doner.setLastBDDate(user.getLastDonateDate());
        doner.setUser(user);
        BloodPost bloodPost = this.bloodPostRepository.getBloodPostByBloodPostId(id);
        String to = bloodPost.getUser().getEmail();
        doner.setBloodPost(bloodPost);
        doner.setApply(true);
        donerRepository.save(doner);
        String name = doner.getUser().getName();
        String bGroup = doner.getUser().getBlood_group();
        String phone = doner.getUser().getPhone();
        String address = doner.getUser().getAddress();
        String subject = "Online Hospital Management";
        String message = ""
                +"<div style='border: 1px solid #e2e2e2; padding:20px'>"
                +"<h1> Name : " +name  +"</h1>"
                +"<h1> Phone : " +phone  +"</h1>"
                +"<h1> Address : " +address  +"</h1>"
                +"<h1> Blood Group : " +bGroup  +"</h1>"
                +"</div>";
        emailService.sendEmail(subject, message, to);
        session.setAttribute("message", new Message("Your comment is added", "success"));
        return "redirect:/user/blood-post-details/"+doner.getId();
    }
    
 // edit blog
    @GetMapping("/update-confrim/{id}")
    public String updateBlog(@PathVariable(name = "id") Long id,Model model)
    {
        Doner doner = donerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        int uId = doner.getUser().getId();
        System.out.println(uId);
        model.addAttribute("doner",doner);
        return "/user/edit_blood_status";
    }
    
  //process edit blog post
    @PostMapping("/process-doner-confrim/{id}")
    public String processDonateConfrim(@PathVariable("id") Long id,
    		@ModelAttribute("doner") Doner doner,Principal principal,HttpSession session)
    {
        Doner doners = donerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        String subject = "Online Hospital Management";
        int uId = doners.getUser().getId();
        String to = doners.getUser().getEmail();
        int bId = doners.getBloodPost().getId();
        BloodPost bloodPost = this.bloodPostRepository.getBloodPostByBloodPostId(bId);
        User user = this.userRepository.getUserByUserId(uId);
        bloodPost.setStatus(true);
        Date thisDate = new Date();
        SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String donateDate = dateFormate.format(thisDate);
        user.setLastDonateDate(donateDate);
        doner.setUser(user);
        doner.setBloodPost(bloodPost);
        doner.setStatus(true);
        donerRepository.save(doner);
        String title = doners.getBloodPost().getTitle();
        String message = ""
                +"<div style='border: 1px solid #e2e2e2; padding:20px'>"
                +"<h1>" +title  +"</h1>"
                +"<h3>" +doners.getHospitalName()  +"</h3>"
                +"<h3>" +doners.getDonateDate()  +"</h3>"
                +"<h3>" +doners.getLocation()  +"</h3>"
                +"</div>";
        emailService.sendEmail(subject,message,to);
        session.setAttribute("message", new Message("Conformation sent successfully", "success"));
        return "redirect:/user/confrim-donate-blood";
    }
    
 // show doner status
    @GetMapping("/view-doner-status/{id}")
    public String showDonerStatus(@PathVariable(name = "id") Long id,Model model)
    {
        Doner doner = donerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("doner",doner);
        return "/user/view_doner_status";
    }

    // Delete blood post
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Integer id,Model model,HttpSession session,Principal principal)
    {
        BloodPost bloodPost = this.bloodPostRepository.findById(id).get();
        bloodPost.setUser(null);
        bloodPost.setDoners(null);
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String lastDonateDate = member.getLastDonateDate();
        boolean avaliable;
        Long remainDate;
        if(!lastDonateDate.isEmpty()) {
        	Date newDate = new Date();
            SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDate = dateFormate.format(newDate);
            try {
    			Date lDate = sdf.parse(lastDonateDate);
    			Date cDate = sdf.parse(currentDate);
    			
    			long difference_In_Time
                = cDate.getTime() - lDate.getTime();
    			
    			long difference_In_Days
                = (difference_In_Time
                   / (1000 * 60 * 60 * 24))
                  % 365;
    			remainDate = 90 - difference_In_Days;
    			System.out.println("Different "+difference_In_Days);
    			if (difference_In_Days >= 90) {
    				avaliable = true;
    			}
    			else {
					avaliable = false;
				}
    			model.addAttribute("avaliable",avaliable);
    			model.addAttribute("remainDate",remainDate);
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        } else {
        	avaliable = true;
        	remainDate = (long) 0;
        	model.addAttribute("avaliable",avaliable);
			model.addAttribute("remainDate",remainDate);
        }
        boolean apply;
        if(lastDonateDate.isEmpty()) {
        	lastDonateDate = "Not yet donate blood";
        	apply = false;	
        	model.addAttribute("apply", apply);
        } else {
        	apply = true;
        	model.addAttribute("apply", apply);
        }
        model.addAttribute("member",member);
        boolean bPost = false;
        if (member.getBloodPosts().size() == 0) {
        	bPost = true;
        }
        boolean dRequest = false;
        if (member.getDoners().size() == 0) {
        	dRequest = true;
        }
        model.addAttribute("bPost",bPost);
        model.addAttribute("dRequest",dRequest);
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
    
    // Hospital Service
    @RequestMapping("/hospital/{page}")
    public String hospital(@PathVariable("page") Integer page, Model model,@Param("keyword") String keyword)
    {
        model.addAttribute("title","Hospital - Online Hospital Management");
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(page, 10, sort);
        Page<Hospital> hospitals = hospitalService.listAll(keyword,pageable);
        model.addAttribute("hospitals",hospitals);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", hospitals.getTotalPages());
        return "/user/hospital";
    }

    // show hospital details
    @RequestMapping("/hospital-details/{id}")
    public String hospitalDetails(@PathVariable("id") Integer id, Model model, Principal principal)
    {
        Hospital hospital = hospitalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        String username = principal.getName();
        User currentUser = this.userRepository.getUserByUserName(username);
        String area = currentUser.getAddress();
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(0,10,sort);
        Page<Hospital> hospitals = this.hospitalRepository.findAll(pageable);
        List<HospitalComment> hospitalComment = hospital.getHospitalComment();
        List<Hospital> areaHospital = hospitalRepository.findByArea(area);
        boolean found;
        if(areaHospital.isEmpty()) {
        	found = false;
        } else {
        	found = true;
        }
        model.addAttribute("hospital",hospital);
        model.addAttribute("hospitals", hospitals);
        model.addAttribute("areaHospital", areaHospital);
        model.addAttribute("found", found);
        model.addAttribute("hospitalComment", hospitalComment);
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
    
    @RequestMapping("/covid-19")
    public String covid(Model model) {
    	model.addAttribute("title","Covid-19 - Online Hospital Management");
    	return "/user/covid";
    }
    
  //process blog post comment
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
    
  //process blood post comment
    @PostMapping("/bloodPost-Comment/{id}")
    public String bloodPostComment(@PathVariable("id") Integer id,@ModelAttribute("comment") BloodPostComment comment,Principal principal,HttpSession session)
    {
        User user = this.userRepository.getUserByUserName(principal.getName());
        comment.setUser(user);
        BloodPost post = this.bloodPostRepository.getBloodPostByBloodPostId(id);
        comment.setBloodPost(post);
        bloodPostCommentRepository.save(comment);
        session.setAttribute("message", new Message("Your comment is added", "success"));
        return "redirect:/user/blood-post-details/"+comment.getId();
    }
    
  //process hospital comment
    @PostMapping("/hospital-Comment/{id}")
    public String hospitalComment(@PathVariable("id") Integer id,@ModelAttribute("comment") HospitalComment comment,Principal principal,HttpSession session)
    {
        User user = this.userRepository.getUserByUserName(principal.getName());
        comment.setUser(user);
        Hospital hospital = this.hospitalRepository.getHospitalbyId(id);
        comment.setHospital(hospital);
        hospitalCommentRepo.save(comment);
        session.setAttribute("message", new Message("Your comment is added", "success"));
        return "redirect:/user/hospital-details/"+comment.getId();
    }
}
