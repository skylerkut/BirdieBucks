package coms309.Users;

import java.util.List;

import coms309.Stocks.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "UserController", description = "REST APIs for the user/admin entity")
@RestController
public class UserController {

    long purchaseNum = 5;

    int friendGroupNum = 2;

    long userNum = 5;
    long stockNum = 5;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StockRepository stockRepository;


    @Autowired
    FriendGroupRepository friendGroupRepository;

    StockUpdater stockAPI = new StockUpdater();

    @Autowired
    StockPurchasedRepository stockPurchasedRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Gets a list of all the users in the userRepository", response = Iterable.class, tags = "user")
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @ApiOperation(value = "Gets the list of purchased stocks for a specific user", response = Iterable.class, tags = "user")
    @GetMapping(path = "/user/{id}")
    List<StockPurchased> getAllStocksForUser(@PathVariable long id){return userRepository.findById(id).getStocks();}

    @ApiOperation(value = "Gets the user and their information by their id", response = User.class, tags = "user")
    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable long id){
        return userRepository.findById(id);
    }
    @ApiOperation(value = "Get user response from their username {username}", response = User.class, tags = "user")
    @GetMapping(path = "/userByName/{username}")
    User getUserByUsername(@PathVariable String username){return userRepository.findByUsername(username);}

    @ApiOperation(value = "Has the user purchase a stock, will show the money spent from the transaction", response = double.class, tags = "userbuyandsell")
    @GetMapping(path = "/buy/{id}/user/{uid}/amt/{amount}")
    double purchaseStock(@PathVariable long id, @PathVariable long uid, @PathVariable int amount){
        if(userRepository.findById(uid).getPrivilege() == 'b') return -1;
        Stock stock = stockRepository.findById(id);
        int countBefore = userRepository.findById(uid).getStocks().size();
        StockPurchased potentiallyRemove = userRepository.findById(uid).purchase(amount, stock, purchaseNum);
        ++purchaseNum;
        if(countBefore != userRepository.findById(uid).getStocks().size()){
            stockPurchasedRepository.save(userRepository.findById(uid).getStocks().get(userRepository.findById(uid).getStocks().size() - 1));
        }else{
            modifySPRepoPurchase(uid);
            stockPurchasedRepository.delete(potentiallyRemove);
        }
        return stock.getCurrValue() * amount;
    }

    private void modifySPRepoPurchase(long uid){
        int foundidx = -1;
        for(long i = 0; i < stockPurchasedRepository.count(); ++i){
            for(int j = 0; j < userRepository.findById(uid).getStocks().size(); ++j){
                if(stockPurchasedRepository.findById(i).getStock().equals(userRepository.findById(uid).getStocks().get(j).getStock()) && stockPurchasedRepository.findById(i).getUser().equals(userRepository.findById(uid))){
                    foundidx = j;
                    break;
                }
            }
            if(foundidx != -1){
                break;
            }
        }
        stockPurchasedRepository.save(userRepository.findById(uid).getStocks().get(foundidx));
    }

    @ApiOperation(value = "Get list of all friend group objects", response = Iterable.class, tags = "friendgroup")
    @GetMapping(path = "/friendgroup")
    List<FriendGroup> getFriendGroups(){
        return friendGroupRepository.findAll();
    }

    //gets list of people in the friend group

    @ApiOperation(value = "create list of users in friend group {groupName}", response = Iterable.class, tags = "friendgroup")
    @GetMapping(path = "/friendgroup/{groupName}")
    List<User> getGroupMembers(@PathVariable String groupName){
        return friendGroupRepository.findBygroupName(groupName).getGroupMembers();
    }


    @ApiOperation(value = "Has the user sell a stock, returns their money that they earned from the transaction", response = double.class, tags = "userbuyandsell")
    @GetMapping(path = "/sell/{id}/user/{uid}/{numStocks}")
    double sellStock(@PathVariable long id, @PathVariable long uid, @PathVariable int numStocks){
        Stock stock = stockRepository.findById(id);
        if(userRepository.findById(uid).getPrivilege() == 'b' || userRepository.findById(uid).getStocks().contains(stock)) return -1;
        int currLength = userRepository.findById(uid).getStocks().size();
        StockPurchased changed = userRepository.findById(uid).sell(numStocks, stock);
        int soldStocks = changed.getNumPurchased();
        if(currLength != userRepository.findById(uid).getStocks().size()){
            stockPurchasedRepository.delete(changed);
        }else{
            StockPurchased sp = modifySPRepoSell(uid);
            soldStocks -= sp.getNumPurchased();
            stockPurchasedRepository.delete(changed);
        }
        return stock.getCurrValue() * soldStocks;
    }

    private StockPurchased modifySPRepoSell(long uid){
        int foundidx = -1;
        for(long i = 0; i < stockPurchasedRepository.count(); ++i){
            for(int j = 0; j < userRepository.findById(uid).getStocks().size() && stockPurchasedRepository.findById(i).getUser().getId().equals(uid) && stockPurchasedRepository.findById(i).getStock().equals(userRepository.findById(uid).getStocks().get(j).getStock()); ++j){
                if(stockPurchasedRepository.findById(i).getNumPurchased() != userRepository.findById(uid).getStocks().get(j).getNumPurchased()){
                    foundidx = j;
                    break;
                }
            }
            if(foundidx != -1){
                break;
            }
        }
        if(foundidx != -1){
            return stockPurchasedRepository.save(userRepository.findById(uid).getStocks().get(foundidx));
        }
        return null;
    }


    @ApiOperation(value = "create new user based on request body", response = String.class, tags = "user")
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        //if there is nobody or the username already exists
        if (user == null)
            return "no user input";
        user.setId(userNum);
        if (userRepository.getOne(user.getId()).getUsername() !=null)
            return "username already taken";
        userRepository.save(user);
        ++userNum;
        return success;
    }

    @ApiOperation(value = "create a new login request with the request body", response = LoginAttempt.class, tags = "user")
    @PostMapping(path = "/login")
    LoginAttempt login(@RequestBody LoginAttempt login){
        User user = userRepository.findByUsername(login.getUsername());
        if(user.getPrivilege() == 'b') return null;
        if (user != null) {
            if (user.getPassword().equals(login.getPassword())) {
                login.setSuccess("Success!");
            } else {
                login.setSuccess("Wrong Password");
            }
        } else {
            login.setSuccess("Username Not Found");
        }
        return login;
    }
    //creates a new friend group using the name in the requestbody
    @ApiOperation(value = "create new friend group named {groupName}", response = String.class, tags = "friendgroup")
    @PostMapping(path = "/friendgroup/{groupName}/{uid}")
    String createFriendGroup(@PathVariable String groupName, @PathVariable long uid){
        if(friendGroupRepository.findBygroupName(groupName) == null) {
            FriendGroup friendGroup = new FriendGroup();
            User groupLeader = userRepository.getOne(uid);
            groupLeader.setPrivilege('g');
            ++friendGroupNum;
            friendGroup.setGroupName(groupName);
            friendGroup.setGroupLeader(groupLeader);
            groupLeader.setFriendGroup(friendGroup);
            friendGroup.addUser(groupLeader);
            //userRepository.save(groupLeader);
            friendGroupRepository.save(friendGroup);
            return success;
        }
        return failure;
    }

    @GetMapping(path = "/friendgroup/get/{uid}")
    FriendGroup getFriendGroup(@PathVariable long uid){
        return userRepository.getOne(uid).getFriendGroup();
    }


