package coms309.Stocks;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import coms309.Users.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "company")
    private String company;

    @Column(name = "current_value")
    private double currValue;

    @Column(name = "previous_day_change")
    private double prevDayChange;

    private String companyWebsite = "investor.gov";

    private String companyLogo;

    @JsonIgnore
    @OneToMany(mappedBy = "stock")
    private List<StockPurchased> users = new ArrayList<>();


    public Stock(Long id, String symbol, String company, double currValue, double prevDayChange){
        this.id = id;
        this.symbol = symbol;
        this.company = company;
        this.currValue = currValue;
        this.prevDayChange = prevDayChange;
    }

    public Stock(Long id, String symbol, String company, double currValue, double prevDayChange, String companyWebsite){
        this.id = id;
        this.companyWebsite = companyWebsite;
        this.symbol = symbol;
        this.company = company;
        this.currValue = currValue;
        this.prevDayChange = prevDayChange;
    }

    public Stock(){}


    public String getCompanyWebsite(){return companyWebsite;}

    public String getCompanyLogo(){return companyLogo;}

    public void setCompanyWebsite(String companyWebsite){this.companyWebsite = companyWebsite;}

    public void setCompanyLogo(String companyLogo){this.companyLogo = companyLogo;}
    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}
    public String getSymbol(){return  symbol;}
    public void setSymbol(String symbol){this.symbol = symbol;}
    public String getCompany(){return company;}
    public void setCompany(String company){this.company = company;}
    public double getCurrValue(){return currValue;}
    public void setCurrValue(double currValue){this.currValue = currValue;}
    public double getPrevDayChange(){return prevDayChange;}
    public void setPrevDayChange(double prevDayChange){this.prevDayChange = prevDayChange;}
    public void setUser(User user, int numBuying, Long id){
        StockPurchased curr = new StockPurchased();
        curr.setId(id);
        curr.setUser(user);
        curr.setStock(this);
        curr.setSinglePrice(currValue);
        curr.setNumPurchased(numBuying);
        curr.setCostPurchase(numBuying * currValue);
        users.add(curr);
    }

    public List<StockPurchased> getUsers(){return users;}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Stock other = (Stock) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}
