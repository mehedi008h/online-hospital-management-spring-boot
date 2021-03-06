package com.online.hospital.managment.model;

import javax.persistence.*;

import org.hibernate.type.TrueFalseType;

import com.online.hospital.managment.model.comment.BlogComment;
import com.online.hospital.managment.model.comment.BloodPostComment;
import com.online.hospital.managment.model.comment.HospitalComment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @Column(unique = true)
    private String email;
    private String phone;
    private String address;
    private String blood_group;
    private String gender;
    private String work;
    private String bod;
    private String image;
    @Column(length = 5000)
    private String description;
    private String lastDonateDate;
    private String password;
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloodPost> bloodPosts = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogComment> blogComment = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BloodPostComment> bloodPostComment = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Doner> doners = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalComment> hospitalComment = new ArrayList<>();
    
    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }
    
    public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLastDonateDate() {
		return lastDonateDate;
	}

	public void setLastDonateDate(String lastDonateDate) {
		this.lastDonateDate = lastDonateDate;
	}
	
	public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getBod() {
        return bod;
    }

    public void setBod(String bod) {
        this.bod = bod;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<BloodPost> getBloodPosts() {
        return bloodPosts;
    }

    public void setBloodPosts(List<BloodPost> bloodPosts) {
        this.bloodPosts = bloodPosts;
    }
    
	public List<BlogComment> getBlogComment() {
		return blogComment;
	}

	public void setBlogComment(List<BlogComment> blogComment) {
		this.blogComment = blogComment;
	}
	
	public List<Doner> getDoners() {
		return doners;
	}

	public void setDoners(List<Doner> doners) {
		this.doners = doners;
	}
	
	public List<BloodPostComment> getBloodPostComment() {
		return bloodPostComment;
	}

	public void setBloodPostComment(List<BloodPostComment> bloodPostComment) {
		this.bloodPostComment = bloodPostComment;
	}
	
	public List<HospitalComment> getHospitalComment() {
		return hospitalComment;
	}

	public void setHospitalComment(List<HospitalComment> hospitalComment) {
		this.hospitalComment = hospitalComment;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", address=" + address
				+ ", blood_group=" + blood_group + ", gender=" + gender + ", work=" + work + ", bod=" + bod + ", image="
				+ image + ", description=" + description + ", lastDonateDate=" + lastDonateDate + ", password="
				+ password + ", role=" + role + ", bloodPosts=" + bloodPosts + ", blogComment=" + blogComment
				+ ", bloodPostComment=" + bloodPostComment + ", doners=" + doners + ", hospitalComment="
				+ hospitalComment + "]";
	}

	
}