//
//    //creates a new friend group using the name in the requestbody and adds the user from the path variable into the group
//    @PostMapping(path = "/friendgroup/{userID}")
//    String createFriendGroup(@RequestBody String name, @PathVariable int userID){
//        FriendGroup friendGroup = new FriendGroup();
//        friendGroup.setGroupName(name);
//        friendGroup.addUser(userRepository.findById(userID));
//        friendGroupRepository.save(friendGroup);
//        return success;
//    }

    //adds user userID to FriendGroup groupName

    @GetMapping(path = "/friendgroup/getall/{groupName}")
    List<User> getUsersFromGroup(@PathVariable String groupName){
        FriendGroup group = friendGroupRepository.findBygroupName(groupName);
        return group.getGroupMembers();
    }

    @ApiOperation(value = "Add User {userId} to friend group {groupName}", response = String.class, tags = "friendgroup")
    @PutMapping(path = "/friendgroup/{groupName}/{userID}/{gid}")
    @Transactional
    public String addUserToGroup(@PathVariable String groupName, @PathVariable long userID, @PathVariable long gid) {
        FriendGroup group = friendGroupRepository.findBygroupName(groupName);
        User user = userRepository.findById(gid);

        if (group != null && user != null && user.getPrivilege() == 'g' && group.getGroupLeader() == gid) {
            User addedUser = userRepository.findById(userID);
            addedUser.setFriendGroup(group);
            group.addUser(addedUser);
            userRepository.save(addedUser);

            // Save the friend group to update the user-group relationship
            friendGroupRepository.save(group);

            return success;
        } else {
            return failure;
        }
    }

    @PutMapping(path = "/friendgroup/remove/{gname}/{gid}/{uid}")
    @Transactional
    public String removeUserFromGroup(@PathVariable String gname, @PathVariable long gid, @PathVariable long uid){
        FriendGroup group = friendGroupRepository.findBygroupName(gname);
        User user = userRepository.findById(gid);

        if(group != null && user != null && user.getPrivilege() == 'g' && group.getGroupLeader() == gid){
            group.removeUser(userRepository.getOne(uid));
            userRepository.getOne(uid).setFriendGroup(null);
            friendGroupRepository.save(group);
            return success;
        }else{
            return failure;
        }
    }

    @PutMapping(path = "/friendgroup/setnewleader/{gname}/{gid}/{uid}")
    String setNewLeader(@PathVariable String gname, @PathVariable long gid, @PathVariable long uid){
        FriendGroup group = friendGroupRepository.findBygroupName(gname);
        User currLeader = userRepository.findById(gid);
        User pnewLeader = userRepository.findById(uid);
        if(currLeader.getPrivilege() == 'g' && group.getGroupLeader() == gid && pnewLeader.getFriendGroup() != null && pnewLeader.getFriendGroup().getGroupLeader() == gid){
            pnewLeader.setPrivilege('g');
            group.setGroupLeader(pnewLeader);
            currLeader.setPrivilege('u');
            friendGroupRepository.save(group);
            userRepository.save(currLeader);
            userRepository.save(pnewLeader);
            return success;
        }else{
            return failure;
        }
    }


    @ApiOperation(value = "Update the user {id} with information from request body", response = User.class, tags = "user")
    @PutMapping(path = "/users/{id}")
    User updateUser(@PathVariable long id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user.getPrivilege() == 'b') return null;
        userRepository.save(request);
        return userRepository.findById(id);
    }

