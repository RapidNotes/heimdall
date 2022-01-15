package com.rapidnotes.heimdall.domain;

import com.sun.istack.NotNull;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;

@Entity
public class User implements UserDetails {
    @Id
    @NonNull
    private String username;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NonNull
    private String email;
    @NonNull
    private String encodedPassword;

    public User(){ }

    public User(String username, String firstname, String lastname, String email, String encodedPassword) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.encodedPassword = encodedPassword;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return encodedPassword;
    }

    public String getEncodedPassword(String encodedPassword) {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
       return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
