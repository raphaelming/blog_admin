package com.raphelming.blog_admin.controller;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수 입력입니다.")
    private String name;
    private String password;
    private String phoneNumber;
}
