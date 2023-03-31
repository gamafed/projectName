package com.companyName.projectName;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class ObjectMapperTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL) //only non null can show out
    private static class Book {
        private String id;
        private String name;
        private int price;
        @JsonIgnore
        private String isbn;

//        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createdTime;
        @JsonUnwrapped //flat
        private Publisher publisher;
    }

    @Data
    private static class Publisher {
        private String companyName;
        private String address;
        @JsonProperty("telephone")
        private String tel;
    }

    @Test
    void testSerializeBookToJSON() throws Exception {
        //Obj.
        Book book = new Book();
        book.setId("B0001");
        book.setName("Computer Science");
        book.setPrice(350);
        book.setIsbn("978-986-123-456-7");
        book.setCreatedTime(new Date());

        //JSON Str.
        String bookJSONStr = mapper.writeValueAsString(book);
        System.out.println(bookJSONStr);//JsonIgnore Isbn
        //JSON Obj.
        JSONObject bookJSON = new JSONObject(bookJSONStr);

        Assertions.assertEquals(book.getId(), bookJSON.getString("id"));
        Assertions.assertEquals(book.getName(), bookJSON.getString("name"));
        Assertions.assertEquals(book.getPrice(), bookJSON.getInt("price"));
//		Assertions.assertEquals(book.getIsbn(), bookJSON.getString("isbn")); //JsonIgnore Isbn

        Assertions.assertEquals(
                book.getCreatedTime().getTime(),
                bookJSON.getLong("createdTime")
        );
    }

    @Test
    public void testDeserializeJSONToPublisher() throws Exception {
        //JSON Obj.
        JSONObject publisherJSON = new JSONObject()
                .put("companyName", "Taipei Company")
                .put("address", "Taipei")
                .put("telephone", "02-1234-5678");

        //JSON Str.
        String publisherJSONStr = publisherJSON.toString();
        //JSON Str. Mapping to Obj.
        Publisher publisher = mapper.readValue(publisherJSONStr, Publisher.class);

        Assertions.assertEquals(publisherJSON.getString("companyName"), publisher.getCompanyName());
        Assertions.assertEquals(publisherJSON.getString("address"), publisher.getAddress());
        Assertions.assertEquals(publisherJSON.getString("telephone"), publisher.getTel());
    }

}
