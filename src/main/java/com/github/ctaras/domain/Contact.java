package com.github.ctaras.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

@lombok.Getter
@lombok.Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id", "homePhoneNumber", "address", "email"})
@ToString
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    @Pattern(regexp = "^\\w{4,255}$", message = "{entity.contact.lastname.pattern}")
//    @NotNull(message = "{entity.contact.lastName.notnull}")
//    @Size(min = 4, max = 255, message = "{entity.contact.lastName.size}")
    private String lastName;

    @Pattern(regexp = "^\\w{4,255}$", message = "{entity.contact.firstname.pattern}")
//    @NotNull(message = "{entity.contact.firstName.notnull}")
//    @Size(min = 4, max = 255, message = "{entity.contact.firstName.size}")
    private String firstName;

    @Pattern(regexp = "^\\w{4,255}$", message = "{entity.contact.middlename.pattern}")
//    @NotNull(message = "{entity.contact.middleName.notnull}")
//    @Size(min = 4, max = 255, message = "{entity.contact.middleName.size}")
    private String middleName;

    @Pattern(regexp = "^\\+\\d{3}\\(\\d{2}\\) \\d{3}-\\d{4}$", message = "{entity.contact.mobilePhoneNumber.pattern}")
//    @NotNull(message = "{entity.contact.mobilePhoneNumber.notnull}")
//    @Size(min = 17, max = 17, message = "{entity.contact.mobilePhoneNumber.size}")
    private String mobilePhoneNumber;

    @Pattern(regexp = "^((\\+\\d{3}\\(\\d{2}\\) \\d{3}-\\d{4})|.{0})$", message = "{entity.contact.homePhoneNumber.pattern}")
    private String homePhoneNumber;

    private String address;

    @Pattern(regexp = "^((.{0})|(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\]))$", message = "{entity.contact.email.pattern}")
    private String email;
}
