package org.fffd.l23o6.pojo.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "注册会员请求")
public class MembershipRequest {
    @Schema(description = "密码", required = true)
    @NotNull
    @Size(min = 6, max = 6, message = "密码长度必须为6位")
    @Pattern.List({
            @Pattern(regexp = "^[0-9]*$", message = "密码只能包含数字")
    })
    private String vippassword;
}