//    @ApiOperation(value = "Assigns a stock to the user, essentially having them purchase it just a little different", response = String.class, tags = "userbuyandsell")
//    @PutMapping(path = "/users/{userId}/stocks/{stockId}/{numPurchasing}")
//    String assignStockToUser(@PathVariable long userId, @PathVariable long stockId, @PathVariable int numPurchasing){
//        User user = userRepository.findById(userId);
//        if(user.getPrivilege() == 'b') return failure;
//        Stock stock = stockRepository.findById(stockId);
//        stock.setUser(user, numPurchasing, userId);
//        user.setStock(stock, numPurchasing, stockId);
//        userRepository.save(user);
//        stockRepository.save(stock);
//        return success;
//    }

    @ApiOperation(value = "Remove User {id}", response = String.class, tags = "user")
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable Long id){
        userRepository.findById(id);
        return success;
    }


    //removes user userID from FriendGroup groupName

    @ApiOperation(value = "Remove User {userId} from friend group {groupName}", response = String.class, tags = "friendgroup")
    @DeleteMapping(path = "/friendgroup/{groupName}/{userID}")
    String removeUserFromGroup(@PathVariable String groupName, @PathVariable long userID){
        if(userRepository.getOne(userID).getPrivilege() == 'g') return failure;
        friendGroupRepository.findBygroupName(groupName).removeUser(userRepository.findById(userID));
        friendGroupRepository.save(friendGroupRepository.findBygroupName(groupName));
        return success;
    }

    @ApiOperation(value = "Creates a new stock based on a stock json object, admins have permissions to do this", response = String.class, tags = "stock")
    @PostMapping(path = "/newstocks/{uid}")
    String createStock(@RequestBody Stock stock, @PathVariable long uid){
        User user = userRepository.findById(uid);
        if(user.getPrivilege() != 'a' || stock == null) return failure;
        stock.setId(stockNum);
        ++stockNum;
        stockRepository.save(stock);
        return success;
    }

    @ApiOperation(value = "Deletes a stock based on its id, if the admin wants to have that stock deleted", response = String.class, tags = "stock")
    @DeleteMapping(path = "/stocks/{sid}/{uid}")
    String deleteStock(@PathVariable long sid, @PathVariable long uid){
        User user = userRepository.findById(uid);
        if(user.getPrivilege() != 'a') return failure;
        stockRepository.deleteById(sid);
        return success;
    }

    @ApiOperation(value = "Updates all the stocks if the admin user calls upon the stocks to be updated", response = String.class, tags = "stock")
    @PutMapping(path = "/update/{uid}")
    String updateAllStocks(@PathVariable long uid){
        if(userRepository.findById(uid).getPrivilege() != 'a') return failure;
        stockAPI.updateAllStocks(stockRepository);
        return success;
    }

    @ApiOperation(value = "Bans a user based on their uid, but ensures that this is being done by an admin with aid", response = String.class, tags = "user")
    @PutMapping(path = "/banuser/{uid}/byadmin/{aid}")
    String banUser(@PathVariable long uid, @PathVariable long aid){
        User admin = userRepository.findById(aid);
        if(admin.getPrivilege() != 'a') return failure;
        User user = userRepository.findById(uid);
        user.setPrivilege('b');
        if(!user.getStocks().isEmpty()){
            removeStocks(uid);
        }
        userRepository.save(user);
        return success;
    }

    private void removeStocks(long uid){
        for(long i = 1; i < stockPurchasedRepository.count(); ++i){
            for(int j = 0; j < userRepository.getOne(uid).getStocks().size(); ++j){
                if(stockPurchasedRepository.findById(i).getUser().getId().equals(userRepository.findById(uid).getId())){
                    stockPurchasedRepository.deleteById(i);
                    userRepository.findById(uid).getStocks().remove(j);
                    break;
                }
            }
        }
    }


    @ApiOperation(value = "Unbans the user with the uid, but must be done by an admin called by aid", response = String.class, tags = "user")
    @PutMapping(path = "/unban/{uid}/byadmin/{aid}")
    String unbanUser(@PathVariable long uid, @PathVariable long aid){
        User admin = userRepository.findById(aid);
        if(admin.getPrivilege() != 'a') return failure;
        User user = userRepository.findById(uid);
        user.setPrivilege('u');
        userRepository.save(user);
        return success;
    }

}
