package com.revworkforce.security;

import com.revworkforce.entity.Employee;
import com.revworkforce.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final EmployeeRepository employeeRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Employee employee = employeeRepository.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return User.builder()
      .username(employee.getEmail())
      .password(employee.getPassword())
      .roles(employee.getRole().getRoleName().replace("ROLE_", ""))
      .build();
  }
}
