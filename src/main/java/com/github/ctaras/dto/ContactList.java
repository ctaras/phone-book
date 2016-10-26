package com.github.ctaras.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.ctaras.domain.Contact;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ContactList implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("data")
    private List<Contact> listModel;
}
