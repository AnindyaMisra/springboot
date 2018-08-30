package com.tcs.casestudy1;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.casestudy1.entity.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerTests {

	@Autowired
	private WebApplicationContext context;
	private Customer customer;
	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void getCustomerTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/customer").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0].customerId").isNumber()).andExpect(jsonPath("$[0].customerName").isString());

		mvc.perform(MockMvcRequestBuilders.delete("/customer/").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(MockMvcRequestBuilders.get("/customer").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void addCustomerTest() throws Exception {
		customer = new Customer(0l, "Amit", "9400", "Kolkata", "Male");
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/customer").content(toJson(customer))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.customerId").isNumber()).andReturn();
		JSONObject json = new JSONObject(result.getResponse().getContentAsString());
		mvc.perform(
				MockMvcRequestBuilders.get("/customer/" + json.get("customerId")).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$.customerId").value(json.get("customerId")))
				.andExpect(jsonPath("$.customerName").value("Amit"));
	}

	@Test
	public void deleteCustomerTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/customer/0").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

		// add a customer
		customer = new Customer(0l, "Amit", "9400", "KOlkata", "Male");

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/customer").content(toJson(customer))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.customerId").isNumber()).andReturn();

		// delete the customer
		JSONObject json = new JSONObject(result.getResponse().getContentAsString());
		long customerId = (Integer) json.get("customerId");

		mvc.perform(MockMvcRequestBuilders.delete("/customer/" + customerId)
				// .content(toJson(customer))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// confirm deletion
		mvc.perform(MockMvcRequestBuilders.get("/customer/" + customerId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteAllCustomersTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/customer/").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(MockMvcRequestBuilders.get("/customer").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateCustomerTest() throws Exception {
		// add a customer
		customer = new Customer(0l, "Amit", "9400", "KOlkata", "Male");

		mvc.perform(MockMvcRequestBuilders.put("/customer/0").content(toJson(customer))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/customer").content(toJson(customer))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.customerId").isNumber()).andReturn();

		// update the customer
		JSONObject json = new JSONObject(result.getResponse().getContentAsString());
		long customerId = (Integer) json.get("customerId");
		customer.setCustomerId(customerId);
		customer.setContactNumber("9401");

		mvc.perform(MockMvcRequestBuilders.put("/customer/" + customerId).content(toJson(customer))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// confirm Update
		mvc.perform(MockMvcRequestBuilders.get("/customer/" + customerId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$.customerId").value(json.get("customerId")))
				.andExpect(jsonPath("$.contactNumber").value("9401"));
	}

	private byte[] toJson(Object r) throws Exception {
		ObjectMapper map = new ObjectMapper();
		return map.writeValueAsString(r).getBytes();
	}

}