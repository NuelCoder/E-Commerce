package com.example.pcuhub.pcuhub.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.pcuhub.pcuhub.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message= "Please provide a name!")
    @Column(nullable= false)
    private String name;

    @NotBlank(message= "Please provide an email")
    @Column(nullable= false, unique= true)
    @Pattern(
        regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$",
        message = "Invalid email address"
    )
    private String email;

     @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%.^&+=!]).{8,}$",
        message = "Password must be at least 8 characters long and contain uppercase, lowercase, digit, and special character"
    )

   
    @Column(nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable= false)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // @OneToMany(mappedBy= "createdBy", cascade=CascadeType.ALL)
    // private List<Category> categories;

    @OneToMany(mappedBy= "user", cascade= CascadeType.ALL)
    private  List<CartItem> cartItems;

    @OneToMany(mappedBy= "createdBy", cascade= CascadeType.ALL)
    private List<Product> products;

    @OneToMany(mappedBy= "user", cascade= CascadeType.ALL)
    private List<Order> orders;

    boolean isAccountNonExpired = true;
    boolean isAccountNonLocked = true;
    boolean isCredentialsNonExpired = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
       return email;
    }

    @Override
    public boolean isAccountNonExpired() {
      return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
      return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
       return isCredentialsNonExpired;
    }
}
