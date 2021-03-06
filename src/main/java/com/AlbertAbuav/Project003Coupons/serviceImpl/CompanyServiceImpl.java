package com.AlbertAbuav.Project003Coupons.serviceImpl;

import com.AlbertAbuav.Project003Coupons.beans.*;
import com.AlbertAbuav.Project003Coupons.exception.invalidCompanyException;
import com.AlbertAbuav.Project003Coupons.repositories.CompanyRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CouponRepository;
import com.AlbertAbuav.Project003Coupons.repositories.CustomerRepository;
import com.AlbertAbuav.Project003Coupons.service.CompanyService;
import com.AlbertAbuav.Project003Coupons.utils.ChartUtils;
import com.AlbertAbuav.Project003Coupons.utils.Colors;
import com.AlbertAbuav.Project003Coupons.utils.CouponComponentsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Scope("prototype")
public class CompanyServiceImpl extends ClientFacade implements CompanyService {

    @Autowired
    private ChartUtils chartUtils;

    @Autowired
    private CouponComponentsUtils couponComponentsUtils;

    private int companyID;

    public CompanyServiceImpl(CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository) {
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
    public boolean login(String email, String password) throws invalidCompanyException {
        if (!companyRepository.existsByEmailAndPassword(email, password)) {
            throw new invalidCompanyException("Could not login. One or both parameters are incorrect!");
        }
        Company logged = companyRepository.findByEmailAndPassword(email, password);
        Colors.setCyanBoldPrint("The logged Company is: | ");
        chartUtils.printCompany(logged);
        System.out.println();
        companyID = logged.getId();
        return true;
    }

    /**
     * Add a new coupon.
     * Do not add a coupon with the same title to an existing coupon of the same company.
     * It is ok to add a coupon with the same title to another company's coupon.
     *
     * @param coupon Coupon
     */
    public void addCoupon(Coupon coupon) throws invalidCompanyException {
        if (Objects.isNull(coupon)) {
            throw new invalidCompanyException("There is no coupon like you entered");
        }
        if (!companyRepository.existsById(coupon.getCompanyID())) {
            throw new invalidCompanyException("The Company id \"" + coupon.getCompanyID() + "\" doesn't appears in the data base");
        }
        if (couponRepository.existsByCompanyIDAndTitle(coupon.getCompanyID(), coupon.getTitle())) {
            throw new invalidCompanyException("Cannot add a coupon with the same title to an existing coupon of the same company!");
        }
        if (coupon.getEndDate().before(coupon.getStartDate())) {
            throw new invalidCompanyException("The end date is before the start date");
        }
        Company company = companyRepository.getOne(coupon.getCompanyID());
        company.getCoupons().add(coupon);
        companyRepository.saveAndFlush(company);
    }

    /**
     * Update an existing coupon.
     * The coupon id could not be updated.
     * The company id could not be updated.
     *
     * @param coupon Coupon
     */

    public void updateCoupon(Coupon coupon) throws invalidCompanyException {
        if (Objects.isNull(coupon)) {
            throw new invalidCompanyException("There is no coupon like you entered!");
        }
        if (coupon.getCompanyID() != companyID) {
            throw new invalidCompanyException("The \"company id\" cannot be updated");
        }
        if (coupon.getEndDate().before(coupon.getStartDate())) {
            throw new invalidCompanyException("The end date is before the start date");
        }
        Company companyToUpdate = companyRepository.getOne(coupon.getCompanyID());
        List<Coupon> companyCoupons = companyToUpdate.getCoupons();

        companyToUpdate.setCoupons(couponComponentsUtils.updateCouponList(companyCoupons, coupon, companyID));
        companyRepository.saveAndFlush(companyToUpdate);
    }

    /**
     * Delete an existing coupon.
     * The purchase history of the coupon by customers must also be deleted.
     *
     * @param coupon Coupon
     */

    public void deleteCoupon(Coupon coupon) throws invalidCompanyException {
        if (Objects.isNull(coupon)) {
            throw new invalidCompanyException("There is no coupon like you entered");
        }
        if (!couponRepository.existsByIdAndCompanyID(coupon.getId(), coupon.getCompanyID())) {
            throw new invalidCompanyException("There is no coupon id-" + coupon.getId() + " in the system!");
        } else if (coupon.getCompanyID() != companyID) {
            throw new invalidCompanyException("you can not delete other companies coupons!");
        }
        couponComponentsUtils.deleteSingleCouponFromListOfCustomers(coupon);
        couponComponentsUtils.deleteCouponFromCompany(coupon);
        couponRepository.delete(coupon);
        Colors.setYellowBoldPrint("DELETED: | ");
        chartUtils.printCoupon(coupon);
    }

    /**
     * Get all company coupons.
     * That means, all the coupons of the company that made the login.
     *
     * @return List
     */
    public List<Coupon> getAllCompanyCoupons() {
        return couponRepository.findByCompanyID(companyID);
    }

    /**
     * Get all coupons from a specific category of the company.
     * That means, only coupons from a specific category of the company that made the login.
     *
     * @param category Category
     * @return List
     */
    public List<Coupon> getAllCompanyCouponsOfSpecificCategory(Category category) throws invalidCompanyException {
        if (!couponRepository.existsByCompanyIDAndCategory(companyID, category)) {
            throw new invalidCompanyException("The Company doesn't have a Coupon with this Category");
        }
        return couponRepository.findByCompanyIDAndCategory(companyID, category);
    }

    /**
     * Get all coupons up to the maximum price set by the company.
     * That means, only coupons up to the maximum price set by the company that performed the login.
     *
     * @param maxPrice double
     * @return List
     */
    public List<Coupon> getAllCompanyCouponsUpToMaxPrice(double maxPrice) throws invalidCompanyException {
        if (maxPrice < 0) {
            throw new invalidCompanyException("The max price can't be less than 0");
        }
        if (!couponRepository.existsByCompanyIDAndPriceLessThan(companyID, maxPrice)) {
            throw new invalidCompanyException("The Company doesn't have a Coupon with a price less then " + maxPrice);
        }
        return couponRepository.findByCompanyIDAndPriceLessThan(companyID, maxPrice);
    }

    /**
     * Get company details.
     * That means, the details of the company that performed the login.
     *
     * @return Company
     */
    public Company getTheLoggedCompanyDetails() {
        System.out.println(companyID);
        return companyRepository.getOne(companyID);
    }

    /**
     * The method return a single coupon of the logged company by id.
     *
     * @param id int
     * @return Coupon
     */
    public Coupon getSingleCoupon(int id) throws invalidCompanyException {
        if (!couponRepository.existsById(id)) {
            throw new invalidCompanyException("Their is no coupon for the couponID: \"" + id + "\" you entered!");
        }
        if (!couponRepository.existsByIdAndCompanyID(id, companyID)) {
            throw new invalidCompanyException("This coupon id doesn't belong to the connected company");
        }
        return couponRepository.getOne(id);
    }

    /**
     * Get all company customers of a single Coupon
     *
     * @param couponID int
     * @return List
     */
    public List<Customer> getAllCompanyCustomersOfASingleCouponByCouponId(int couponID) throws invalidCompanyException {
        if (!couponRepository.existsByIdAndCompanyID(couponID, companyID)) {
            throw new invalidCompanyException("This coupon doesn't belong to the logged company");
        }
        return customerRepository.findAllByCoupons_Id(couponID);
    }

}
