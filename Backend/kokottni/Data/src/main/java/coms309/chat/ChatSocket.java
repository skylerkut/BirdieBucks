package coms309.chat;

import coms309.Users.User;
import coms309.Users.UserRepository;
import coms309.Users.UserController;
import coms309.Users.FriendGroup;
import coms309.Users.FriendGroupRepository;


import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.stereotype.Component;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@RestController      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{username}/{target}")  // this is Websocket url
public class ChatSocket {


	@Autowired
	FriendGroupRepository friendGroupRepository;
  // cannot autowire static directly (instead we do it by the below
  // method
	private static MessageRepository msgRepo;

	@Autowired
	private UserRepository userRepository;




	/*
   * Grabs the MessageRepository singleton from the Spring Application
   * Context.  This works because of the @Controller annotation on this
   * class and because the variable is declared as static.
   * There are other ways to set this. However, this approach is
   * easiest.
	 */
	@Autowired
	public void setMessageRepository(MessageRepository repo) {
		msgRepo = repo;  // we are setting the static variable
	}

	// Store all socket session and their corresponding username.
	private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
	private static Map<String, Session> usernameSessionMap = new Hashtable<>();

	private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

	@OnOpen
	public void onOpen(Session session, @PathParam("username") String username) 
      throws IOException {

//		User user = userRepository.findByUsername(username);
//
//		broadcast("hello " + user.getName());

		logger.info("Entered into Open");

    // store connecting user information
		sessionUsernameMap.put(session, username);
		usernameSessionMap.put(username, session);

		//Send chat history to the newly connected user
		//sendMessageToPArticularUser(username, getChatHistory());
		
    // broadcast that new user joined
		String message = "User:" + username + " has Joined the Chat";
		broadcast(message);
	}


	@OnMessage
	public void onMessage(Session session, String message, @PathParam("target") String target) throws IOException {

		// Handle new messages
		logger.info("Entered into Message: Got Message:" + message);


		//FriendGroup friendGroup = friendGroupRepository.findBygroupName(target);

		String username = sessionUsernameMap.get(session);

		String destUsername = message.split(" ")[0].substring(1);

//		if (friendGroup != null) {
//			// Send the message to all users in the friend group
//			List<User> groupMembers = friendGroup.getGroupMembers();
//			for (User user : groupMembers) {
//				sendMessageToPArticularUser(user.getUsername(), message);
//			}
//		} else {
//
//			sendMessageToPArticularUser(target, "[DM] " + username + ": " + message);
//			sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);
//
//		}
// Direct message to a user using the format "@username <message>"
		if (message.startsWith("@")) {


			// send the message to the sender and receiver
			sendMessageToPArticularUser(destUsername, "[DM] " + username + ": " + message);
			sendMessageToPArticularUser(username, "[DM] " + username + ": " + message);

		}
		else { // broadcast
			broadcast(username + ": " + message);
		}


		// Saving chat history to repository
		msgRepo.save(new Message(username, message, target));
	}


	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Entered into Close");

    // remove the user connection information
		String username = sessionUsernameMap.get(session);
		sessionUsernameMap.remove(session);
		usernameSessionMap.remove(username);

    // broadcase that the user disconnected
		String message = username + " disconnected";
		broadcast(message);
	}


	@OnError
	public void onError(Session session, Throwable throwable) {
		// Do error handling here
		logger.info("Entered into Error");
		throwable.printStackTrace();
	}


	private void sendMessageToPArticularUser(String username, String message) {
		Session userSession = usernameSessionMap.get(username);
		if (userSession != null) {
			try {
				userSession.getBasicRemote().sendText(message);
			} catch (IOException e) {
				logger.error("Error sending message to user: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			logger.error("User session not found for username: " + username);
			// You can log an error or take appropriate action here.
		}
	}



	private void broadcast(String message) {
		sessionUsernameMap.forEach((session, username) -> {
			try {
				session.getBasicRemote().sendText(message);
			} 
      catch (IOException e) {
				logger.info("Exception: " + e.getMessage().toString());
				e.printStackTrace();
			}

		});

	}
	

  // Gets the Chat history from the repository
	private String getChatHistory() {
		List<Message> messages = msgRepo.findAll();
    
    // convert the list to a string
		StringBuilder sb = new StringBuilder();
		if(messages != null && messages.size() != 0) {
			for (Message message : messages) {
				sb.append(message.getUserName() + ": " + message.getContent() + "\n");
			}
		}
		return sb.toString();
	}


	@Api(value = "MessageController", description = "REST API related to Web Socket Messages")
	@RestController
	@RequestMapping("/chat/messages")
	public class MessageController {

		@Autowired
		private MessageRepository messageRepository;

		@Autowired
		FriendGroupRepository friendGroupRepository;

		@ApiOperation(value = "Get list of Messages sent to {target} ", response = Iterable.class, tags = "message-controller")
		@GetMapping("/{target}")
		public List<Message> getMessagesByTarget(@PathVariable String target) {
			return messageRepository.findByTarget(target);
		}

		@ApiOperation(value = "Get list of all friend group objects", response = Iterable.class, tags = "friendgroup")
		@GetMapping(path = "/friendgroup")
		List<FriendGroup> getFriendGroups(){
			return friendGroupRepository.findAll();
		}
	}


} // end of Class
