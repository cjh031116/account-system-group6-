package com.accounting.system.service.impl;

import com.accounting.system.service.UserService;

import java.util.prefs.Preferences;

public class UserServiceImpl implements UserService {
    private static final String PREF_USERNAME = "remembered_username";
    private String currentUser;
    private final Preferences prefs;
    
    public UserServiceImpl() {
        this.prefs = Preferences.userNodeForPackage(UserServiceImpl.class);
    }

} 