package coms309.Stocks;

import coms309.Users.User;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "stock_purchased")
public class StockPurchased {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "amount_purchased")
    private int numPurchased;

    @Column(name = "cost_of_purchased")
    private double costPurchase;

    @Column(name = "price_of_one_at_purchase")
    private double singlePrice;

    public StockPurchased(){
    }

    public User getUser(){return user;}

    public void setUser(User user){this.user = user;}

    public Stock getStock(){return stock;}

    public void setStock(Stock stock){this.stock = stock;}

    public Long getId(){return id;}

    public int getNumPurchased(){return numPurchased;}

    public void setNumPurchased(int numPurchased){this.numPurchased = numPurchased;}

    public double getCostPurchase() {return costPurchase;}

    public void setCostPurchase(double costPurchase){this.costPurchase = costPurchase;}

    public void setId(Long id){this.id = id;}

    public double getSinglePrice(){return singlePrice;}
    public void setSinglePrice(double singlePrice){this.singlePrice = singlePrice;}



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
        StockPurchased other = (StockPurchased) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }


}
