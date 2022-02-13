package com.rapidnotes.heimdall.util;

import com.rapidnotes.heimdall.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;


class JWTTokenUtilTest {

    private String jwtSecret;
    private Key signingKey;
    private String jwtIssuer;
    private JWTTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        // closeable = MockitoAnnotations.openMocks(this);
        jwtSecret = "testsecretasdasdasdsagdhasghdkgashdgahsgdhjagshdgashgdahgshghasgdhgashdgahghagshdgashfghdgfsfsdf";
        signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(jwtSecret),
                SignatureAlgorithm.HS512.getJcaName());
        jwtIssuer = "test.com";
        jwtTokenUtil = new JWTTokenUtil(jwtSecret, signingKey, jwtIssuer);
    }

    @Test
    @DisplayName("validate test with valid jwt token")
    void validate() {
        String jwtToken = Jwts.builder().setSubject("test_username").signWith(signingKey).compact();
        boolean validated = jwtTokenUtil.validate(jwtToken);
        Assertions.assertTrue(validated);
    }

    @Test
    @DisplayName("validate test with jwt token signed with different jwt signature")
    void validateFailInvalidJWTSignature() {
        String jwtToken = Jwts.builder()
                .setSubject("test_username")
                .signWith(new SecretKeySpec(
                        DatatypeConverter.parseBase64Binary(
                                "invalid_signature_test_test_test_test_test_test_test"),
                                SignatureAlgorithm.HS512.getJcaName()))
                .compact();
        boolean validated = jwtTokenUtil.validate(jwtToken);
        Assertions.assertFalse(validated);
    }

    @Test
    @DisplayName("validate test with malformed jwt token")
    void validateFailMalformedToken() {
        String jwtToken = "random_token_string";
        boolean validated = jwtTokenUtil.validate(jwtToken);
        Assertions.assertFalse(validated);
    }

    @Test
    @DisplayName("validate test with expired jwt token")
    void validateFailExpiredToken() {
        String jwtToken = Jwts.builder()
                .setSubject("test_username")
                .signWith(signingKey)
                .setExpiration(new Date(System.currentTimeMillis() - 3600))
                .compact();
        boolean validated = jwtTokenUtil.validate(jwtToken);
        Assertions.assertFalse(validated);
    }

    @Test
    @DisplayName("validate test with jwt token without subject")
    void validateFailUnsupportedToken() {
        String jwtToken = Jwts.builder()
                .signWith(signingKey)
                .compact();
        boolean validated = jwtTokenUtil.validate(jwtToken);
        Assertions.assertFalse(validated);
    }

    @Test
    @DisplayName("validate test with invalid jwt tokens(empty or null)")
    void validateFailInvalidToken() {
        String emptyJwtToken = " ";
        boolean validated1 = jwtTokenUtil.validate(emptyJwtToken);
        boolean validated2 = jwtTokenUtil.validate(null);
        Assertions.assertFalse(validated1);
        Assertions.assertFalse(validated2);
    }

    @Test
    @DisplayName("generateJWT test generate for test user")
    void generateJWT() {
        User user = new User(
                "test_username",
                "firstname",
                "lastname",
                "test@email.com",
                "test_encoded_password");
        String jwtToken = jwtTokenUtil.generateJWT(user);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
        assertThat(claims.getSubject(), is(equalTo("test_username")));
        assertThat(claims.getIssuer(), is(equalTo(jwtIssuer)));
        assertThat(claims.getIssuedAt().toString(), is(equalTo(new Date().toString())));
        Assertions.assertTrue(claims.getExpiration().after(new Date(System.currentTimeMillis())));
    }

    @Test
    @DisplayName("getUsername test with valid jwt token")
    void getUsername() {
        String jwtToken = Jwts.builder().setSubject("test_username").signWith(signingKey).compact();
        String subject = jwtTokenUtil.getUsername(jwtToken);
        assertThat(subject, is(equalTo("test_username")));
    }
}