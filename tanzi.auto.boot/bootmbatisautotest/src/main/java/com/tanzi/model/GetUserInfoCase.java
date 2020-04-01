package com.tanzi.model;

import lombok.Data;

@Data
public class GetUserInfoCase {
    private int id ;
    private int userId;
    private String expected;
}
