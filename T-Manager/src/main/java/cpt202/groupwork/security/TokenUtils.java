package cpt202.groupwork.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import org.springframework.stereotype.Component;
import cpt202.groupwork.entity.User;

/**
 * @className: TokenUtils
 * @description: generating token for authorized user
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Component
public class TokenUtils implements Serializable {
    private static final long serialVersionUID = -3L;
    /**
     * Token valid time
     */
    private static final Long EXPIRATION = 604800L; //seconds=7days

    /**
     * generating token, set audience, set expiration time, set user role
     * @param user
     * @return token string or null
     */
    public String createToken(User user) {
        try {
            // Token expire time
            Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000); //expire time 7days
            // generating Token
            String token = Jwts.builder()
                    // set Token issuer (optional)
                    .setIssuer("SpringBoot")
                    // using username set Token audience
                    .setAudience(user.getUserName())
                    // set expiration date
                    .setExpiration(expirationDate)
                    // set Token generating date
                    .setIssuedAt(new Date())
                    // using claim method to set a key = role，value = userRole
                    .claim("role", user.getUserRole())
                    // set encrypt algorithm
                    .signWith(cpt202.groupwork.security.RsaUtils.getPrivateKey(), SignatureAlgorithm.RS256)
                    .compact();
            return String.format("%s", token);
        } catch (Exception e) {
            return "tokenCreate Mistake";
        }
    }

    /**
     * verify token and get username and permission
     * @param token
     * @return sysUser
     */
    public User validationToken(String token) {
        try {
            // decrypt Token，get Claims body
            Claims claims = Jwts.parserBuilder()
                    // set private key to decrypt
                    .setSigningKey(cpt202.groupwork.security.RsaUtils.getPublicKey())
                    .build().parseClaimsJws(token).getBody();
            assert claims != null;
            // verify whether the token is expired
            Date expiration = claims.getExpiration();
            if (!expiration.after(new Date())) {
                return null;
            }
            User user = new User();
            user.setUserName(claims.getAudience());
            user.setUserRole(claims.get("role").toString());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
