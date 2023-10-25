package registration.validator;

import api.model.UserDTO;
import api.model.PostDTO;
import com.google.gson.Gson;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import util.PostUtil;
import util.UserUtil;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class RegistrationValidator {

    String jsonBody;
    String ct = "application/json";
    String uri = "https://dummyapi.io/data/v1/";
    String userId;
    Response resposta;
    String postId;

    UserDTO user = new UserDTO();
    PostDTO post = new PostDTO();

    Gson gson = new Gson();

    @Test(priority = 1)
    public void testCreateUser(ITestContext context) {

        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        String randomString = String.valueOf(randomNumber);

        user.firstName = "Bruno";
        user.lastName = "Foster";
        user.email = "bruno.foster" + randomString + "@gmail.com";

        jsonBody = gson.toJson(user);

        resposta = (Response) given()
                .contentType(ct)
                .header("app-id", "65367e4e847ea2f6252fda5f")
                .log().all()
                .body(jsonBody)
                .when()
                .post(uri + "user/create")
                .then()
                .log().all()
                .statusCode(200)
                .body("firstName", is(user.firstName))
                .body("lastName", is(user.lastName))
                .body("email", is(user.email))
                .extract();


        user.id = resposta.jsonPath().getString("id");
        userId = resposta.jsonPath().getString("id");

        String registerDate = resposta.jsonPath().getString("registerDate");
        String updatedDate = resposta.jsonPath().getString("updatedDate");
        context.setAttribute("userID", userId);
        System.out.println("UserID: " + userId);

        Assert.assertTrue(UserUtil.isValidId((userId)));
        Assert.assertTrue(UserUtil.isValidDate((registerDate)));
        Assert.assertTrue(UserUtil.isValidDate((updatedDate)));
    }

    @Test(priority = 2)
    public void testGetUser(){

        jsonBody = gson.toJson(user);

        resposta = (Response) given()
                .contentType(ct)
                .header("app-id", "65367e4e847ea2f6252fda5f")
                .log().all()
                .body(jsonBody)
                .when()
                .get(uri + "user/" + userId)
                .then()
                .log().all()
                .statusCode(200)
                .extract();

        JSONObject apiResponse;
        apiResponse = new JSONObject(resposta.asString());

        Assert.assertEquals(apiResponse.get("id"), user.id);
        Assert.assertEquals(apiResponse.get("firstName"), user.firstName);
        Assert.assertEquals(apiResponse.get("lastName"), user.lastName);
        Assert.assertEquals(apiResponse.get("email"), user.email);
    }

    @Test(priority = 3)
    public void testCreatePost(ITestContext context) {

        post.text = "Lorem ipsum dolor sit amet, consectetur adipisci elit.";
        post.image = "imagetest.jpeg";
        post.likes = 2;
        post.link = "testpost.com.br";
        post.tags = new String[]{"tag1", "tag2", "tag3"};
        post.publishDate = "2023-10-23T15:55:23.771Z";
        post.owner = userId;

        jsonBody = gson.toJson(post);

        resposta = (Response) given()
                .contentType(ct)
                .header("app-id", "65367e4e847ea2f6252fda5f")
                .log().all()
                .body(jsonBody)
                .when()
                .post(uri + "post/create")
                .then()
                .log().all()
                .statusCode(200)
                .body("image", is(post.image))
                .body("likes", is(post.likes))
                .body("link", is(post.link))
                .body("text", is(post.text))
                .extract();

        JSONObject apiResponse = new JSONObject(resposta.asString());

        JSONArray responseTags = new JSONArray(apiResponse.getJSONArray("tags"));
        Assert.assertEquals(post.tags, PostUtil.jsonArrayToStringArray(responseTags));

        Assert.assertEquals(user.firstName, apiResponse.getJSONObject("owner").getString("firstName"));
        Assert.assertEquals(user.lastName, apiResponse.getJSONObject("owner").getString("lastName"));
        Assert.assertEquals(user.id, apiResponse.getJSONObject("owner").getString("id"));

        String publishDate = resposta.jsonPath().getString("publishDate");
        String updatedDate = resposta.jsonPath().getString("updatedDate");

        post.id = resposta.jsonPath().getString("id");
        postId = resposta.jsonPath().getString("id");
        context.setAttribute("postId", postId);
        System.out.println("PostID extraido: " + postId);

        Assert.assertTrue(UserUtil.isValidId((postId)));
        Assert.assertTrue(UserUtil.isValidDate((publishDate)));
        Assert.assertTrue(UserUtil.isValidDate((updatedDate)));
    }

    @Test(priority = 4)
    public void testGetPost(){

        jsonBody = gson.toJson(user);

        resposta = (Response) given()
                .contentType(ct)
                .header("app-id", "65367e4e847ea2f6252fda5f")
                .log().all()
                .body(jsonBody)
                .when()
                .get(uri + "/post/" + postId)
                .then()
                .log().all()
                .statusCode(200)
                .extract();

        JSONObject apiResponse;
        apiResponse = new JSONObject(resposta.asString());

        Assert.assertEquals(apiResponse.get("id"), post.id);
        Assert.assertEquals(apiResponse.get("text"), post.text);

        Assert.assertEquals(user.firstName, apiResponse.getJSONObject("owner").getString("firstName"));
        Assert.assertEquals(user.lastName, apiResponse.getJSONObject("owner").getString("lastName"));
        Assert.assertEquals(user.id, apiResponse.getJSONObject("owner").getString("id"));
    }

    @Test(priority = 5)
    public void testDeletePost() {

        given()
                .log().all()
                .contentType(ct)
                .header("app-id", "65367e4e847ea2f6252fda5f")
                .when()
                .delete("https://dummyapi.io/data/v1/post/" + postId)
                .then()
                .log().all()
                .statusCode(200)
                .extract();

        JSONObject apiResponse;
        apiResponse = new JSONObject(resposta.asString());

        Assert.assertEquals(apiResponse.get("id"), post.id);
    }

    @Test(priority = 6)
    public void testGetPostNotFound() {

        jsonBody = gson.toJson(user);

        resposta = (Response) given()
                .contentType(ct)
                .header("app-id", "65367e4e847ea2f6252fda5f")
                .log().all()
                .body(jsonBody)
                .when()
                .get(uri + "/post/" + postId)
                .then()
                .log().all()
                .statusCode(404)
                .extract();

        JSONObject apiResponse;
        apiResponse = new JSONObject(resposta.asString());

        Assert.assertEquals(apiResponse.get("error"), "RESOURCE_NOT_FOUND");
    }

    @Test(priority = 7)
    public void testDeleteUser() {

        resposta = (Response) given()
                .log().all()
                .contentType(ct)
                .header("app-id", "65367e4e847ea2f6252fda5f")
                .when()
                .delete("https://dummyapi.io/data/v1/user/" + userId)
                .then()
                .log().all()
                .statusCode(200)
                .extract();

        JSONObject apiResponse;
        apiResponse = new JSONObject(resposta.asString());

        Assert.assertEquals(apiResponse.get("id"), user.id);
    }

    @Test(priority = 8)
    public void testGetUserNotFound() {

        jsonBody = gson.toJson(user);

        resposta = (Response) given()
                .contentType(ct)
                .header("app-id", "65367e4e847ea2f6252fda5f")
                .log().all()
                .body(jsonBody)
                .when()
                .get(uri + "/user/" + userId)
                .then()
                .log().all()
                .statusCode(404)
                .extract();

        JSONObject apiResponse;
        apiResponse = new JSONObject(resposta.asString());

        Assert.assertEquals(apiResponse.get("error"), "RESOURCE_NOT_FOUND");
    }
}

