package com.AlbertAbuav.Project003Coupons.security;

import com.AlbertAbuav.Project003Coupons.serviceImpl.ClientFacade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Information {

    private ClientFacade clientFacade;
    private LocalDateTime time;

    public static boolean removeTokensEvery10Minutes(Map.Entry<String, Information> entry) {
        return entry.getValue().getTime().isBefore(LocalDateTime.now().minusMinutes(10));
    }


}
