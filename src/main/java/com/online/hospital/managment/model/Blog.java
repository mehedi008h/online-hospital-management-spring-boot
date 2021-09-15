package com.online.hospital.managment.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.online.hospital.managment.model.comment.BlogComment;

@Entity
@Table(name = "Blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    @Column(length = 5000)
    private String description;
    @Column(length = 5000)
    private String symptoms;
    @Column(length = 5000)
    private String solution;
    private String type;
    private String videoLink;
    
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogComment> blogComment = new ArrayList<>();

    public Blog() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

	public List<BlogComment> getBlogComment() {
		return blogComment;
	}

	public void setBlogComment(List<BlogComment> blogComment) {
		this.blogComment = blogComment;
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", description=" + description + ", symptoms=" + symptoms
				+ ", solution=" + solution + ", type=" + type + ", videoLink=" + videoLink + ", blogComment="
				+ blogComment + "]";
	}
    
    
}
