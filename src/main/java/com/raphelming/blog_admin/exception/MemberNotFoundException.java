package com.raphelming.blog_admin.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String name) {
        super(name + " NotFoundException");
    }
}
