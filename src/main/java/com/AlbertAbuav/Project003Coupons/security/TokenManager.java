package com.AlbertAbuav.Project003Coupons.security;

import com.AlbertAbuav.Project003Coupons.serviceImpl.ClientFacade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    public void deleteToken(String token) {
        map.remove(token);
    }

//    public boolean isExist(String token) {
//        if (map.containsKey(token)) {
//            return true;
//        }
//        return false;
//    }

    public boolean isExist(String token) {
        return map.get(token) != null;
    }

//    public void removeExpiredToken(int minutes) {
//        map.values().removeIf(x->x.getTime().plusMinutes(minutes).isBefore(LocalDateTime.now()));
//    }

    public void removeExpired() {
        //map.entrySet().removeIf(entry -> entry.getValue().getTime().isBefore(LocalDateTime.now().minusMinutes(10)));
        map.entrySet().removeIf(Information::removeTokensEvery10Minutes);
    }
}
