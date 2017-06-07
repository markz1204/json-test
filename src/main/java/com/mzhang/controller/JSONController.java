package com.mzhang.controller;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
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

    @PostMapping("/json/validate")
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

    @PostMapping("/json/display")
    public ResponseEntity<?> display(@RequestBody String incoming){
        if (incoming.isEmpty()) {
            return new ResponseEntity("please send json data!", HttpStatus.OK);
        }

        JSONObject jsonObject = new JSONObject(incoming);
        StringBuffer stringBuffer = new StringBuffer();
        String result = treeDisplay(jsonObject, null, stringBuffer);

        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    String treeDisplay(Object object, String keyVal, StringBuffer result){

        if(object instanceof JSONObject){

            result.append("<ul>");

            ((JSONObject) object).keys().forEachRemaining(key->{
                Object ele = ((JSONObject) object).get(key.toString());

                if(ele instanceof JSONObject) {
                    result.append("<li>");
                    result.append(key.toString() + ":");
                }

                treeDisplay(((JSONObject) object).get(key.toString()), key.toString(), result);

                if(ele instanceof JSONObject) {
                    result.append("</li>");
                }

            });

            result.append("</ul>");

        }else if(object instanceof JSONArray){

            result.append("<li>");

            result.append(keyVal + ":");

            for(int i=0; i < ((JSONArray) object).length(); i++){
                result.append("<ul>");
                treeDisplay(((JSONArray) object).get(i), null, result);
                result.append("</ul>");
            }

            result.append("</li>");

        }else{

            if(keyVal != null) {
                result.append("<li>");
                result.append(keyVal + ":" + object);
                result.append("</li>");
            }else {
                result.append(object + " ");
            }


        }
        return result.toString();
    }

}
