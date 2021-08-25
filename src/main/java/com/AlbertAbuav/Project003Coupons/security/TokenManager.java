package com.AlbertAbuav.Project003Coupons.security;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginDetails;
import com.AlbertAbuav.Project003Coupons.controllers.model.LoginResponse;
import com.AlbertAbuav.Project003Coupons.exception.SecurityException;
import com.AlbertAbuav.Project003Coupons.exception.invalidAdminException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.login.ClientType;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.serviceImpl.ClientFacade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenManager {

    @Autowired
    private Map<String, Information> map;

    public String addToken(ClientFacade clientFacade) {
        String token = UUID.randomUUID().toString();
        Information information = Information.builder()
                .clientFacade(clientFacade)
                .time(LocalDateTime.now())
                .build();
        map.put(token, information);
        return token;
    }

    public void updateToken(String token){
        Information information = map.get(token);
        information.setTime(LocalDateTime.now());
        map.put(token, information);
    }

    public void deleteToken(String token) {
        map.remove(token);
    }

//    public boolean isExist(String token) {
//        if (map.containsKey(token)) {
//            return true;
//        }
//        return false;
//    }

    public boolean isExist(String token) throws SecurityException {
        if (map.get(token) == null){
            throw new SecurityException("Unauthorized entry");
        }
        return true;
    }

//    public void removeExpiredToken(int minutes) {
//        map.values().removeIf(x->x.getTime().plusMinutes(minutes).isBefore(LocalDateTime.now()));
//    }

    public void removeExpired() {
        //map.entrySet().removeIf(entry -> entry.getValue().getTime().isBefore(LocalDateTime.now().minusMinutes(10)));
        map.entrySet().removeIf(Information::removeTokensEvery10Minutes);
    }
}
