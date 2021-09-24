package com.AlbertAbuav.Project003Coupons.serviceImpl;

import com.AlbertAbuav.Project003Coupons.beans.Category;
import com.AlbertAbuav.Project003Coupons.beans.Coupon;
import com.AlbertAbuav.Project003Coupons.beans.Customer;
import com.AlbertAbuav.Project003Coupons.exception.invalidCustomerException;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CouponRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CustomerRepository;
import com.AlbertAbuav.Project003Coupons.service.CustomerService;
import com.AlbertAbuav.Project003Coupons.utils.ChartUtils;
import com.AlbertAbuav.Project003Coupons.utils.Colors;
import com.AlbertAbuav.Project003Coupons.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Scope("prototype")
public class CustomerServiceImpl extends ClientFacade implements CustomerService {

    @Autowired
    private ChartUtils chartUtils;

    private int customerID;

    public CustomerServiceImpl(CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository) {
        super(companyRepository, customerRepository, couponRepository);
    }

    /**
     * Login method, the method is implemented from the abstract class "ClientFacade"
     *
     * @param email    String
     * @param password String
     * @return boolean
     */
    @Override
    public boolean login(String email, String password) throws invalidCustomerException {
        if (!customerRepository.existsByEmailAndPassword(email, password)) {
            throw new invalidCustomerException("Could not login. One or both parameters are incorrect!");
        }
        Customer logged = customerRepository.findByEmailAndPassword(email, password);
        Colors.setPurpleBoldPrint("The logged Customer is: | ");
        chartUtils.printCustomer(logged);
        System.out.println();
        customerID = logged.getId();
        return true;
    }

    /**
     * Purchase a coupon.
     * You cannot purchase the same coupon more than once.
     * The coupon cannot be purchased if its quantity is 0.
     * The coupon cannot be purchased if its expiration date has already been reached.
     * After the purchase, the quantity in stock of the coupon must be reduced by 1.
     *
     * @param coupon Coupon
     */
    public void addCoupon(Coupon coupon) throws invalidCustomerException {
        if (Objects.isNull(coupon)) {
            throw new invalidCustomerException("There is no coupon like you entered!");
        }
        if (coupon.getAmount() == 0) {
            throw new invalidCustomerException("The coupon id-" + coupon.getId() + " is out of stock!");
        } else if (coupon.getEndDate().before(DateUtils.javaDateFromLocalDate(LocalDate.now()))) {
            throw new invalidCustomerException("The coupon id-" + coupon.getId() + " has expired!");
        } else if (couponRepository.existsByCustomers_IdAndId(customerID, coupon.getId())) {
            throw new invalidCustomerException("You already purchase coupon id-" + coupon.getId() + ". You cannot purchase the same coupon more than once!");
        }
        Customer loggedCustomer = customerRepository.getOne(customerID);
        coupon.setAmount((coupon.getAmount()) - 1);
        loggedCustomer.getCoupons().add(coupon);
        customerRepository.saveAndFlush(loggedCustomer);
    }

    /**
     * Get all coupons purchased by the customer.
     * That means, all the coupons purchased by the customer who made the login.
     *
     * @return List
     */
    public List<Coupon> getAllCustomerCoupons() throws invalidCustomerException {
        if (!couponRepository.existsByCustomers_Id(customerID)) {
            throw new invalidCustomerException("Their is no Coupons to the Logged customer");
        }
        return couponRepository.findAllByCustomers_Id(customerID);
    }

    /**
     * Get all coupons from a specific category purchased by the customer.
     * That means, only coupons from a specific category of the customer who made the login.
     *
     * @param category Category
     * @return List
     */
    public List<Coupon> getAllCustomerCouponsOfSpecificCategory(Category category) throws invalidCustomerException {
        if (!couponRepository.existsByCustomers_IdAndCategory(customerID, category)) {
            throw new invalidCustomerException("Their is no Coupons of that category to the Logged customer");
        }
        return couponRepository.findAllByCustomers_IdAndCategory(customerID, category);
    }

    /**
     * Get all coupons up to the maximum price set by the customer purchased.
     * That means, return only coupons up to the maximum price set by the customer who made the login.
     *
     * @param maxPrice double
     * @return List coupons
     */
    public List<Coupon> getAllCustomerCouponsUpToMaxPrice(double maxPrice) throws invalidCustomerException {
        if (!couponRepository.existsByCustomers_IdAndPriceLessThan(customerID, maxPrice)) {
            throw new invalidCustomerException("Their is no Coupons fo that max price to the Logged customer");
        }
        return couponRepository.findAllByCustomers_IdAndPriceLessThan(customerID, maxPrice);
    }

    /**
     * Get customer details.
     * That means, the details of the customer who performed the login.
     *
     * @return customer
     */
    public Customer getTheLoggedCustomerDetails() {
        return customerRepository.getOne(customerID);
    }

    /**
     * Get the customers of a single coupon.
     *
     * @return List customers
     */
    public List<Customer> findAllCustomersByCouponId(int couponID) {
        return customerRepository.findAllByCoupons_Id(couponID);
    }

    @Override
    public Coupon getOneByCouponId(int couponID) {
        return couponRepository.getOne(couponID);
    }


}
