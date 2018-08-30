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
import com.tcs.casestudy1.entity.Vendor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VendorTests {

	@Autowired
	private WebApplicationContext context;
	private Vendor vendor;
	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void getVendorTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/vendor").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$[0].vendorId").isNumber()).andExpect(jsonPath("$[0].vendorName").isString());

		mvc.perform(MockMvcRequestBuilders.delete("/vendor/").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(MockMvcRequestBuilders.get("/vendor").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void addVendorTest() throws Exception {
		vendor = new Vendor(0l, "Amit", 9400l, "abc@gmail.com", "abc", "Kolkata");
		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/vendor").content(toJson(vendor))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.vendorId").isNumber()).andReturn();
		JSONObject json = new JSONObject(result.getResponse().getContentAsString());
		mvc.perform(
				MockMvcRequestBuilders.get("/vendor/" + json.get("vendorId")).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$.vendorId").value(json.get("vendorId")))
				.andExpect(jsonPath("$.vendorName").value("Amit"));
	}

	@Test
	public void deleteVendorTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/vendor/0").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

		// add a vendor
		vendor = new Vendor(0l, "Amit", 9400l, "abc@gmail.com", "abc", "Kolkata");

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/vendor").content(toJson(vendor))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.vendorId").isNumber()).andReturn();

		// delete the vendor
		JSONObject json = new JSONObject(result.getResponse().getContentAsString());
		long vendorId = (Integer) json.get("vendorId");

		mvc.perform(MockMvcRequestBuilders.delete("/vendor/" + vendorId)
				// .content(toJson(customer))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// confirm deletion
		mvc.perform(MockMvcRequestBuilders.get("/customer/" + vendorId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deleteAllVendorsTest() throws Exception {
		mvc.perform(MockMvcRequestBuilders.delete("/vendor/").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(MockMvcRequestBuilders.get("/vendor").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void updateVendorTest() throws Exception {
		// add a customer
		vendor = new Vendor(0l, "Amit", 9400l, "abc@gmail.com", "abc", "Kolkata");

		mvc.perform(MockMvcRequestBuilders.put("/vendor/0").content(toJson(vendor))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		MvcResult result = mvc
				.perform(MockMvcRequestBuilders.post("/vendor").content(toJson(vendor))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.vendorId").isNumber()).andReturn();

		// update the vendor
		JSONObject json = new JSONObject(result.getResponse().getContentAsString());
		long vendorId = (Integer) json.get("vendorId");
		vendor.setVendorId(vendorId);
		vendor.setVendorContactNumber(9401l);

		mvc.perform(MockMvcRequestBuilders.put("/vendor/" + vendorId).content(toJson(vendor))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// confirm Update
		mvc.perform(MockMvcRequestBuilders.get("/vendor/" + vendorId).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty())
				.andExpect(jsonPath("$.vendorId").value(json.get("vendorId")))
				.andExpect(jsonPath("$.vendorContactNumber").value("9401"));
	}

	private byte[] toJson(Object r) throws Exception {
		ObjectMapper map = new ObjectMapper();
		return map.writeValueAsString(r).getBytes();
	}

}