package com.vet.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.vet.auth_service.model.AuthUser;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final AuthUser user; // Ссылаемся на модель AuthUser

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Если нужно, добавьте роли, например, ROLE_USER или другие.
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // Возвращаем зашифрованный пароль
    }

    @Override
    public String getUsername() {
        return user.getLogin(); // Логин пользователя
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // По умолчанию считаем аккаунт не истекшим
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // По умолчанию аккаунт не заблокирован
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // По умолчанию считаем, что креды не истекли
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
