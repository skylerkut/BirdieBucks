package coms309.Users;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.ManyToOne;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.Stocks.Stock;
import coms309.Stocks.StockPurchased;
import coms309.Stocks.StockPurchasedRepository;

import java.util.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "money")
    private double money;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<StockPurchased> stocks = new ArrayList<>();

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "date_of_birth")
    private String dob;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "privilege")
    private char privilege;


    @ManyToOne
    @JoinColumn(name = "friend_group_id")
    private FriendGroup friendGroup;


    public User(Long id, double money, String name, String email, String dob, String username, String password){

        this.id = id;
        this.money = money;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.username = username;
        this.password = password;
        this.privilege = 'u';
    }

    public User(){}

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setFriendGroup(FriendGroup group){
        this.friendGroup = group;
    }

    public FriendGroup getFriendGroup(){return friendGroup;}

    public double getMoney(){return money;}

    public int getNumStocksPurchased(){return stocks.size() - 1;}

    public void setMoney(double money){this.money = money;}

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getUsername() {return username;}
    public void setUsername(String user){
        this.username = user;
    }
    public String getPassword() {return password;}
    public void setPassword(String pass){this.password = pass;}

    public char getPrivilege(){return privilege;}

    public void setPrivilege(char privilege){this.privilege = privilege;}

    public String getDob(){return dob;}

    public void setDob(String dob){this.dob = dob;}

    public StockPurchased setStock(Stock stock, int numPurchase, Long id){
        for(int i = 0; i <  stocks.size(); ++i){
            if(stock.getId().equals(stocks.get(i).getStock().getId()) && money >= numPurchase * stock.getCurrValue()){
                StockPurchased toRemove = stocks.get(i);
                stocks.get(i).setNumPurchased(stocks.get(i).getNumPurchased() + numPurchase);
                stocks.get(i).setCostPurchase(stocks.get(i).getCostPurchase() + stocks.get(i).getStock().getCurrValue() * numPurchase);
                return toRemove;
            }
        }
        StockPurchased curr = new StockPurchased();
        curr.setStock(stock);
        curr.setUser(this);
        curr.setId(id);
        curr.setNumPurchased(numPurchase);
        curr.setSinglePrice(stock.getCurrValue());
        curr.setCostPurchase(curr.getNumPurchased() * stock.getCurrValue());
        if( curr.getNumPurchased() * stock.getCurrValue() > money) return null;
        stocks.add(curr);
        stock.setUser(this, numPurchase, id);
        money -= curr.getCostPurchase();
        return curr;
    }

    //returns the amount of money that was gained from selling if it was 0, either the user doesn't have any or wasn't found
    public StockPurchased removeStocks(int numStocks, Stock stock){
        StockPurchased potentiallyRemoved;
        for(int i = 0; i < stocks.size(); ++i){
            if(stock.getId().equals(stocks.get(i).getStock().getId())){
                if(numStocks >= stocks.get(i).getNumPurchased()){
                    double moneyChanged = stocks.get(i).getCostPurchase();
                    money += moneyChanged;
                    potentiallyRemoved = stocks.get(i);
                    stocks.remove(stocks.get(i));
                    return potentiallyRemoved;
                }else{
                    double moneyChanged = stocks.get(i).getSinglePrice() * numStocks;
                    money += moneyChanged;
                    potentiallyRemoved = stocks.get(i);
                    stocks.get(i).setNumPurchased(stocks.get(i).getNumPurchased() - numStocks);
                    stocks.get(i).setCostPurchase(stocks.get(i).getCostPurchase() - numStocks * stocks.get(i).getSinglePrice());
                    return potentiallyRemoved;
                }
            }
        }
        return null;
    }


    public List<StockPurchased> getStocks(){return stocks;}

    public StockPurchased purchase(int numStocks, Stock stock, long id){
        if(numStocks < 1 || stock == null) return null;
        return setStock(stock, numStocks, id);
    }

    public StockPurchased sell(int numStocks, Stock stock){
        if(numStocks < 1 || stock == null) return null;
        return removeStocks(numStocks, stock);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (int) (prime * result + ((id == null) ? 0 : id));
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
        User other = (User) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

}
