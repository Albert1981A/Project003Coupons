package com.AlbertAbuav.Project003Coupons.wrappers;

import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListOfCoupons {
    private List<Coupon> coupons = new ArrayList<>();
}
