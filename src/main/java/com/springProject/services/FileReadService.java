package com.springProject.services;


import com.springProject.entities.SimpleEntity;
import com.springProject.repositories.EntityRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileReadService {

    private final EntityRepository entityRepository;

    FileReadService(EntityRepository entityRepository){
        this.entityRepository = entityRepository;
    }

    @PostConstruct
    public void init() throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONArray entities = null;
        try {
            FileReader reader = new FileReader("fileToReads.json");
            Object obj = jsonParser.parse(reader);
             entities = (JSONArray) obj;
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        if(entities == null){
            return;
        }
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        List<SimpleEntity> list = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            JSONObject element = (JSONObject) entities.get(i);
            SimpleEntity object = new SimpleEntity();
            object.setAmount(Integer.parseInt((String)element.get("amount")));
            object.setPrice(Integer.parseInt((String)element.get("price")));
            object.setStatus("active");
            object.setReleaseDate(simpleDateFormat.parse((String)element.get("releaseDate")));
            object.setTotalPrice(object.getPrice()*object.getAmount());
            list.add(object);
        }
        list.forEach(entityRepository::save);
    }
}