package com.AlbertAbuav.Project003Coupons.beans;

import com.AlbertAbuav.Project003Coupons.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int companyID;
    @Enumerated(EnumType.ORDINAL)
    private Category category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private int amount;
    private double price;
    private String image;
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "coupons")
    @Singular  // ==> Works with Builder and initializes the list and let us insert a single book each time
    private List<Customer> customers = new ArrayList<>();


    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", companyID=" + companyID +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + DateUtils.beautifyDate(startDate) +
                ", endDate=" + DateUtils.beautifyDate(endDate) +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                '}';
    }
}
