package coms309;

import coms309.Stocks.StockPurchasedRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.ComponentScan;

import coms309.Stocks.Stock;
import coms309.Stocks.StockRepository;
import coms309.Users.User;
import coms309.Users.UserRepository;
import coms309.Users.FriendGroup;
import coms309.Users.FriendGroupRepository;
import coms309.chat.ChatSocket;
import coms309.chat.Message;
import coms309.chat.MessageRepository;

import java.util.UUID;


// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories
@ComponentScan(basePackages = {"coms309", "coms309.Users", "coms309.chat"})
public class Main {
    public static void main(String[] args) {SpringApplication.run(Main.class, args);}
    @Bean
    CommandLineRunner initUser(
            UserRepository userRepository,
            StockRepository stockRepository,
            StockPurchasedRepository spRepository,
            FriendGroupRepository friendGroupRepository
    ) {
        return args -> {



            User user1 = new User(1L, 1987654, "Nick", "kokottni@iastate.edu", "oldenough", "user", "Password");
            user1.setPrivilege('a');
            User user2 = new User(2L, 34578, "Josh", "jwhit@iastate.edu", "oldenough", "username", "password");
//            user2.setPrivilege('g');
            User user3 = new User(3L, 6543, "Shiv", "shiv@iastate.edu", "nah", "username2", "password");
            User user4 = new User(4L, 7765, "Skyler", "sky@iastate.edu", "yup", "user3", "passman");
            Stock stock1 = new Stock(1L, "TSLA", "Tesla", 101.23, -2.13);
            Stock stock2 = new Stock(2L, "INTC", "Intel", 67.12, 1.34);
            Stock stock3 = new Stock(3L, "GGL", "Google", 213.56, 3.14);
            Stock stock4 = new Stock(4L, "EXAS", "Exact Sciences", 67.34, -2.13);
            Stock stock5 = new Stock(5L, "NVDA",	"NVIDIA", 	478.21,	-4.21);
            Stock stock6 = new Stock(6L, "META",	"Meta", 338.99,	4.29);
            Stock stock7 = new Stock(7L, "HSBC",	"HSBC Holdings, plc",	38.59,	-0.02);
            Stock stock8 = new Stock(8L, "LLY",	"Eli Lilly and Company", 591.60,	0.07);
            Stock stock9 = new Stock(9L, "TSM",	"Taiwan Semiconductor Manufacturing", 97.98,	0.77);
            Stock stock10 = new Stock(10L,"UNH",	"UnitedHealth Group Incorporated", 540.53,	-3.06);
            Stock stock11 = new Stock(11L,"V",	"Visa",	252.94,	-1.20);
            Stock stock12 = new Stock(12L,"NVO",	"Novo Nordisk", 101.43,	-2.44);
            Stock stock13 = new Stock(13L,"JPM",	"JP Morgan Chase & Co", 153.54,	0.35);
            Stock stock14 = new Stock(14L,"SHEL",	"Shell PLC", 66.00,	0.19);
            Stock stock15 = new Stock(15L,"WMT",	"Walmart Inc", 158.64,	1.87);
            Stock stock16 = new Stock(16L,"XOM",	"Exxon Mobil Corporation", 103.90,	-0.06);
            Stock stock17 = new Stock(17L,"AVGO",	"Broadcom Inc", 946.35,	-3.89);
            Stock stock18 = new Stock(18L,"MA",	"Mastercard Incorporated", 409.01,	0.04);
            Stock stock19 = new Stock(19L,"JNJ",	"Johnson & Johnson", 151.63,	0.35);
            Stock stock20 = new Stock(20L,"PG",	"Procter & Gamble Company", 152.29,	1.05, "us.pg.com/");
            Stock stock21 = new Stock(21L,"ORCL",	"Oracle Corporation", 116.24,	-0.23);
            Stock stock22 = new Stock(22L,"BHP",	"BHP Group", 61.64,	0.33);
            Stock stock23 = new Stock(23L,"HD",	"Home Depot", 313.34,	2.42);
            Stock stock24 = new Stock(24L,"ADBE",	"Adobe",	623.32,	4.05);
            Stock stock25 = new Stock(25L,"CVX",	"Chevron", 145.51,	1.15);
            Stock stock26 = new Stock(26L,"ASML",	"ASML Holding",	675.99,	-12.39);
            Stock stock27 = new Stock(27L,"COST",	"Costco", 	594.00,	-0.90);
            Stock stock28 = new Stock(28L,"TM",	"Toyota", 187.16,	0.55);
            Stock stock29 = new Stock(29L,"MRK",	"Merck & Company, Inc.", 	100.18,	-1.25);
            Stock stock30 = new Stock(30L,"KO",	"Coca-Cola",	58.58,	0.12);
            Stock stock31 = new Stock(31L,"ABBV",	"AbbVie Inc", 138.08,	-1.01);
            Stock stock32 = new Stock(32L,"NGG",	"National Grid Transco",	65.70,	0.29);
            Stock stock33 = new Stock(33L,"BAC",	"Bank of America", 29.53,	-0.03);
            Stock stock34 = new Stock(34L,"PEP",	"PepsiCo", 	168.86,	0.54);
            Stock stock35 = new Stock(35L,"ACN",	"Accenture",	332.56,	0.13);
            Stock stock36 = new Stock(36L,"CRM",	"Salesforce", 	224.92,	0.13);
            Stock stock37 = new Stock(37L,"NFLX",	"Netflix",	479.00,	-0.17);
            Stock stock38 = new Stock(38L,"NVS",	"Novartis AG", 	97.00,	-0.74);
            Stock stock39 = new Stock(39L,"MCD",	"McDonalds", 	282.09,	0.25);
            Stock stock40 = new Stock(40L,"LIN",	"Linde plc", 	410.73,	-2.07);
            Stock stock41 = new Stock(41L,"AMD",	"Advanced Micro Devices, Inc", 122.01,	-0.64);
            Stock stock42 = new Stock (42L,"BABA",	"Alibaba Group", 	76.74,	-0.79);
            Stock stock43 = new Stock(43L,"CSCO",	"Cisco", 77.85,	-0.08);
            Stock stock44 = new Stock(44L,"SAP",	"SAP SE ADS",	156.30,	1.36);
            Stock stock45 = new Stock(45L,"TMO",	"Thermo Fisher Scientific Inc", 	485.92,	-4.21);
            Stock stock46 = new Stock(46L, "INTC", "Intel Corporation", 	74.23,	0.15);

            FriendGroup group = new FriendGroup("StockGroup1", user2.getId());
            group.addUser(user2);
            user2.setPrivilege('g');


            if(friendGroupRepository.findBygroupName("StockGroup1") == null){
                friendGroupRepository.save(group);
            }

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            stockRepository.save(stock1);
            stockRepository.save(stock2);
            stockRepository.save(stock3);
            stockRepository.save(stock4);
            stockRepository.save(stock5);
            stockRepository.save(stock6);
            stockRepository.save(stock7);
            stockRepository.save(stock8);
            stockRepository.save(stock9);
            stockRepository.save(stock10);
            stockRepository.save(stock11);
            stockRepository.save(stock12);
            stockRepository.save(stock13);
            stockRepository.save(stock14);
            stockRepository.save(stock15);
            stockRepository.save(stock16);
            stockRepository.save(stock17);
            stockRepository.save(stock18);
            stockRepository.save(stock19);
            stockRepository.save(stock20);
            stockRepository.save(stock21);
            stockRepository.save(stock22);
            stockRepository.save(stock23);
            stockRepository.save(stock24);
            stockRepository.save(stock25);
            stockRepository.save(stock26);
            stockRepository.save(stock27);
            stockRepository.save(stock28);
            stockRepository.save(stock29);
            stockRepository.save(stock30);
            stockRepository.save(stock31);
            stockRepository.save(stock32);
            stockRepository.save(stock33);
            stockRepository.save(stock34);
            stockRepository.save(stock35);
            stockRepository.save(stock36);
            stockRepository.save(stock37);
            stockRepository.save(stock38);
            stockRepository.save(stock39);
            stockRepository.save(stock40);
            stockRepository.save(stock41);
            stockRepository.save(stock42);
            stockRepository.save(stock43);
            stockRepository.save(stock44);
            stockRepository.save(stock45);
            stockRepository.save(stock46);




            user1.setStock(stock1, 2, stock1.getId());
            stock1.setUser(user1, 2, stock1.getId());
            user2.setStock(stock2, 3, stock2.getId());
            stock2.setUser(user2, 3, stock2.getId());
            user3.setStock(stock3, 1, stock3.getId());
            stock3.setUser(user3, 1, stock3.getId());
            user4.setStock(stock4, 4, stock4.getId());
            stock4.setUser(user4, 4, stock4.getId());
            spRepository.save(user1.getStocks().get(0));
            spRepository.save(user2.getStocks().get(0));
            spRepository.save(user3.getStocks().get(0));
            spRepository.save(user4.getStocks().get(0));
//            FriendGroup group = new FriendGroup();
//            group.setGroupLeader(user2);
//            user2.setPrivilege('g');
//            group.setGroupName("name");
//            user2.setFriendGroup(group);
//            //userRepository.save(user2);
//            friendGroupRepository.save(group);

        };
    }
}