package comg.org.syncron;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * Created by srikanthg on 10/01/17.
 */
public class RestApi {

	List<Map<String, String>> ls = new ArrayList();
	List<Map<String, String>> elements = new ArrayList();

	@Test(priority = 1)
	public void testGetAPI() {
		Response res = RestAssured.given().get(
				"https://sheetsu.com/apis/v1.0/da604a7c496a");
		Assert.assertEquals(res.getStatusCode(), 200);
		JSONArray jsonArray = new JSONArray(res.body().asString());
		Map<String, String> data = new LinkedHashMap<String, String>();
		Map<String, String> data1 = new LinkedHashMap<String, String>();
		data.put("name", "siva");
		data1.put("name", "Glenn");
		data.put("id", "1");
		data1.put("id", "6");
		data.put("score", "100");
		data1.put("score", "69");
		ls.add(data);
		ls.add(data1);

		for (int i = 0; i < jsonArray.length(); i++) {

			Assert.assertEquals(jsonArray.getJSONObject(i).getString("name")
					.toLowerCase(), ls.get(i).get("name").toLowerCase());
			Assert.assertEquals(jsonArray.getJSONObject(i).getString("score"),
					ls.get(i).get("score"));
			Assert.assertEquals(jsonArray.getJSONObject(i).getString("id"), ls
					.get(i).get("id"));
		}
	}

	@Test(priority = 2)
	public void testGetRowAPI() {
		Response res1 = RestAssured.given().param("fields", "name")
				.get("https://sheetsu.com/apis/v1.0/da604a7c496a");
		Assert.assertEquals(res1.getStatusCode(), 200);
		JSONArray jsonArray1 = new JSONArray(res1.body().asString());
		for (int i = 0; i < jsonArray1.length(); i++) {
			Assert.assertEquals(jsonArray1.getJSONObject(i).keySet().toString()
					.replace("[", "").replace("]", ""), "name");
		}
	}

	@Test(priority = 3)
	public void testGetColumnAPI() {

		Response res2 = RestAssured.given().get(
				"https://sheetsu.com/apis/v1.0/da604a7c496a/{id}/{value}",
				"id", "1");

		Assert.assertEquals(
				res2.body().jsonPath().getString("id").replace("[", "")
						.replace("]", ""), "1");
	}

	@Test(priority = 0)
	public void testPostAPI() {
		Map<String, String> da = new LinkedHashMap<String, String>();

		da.put("name", "anil");

		da.put("id", "8");

		da.put("score", "100");

		elements.add(da);

		Response res3 = RestAssured.given().contentType(ContentType.JSON)
				.body(da).post("https://sheetsu.com/apis/v1.0/da604a7c496a");
		JSONArray jsonArray2 = new JSONArray(res3.asString());

		for (int i = 0; i < jsonArray2.length(); i++) {

			Assert.assertEquals(jsonArray2.getJSONObject(i).getString("name")
					.toLowerCase(), elements.get(i).get("name").toLowerCase());
			Assert.assertEquals(jsonArray2.getJSONObject(i).getString("score"),
					elements.get(i).get("score"));
			Assert.assertEquals(jsonArray2.getJSONObject(i).getString("id"), elements
					.get(i).get("id"));
		}

	}

	@Test(priority = 4)
	public void testDeleteAPI() {

		Response res4 = RestAssured.given().delete(
				"https://sheetsu.com/apis/v1.0/da604a7c496a/{id}/{value}",
				"id", "8");
		Assert.assertEquals(res4.getStatusCode(), 204);
		JSONArray jsonArray = new JSONArray(res4.body().asString());

		for (int i = 0; i < jsonArray.length(); i++) {

			Assert.assertNotEquals(jsonArray.getJSONObject(i).getString("name")
					.toLowerCase(), ls.get(i).get("name").toLowerCase());
			Assert.assertNotEquals(jsonArray.getJSONObject(i)
					.getString("score"), ls.get(i).get("score"));
			Assert.assertNotEquals(jsonArray.getJSONObject(i).getString("id"),
					ls.get(i).get("id"));
		}

	}

}
