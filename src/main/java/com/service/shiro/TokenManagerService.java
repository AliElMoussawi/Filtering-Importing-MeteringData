package com.service.shiro;



import com.authentication.SecurityConstants;
import com.model.GroupRole;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
public class TokenManagerService {
     @Autowired
    private UserService userService;

    //Get User Info from the Token
    public User parseUserFromToken(String token){

        Claims claims = Jwts.parser()
            .setSigningKey(SecurityConstants.SECRET)
            .parseClaimsJws(token)
            .getBody();
        Optional<User> currentUser = userService.getById((String) claims.get("id"));
        return currentUser.orElse(null);
    }

    public String createTokenForUser(String username) {
      Optional<User> user = userService.getUserByUsername(username);
      return user.map(value -> Jwts.builder()
              .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
              .setSubject(value.getUsername())
              .claim("id", value.getId())
              .claim("roles", value.getRoles().stream().map(GroupRole::getCode).collect(Collectors.toList()))
             // .claim("permissions", getPermissions(value.getRoles()) )
              .claim("customerAccountId",value.getCustomerAccountId())
              .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
              .compact()).orElse(null);
    }

    private List<String> getPermissions(List<GroupRole> roles){
        return roles.stream()
                .flatMap(r -> Arrays.stream(r.getPermissions().split(","))
                        .collect(Collectors.toList()).stream())
                .collect(Collectors.toList());
    }

    public Boolean validateToken(String token, String username) {
        final String usernameFromToken = getUsernameFromToken(token);
        return (username.equals(usernameFromToken) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        System.out.println("is token expired");
        final Date expiration = getExpirationDateFromToken(token);
        System.out.println("expiration date :"+expiration.toString());
        return expiration.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        Date date=getClaimFromToken(token, Claims::getExpiration);
        System.out.println("date: "+date.toString());
        return date;
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token) {
        System.out.println("claim from token");
        return Jwts.parser().setSigningKey(SecurityConstants.SECRET).parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getCustomerAccountIdFromToken(String token){
        Claims claims =
            Jwts.parser()
            .setSigningKey(SecurityConstants.SECRET)
            .parseClaimsJws(token)
            .getBody();
         String customerAccountId= ""+userService.getById((String) claims.get("customerAccountId")).get().getCustomerAccountId();
        return customerAccountId;

    }


}
