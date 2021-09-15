package com.online.hospital.managment.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.online.hospital.managment.model.comment.BlogComment;

public interface BlogCommentRepository extends JpaRepository<BlogComment, Long> {

}
