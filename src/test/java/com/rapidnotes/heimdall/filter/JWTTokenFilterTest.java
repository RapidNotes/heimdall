package com.rapidnotes.heimdall.filter;

import com.rapidnotes.heimdall.dao.UserRepo;
import com.rapidnotes.heimdall.domain.User;
import com.rapidnotes.heimdall.util.JWTTokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


class JWTTokenFilterTest {

    @Mock
    JWTTokenUtil jwtTokenUtil;
    @Mock
    UserRepo userRepo;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;

    JWTTokenFilter filter;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
       closeable = MockitoAnnotations.openMocks(this);
       filter = new JWTTokenFilter(jwtTokenUtil, userRepo);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @DisplayName("Parameterized doFilterInternal test with invalid auth header")
    @ParameterizedTest(name = "{displayName} \"{0}\"")
    @MethodSource("invalidAuthHeaderValues")
    void doFilterInternalInvalidAuthHeaders(String authHeader) throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(authHeader);
        filter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
    }


    @Test
    @DisplayName("doFilterInternal test with invalid jwt token")
    void doFilterInternalInvalidToken() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer test_token");
        Mockito.when(jwtTokenUtil.validate("test_token")).thenReturn(Boolean.FALSE);
        filter.doFilterInternal(request, response, filterChain);
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilterInternal test with valid jwt token")
    void doFilterInternalValidToken() throws ServletException, IOException {
        Mockito.when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer test_token");
        Mockito.when(jwtTokenUtil.validate("test_token")).thenReturn(Boolean.TRUE);
        Mockito.when(jwtTokenUtil.getUsername("test_token")).thenReturn("test_user");
        User user = Mockito.mock(User.class);
        Mockito.when(userRepo.findById("test_user")).thenReturn(Optional.of(user));
        filter.doFilterInternal(request, response, filterChain);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getPrincipal(), equalTo(user));
        Mockito.verify(filterChain).doFilter(request, response);
    }

    static Stream<String> invalidAuthHeaderValues() {
        return Stream.of("", "test_token", null);
    }


}