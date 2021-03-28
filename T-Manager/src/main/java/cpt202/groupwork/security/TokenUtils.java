package cpt202.groupwork.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import org.springframework.stereotype.Component;
import cpt202.groupwork.entity.User;


/**
 * 生成 验证用户Token
 *
 * @author
 */
@Component
public class TokenUtils implements Serializable {
    private static final long serialVersionUID = -3L;
    /**
     * Token 有效时长
     */
    private static final Long EXPIRATION = 604800L;//seconds=7days

    /**
     * 生成 Token 字符串  setAudience 接收者 setExpiration 过期时间 role 用户角色
     *
     * @param user 用户信息
     * @return 生成的Token字符串 or null
     */
    public String createToken(User user) {
        try {
            // Token 的过期时间
            Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);//过期时间:生成后七天
            // 生成 Token
            String token = Jwts.builder()
                    // 设置 Token 签发者 可选
                    .setIssuer("SpringBoot")
                    // 根据用户名设置 Token 的接受者
                    .setAudience(user.getUsername())
                    // 设置过期时间
                    .setExpiration(expirationDate)
                    // 设置 Token 生成时间 可选
                    .setIssuedAt(new Date())
                    // 通过 claim 方法设置一个 key = role，value = userRole 的值
                    .claim("role", user.getRole())
                    // 设置加密密钥和加密算法
                    .signWith(cpt202.groupwork.security.RsaUtils.getPrivateKey(), SignatureAlgorithm.RS256)
                    .compact();
            return String.format("Bearer %s", token);
        } catch (Exception e) {
            return "tokenCreate Mistake";
        }
    }

    /**
     * 验证 Token ，并获取到用户名和用户权限信息
     *
     * @param token Token 字符串
     * @return sysUser 用户信息
     */
    public User validationToken(String token) {
        try {
            // 解密 Token，获取 Claims 主体
            Claims claims = Jwts.parserBuilder()
                    // 设置私钥解密
                    .setSigningKey(cpt202.groupwork.security.RsaUtils.getPublicKey())
                    .build().parseClaimsJws(token).getBody();
            assert claims != null;
            // 验证 Token 有没有过期 过期时间
            Date expiration = claims.getExpiration();
            // 判断是否过期 过期时间要在当前日期之后
            if (!expiration.after(new Date())) {
                return null;
            }
            User user = new User();
            user.setUsername(claims.getAudience());
            user.setRole(claims.get("role").toString());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
