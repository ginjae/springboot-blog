package me.kimjaemin.springbootblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequest {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "올바른 이메일 주소를 입력해 주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Size(min = 8, max = 64, message = "비밀번호는 8-64자로 입력해 주세요.")
    private String password1;

    @NotBlank(message = "비밀번호 확인을 입력해 주세요.")
    @Size(min = 8, max = 64, message = "비밀번호 확인은 8-64자로 입력해 주세요.")
    private String password2;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 2, max = 8, message = "닉네임은 2-8자로 입력해 주세요.")
    private String nickname;

}
