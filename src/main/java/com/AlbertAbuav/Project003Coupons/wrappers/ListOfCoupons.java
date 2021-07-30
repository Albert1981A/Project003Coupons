package com.AlbertAbuav.Project003Coupons.wrappers;

import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListOfCoupons {

    @ToString.Exclude
    @JsonIgnore
    private List<Coupon> coupons = new ArrayList<>();
}
