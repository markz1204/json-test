package com.mzhang.controller;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class JSONController {

    private final Logger logger = LoggerFactory.getLogger(JSONController.class);

    //Single file upload
    @PostMapping("/json/validate")
    // If not @RestController, uncomment this
    //@ResponseBody
    public ResponseEntity<?> validate(@RequestBody String incoming) {

        if (incoming.isEmpty()) {
            return new ResponseEntity("please send json data!", HttpStatus.OK);
        }

        JSONObject jsonObject = new JSONObject(incoming);

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schema/product.json");

        JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
        Schema schema = SchemaLoader.load(rawSchema);
        try {
            schema.validate(new JSONObject(jsonObject.toString()));

        }catch (ValidationException e) {

            List<String> failure = e.getCausingExceptions().stream()
                    .map(ValidationException::getMessage)
                    .collect(toList());
            return new ResponseEntity<String>(failure.size() == 0 ? e.getMessage() : failure.toString(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<String>("Valid", HttpStatus.OK);

    }


}
