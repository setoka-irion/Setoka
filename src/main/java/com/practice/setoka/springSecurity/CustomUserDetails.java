package com.practice.setoka.springSecurity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.practice.setoka.Enum.Status;
import com.practice.setoka.dao.Users;

public class CustomUserDetails implements UserDetails {
	 private final Users user;
	 
	 public CustomUserDetails(Users user) {
		 this.user = user;
	 }
	 
	 public Users getUser() {
		 return user;
	 }
	 
	 @Override
	 public Collection<? extends GrantedAuthority> getAuthorities(){
		 String roleStr = "ROLE_" + user.getGrade().name();
		 return List.of(new SimpleGrantedAuthority(roleStr));
	 }
	 
	 @Override
	 public String getPassword() {
		 return user.getPassword();
	 }
	 @Override
	 public String getUsername() {
		 return user.getId();
	 }
	 @Override
	 public boolean equals(Object obj) {
	     if (this == obj) return true;
	     if (obj == null || getClass() != obj.getClass()) return false;
	     CustomUserDetails that = (CustomUserDetails) obj;
	     return Objects.equals(this.getUsername(), that.getUsername());
	 }

	 @Override
	 public int hashCode() {
	     return Objects.hash(getUsername());
	 }
	 
	 @Override public boolean isAccountNonExpired() { return true; }
	 @Override public boolean isAccountNonLocked() { return true; }
	 @Override public boolean isCredentialsNonExpired() { return true; }
	 @Override public boolean isEnabled() { return user.getStatus() != Status.삭제; }
	 
}
