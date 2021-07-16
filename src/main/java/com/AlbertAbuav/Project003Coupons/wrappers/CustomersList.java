package com.AlbertAbuav.Project003Coupons.wrappers;

import com.AlbertAbuav.Project003Coupons.beans.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomersList {
    private List<Customer> customers = new ArrayList<>();
}
