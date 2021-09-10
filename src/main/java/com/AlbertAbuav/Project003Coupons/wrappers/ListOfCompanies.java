package com.AlbertAbuav.Project003Coupons.wrappers;

import com.AlbertAbuav.Project003Coupons.beans.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListOfCompanies {
    private List<Company> companies = new ArrayList<>();
}
