package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.OrderController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		SareetaApplication.class,
})
public class SareetaApplicationTests {

	@Autowired
	private UserController userController;

	@Autowired
	private ItemController itemController;

	@Autowired
	private OrderController orderController;

	@Autowired
	private CartController cartController;

	@Test
	public void contextLoads() {
	}

	@Test
	public void createUser(){
		ResponseEntity<User> response =  userController.createUser(createUserRequest("User1"));
		Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
		Assert.assertEquals(response.getBody().getUsername(),"User1");
	}

	@Test
	public void createUserWithBadPassword(){
		CreateUserRequest userRequestWithBadPassword = createUserRequest("User1");
		userRequestWithBadPassword.setPassword("123");
		userRequestWithBadPassword.setConfirmPassword("pwd");
		ResponseEntity<User> response =  userController.createUser(userRequestWithBadPassword);
		Assert.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
	}

	@Test
	public void findUserByName(){
		createUser();
		ResponseEntity<User> response = userController.findByUserName("User1");
		Assert.assertEquals(HttpStatus.OK,response.getStatusCode());
	}

	@Test
	public void findUserById(){
		ResponseEntity<User> createUserResponse =  userController.createUser(createUserRequest("User1"));
		Long userId = createUserResponse.getBody().getId();
		ResponseEntity<User> findUserResponse = userController.findById(userId);
		Assert.assertEquals(HttpStatus.OK,findUserResponse.getStatusCode());
	}

	@Test
	public void getItems(){
		ResponseEntity<List<Item>> responseGetItems = itemController.getItems();
		String[] items = responseGetItems.getBody().stream()
						.map(i->i.getName())
						.toArray(String[]::new);
		Assert.assertArrayEquals(items,new String[]{"Round Widget","Square Widget"});
	}

	@Test
	public void getItemById(){
		ResponseEntity<Item> response = itemController.getItemById(1L);
		Assert.assertEquals(response.getBody().getName(),"Round Widget");
	}

	@Test
	public void getItemByName(){
		ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");
		Assert.assertTrue(response.getBody().get(0).getId() == 1L);
	}

	@Test
	public void addToCartToExistingUser(){
		createUser();
		ResponseEntity<Cart> responseAddToCart = cartController.addTocart(modifyCartRequest("User1",1L,5));
		Assert.assertEquals(HttpStatus.OK, responseAddToCart.getStatusCode());
	}

	@Test
	public void addToCartToNonExistingUser(){
		createUserRequest("User1");
		ResponseEntity<Cart> responseAddToCart = cartController.addTocart(modifyCartRequest("NullUser",1L,5));
		Assert.assertEquals(HttpStatus.NOT_FOUND, responseAddToCart.getStatusCode());
	}

	@Test
	public void addToCartCheckTotalPrice(){
		createUser();
		ResponseEntity<Cart> responseAddToCart = cartController.addTocart(modifyCartRequest("User1",1L,5));
		Assert.assertEquals(HttpStatus.OK, responseAddToCart.getStatusCode());
		Assert.assertEquals(BigDecimal.valueOf(14.95),responseAddToCart.getBody().getTotal());
	}

	@Test
	public void remove_TwoItems_FromSixItems_InCart(){
		createUser();
		cartController.addTocart(modifyCartRequest("User1",1L,6));
		ResponseEntity<Cart> responseCart = cartController.removeFromcart(modifyCartRequest("User1",1L,2));
		Assert.assertEquals(4,responseCart.getBody().getItems().size());
	}

	@Test
	public void checkOrderForNonExistingUser(){
		ResponseEntity<List<UserOrder>> responseUserOrders = orderController.getOrdersForUser("User1");
		Assert.assertEquals(HttpStatus.NOT_FOUND,responseUserOrders.getStatusCode());
	}

	private static CreateUserRequest createUserRequest(String username){
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setUsername(username);
		createUserRequest.setPassword("password");
		createUserRequest.setConfirmPassword("password");
		return createUserRequest;
	}

	private static ModifyCartRequest modifyCartRequest(String username, Long itemId, int quantity){
		ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
		modifyCartRequest.setUsername(username);
		modifyCartRequest.setItemId(itemId);
		modifyCartRequest.setQuantity(quantity);
		return modifyCartRequest;
	}
}
