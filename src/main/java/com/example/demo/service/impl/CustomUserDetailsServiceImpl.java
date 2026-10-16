package com.example.demo.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Troupe;
import com.example.demo.repository.TroupeRepository;
import com.example.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService, CustomUserDetailsService {

    private final TroupeRepository troupeRepository; // あなたのユーザー用リポジトリ

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ※今回はDBのメールアドレスをログインIDとして使っているようなので email で検索しています
        // ユーザー名で検索する場合は findByUsername(username) 等に変更してください
        		Troupe troupe = troupeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));

        // Spring Security用のUserDetailsオブジェクトに詰めて返す
        return User.builder()
                .username(troupe.getEmail()) // ログインIDとして使う項目
                .password(troupe.getPasswordHash()) // DBに保存されているハッシュ化済みパスワード
                .roles("USER") // 権限（必要に応じて変更）
                .build();
    }
}