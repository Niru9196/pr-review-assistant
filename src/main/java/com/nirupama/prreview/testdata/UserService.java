package com.nirupama.prreview.testdata;

import java.util.List;

public class UserService {

    public String findUserName(List<String> users, String target) {

        for (String user : users) {
            if (user.equals(target)) {
                return user;
            }
        }

        return null;
    }

    public void processUsers(List<String> users) {

        for (String user : users) {
            for (String other : users) {
                if (user.equals(other)) {
                    System.out.println("Match found");
                }
            }
        }
    }
}