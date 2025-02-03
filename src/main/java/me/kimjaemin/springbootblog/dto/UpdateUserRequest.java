package me.kimjaemin.springbootblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "닉네임을 입력해 주세요.")
    @Size(min = 2, max = 15, message = "닉네임은 2-15자로 입력해 주세요.")
    private String nickname;

}
