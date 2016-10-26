package com.github.ctaras.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@lombok.Getter
@lombok.Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "password", "fullName"})
@ToString(exclude = {"password"})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @Pattern(regexp = "^[a-zA-Z]{3,255}$", message = "{entity.user.login.pattern}")
    //@NotNull(message = "{entity.user.login.notnull}")
    //@Size(min = 3, max = 255, message = "{entity.user.login.size}")
    private String login;

    @Pattern(regexp = "^.{5,255}$", message = "{entity.user.password.pattern}")
    //@NotNull(message = "{entity.user.password.notnull}")
    //@Size(min = 5, max = 255, message = "{entity.user.password.size}")
    private String password;

    @Pattern(regexp = "^[ a-zA-Z]{5,255}$", message = "{entity.user.fullname.pattern}")
    //@NotNull(message = "{entity.user.fullName.notnull}")
    //@Size(min = 5, max = 255, message = "{entity.user.fullName.size}")
    private String fullName;
}
