package org.jlu.telstraapp.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jlu.telstraapp.users.dao.UserDao;
import org.jlu.telstraapp.users.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class MyUserDetailsService implements UserDetailsService {
	
	//get user from the database, via Hibernate
	@Autowired
	private UserDao userDao;

	@Transactional
	public UserDetails loadUserByUsername(final String username)
		throws UsernameNotFoundException {

		org.jlu.telstraapp.users.model.User user = userDao.findByUserName(username);
		List<GrantedAuthority> authorities =
                                      buildUserAuthority(user.getUserRole());

		return buildUserForAuthentication(user, authorities);

	}

	private User buildUserForAuthentication(org.jlu.telstraapp.users.model.User user,
		List<GrantedAuthority> authorities) {
		return new User(user.getUsername(), user.getPassword(),
			user.isEnabled(), true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		// Build user's authorities
		for (UserRole userRole : userRoles) {
			setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
		}

		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

		return Result;
	}
	
	public UserDao getUserDao() {
        return this.userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}