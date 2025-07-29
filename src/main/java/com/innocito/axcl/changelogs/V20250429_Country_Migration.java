package com.innocito.axcl.changelogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@ChangeUnit(id = "V20250429_Country_Migration", order = "0001", author = "Nikhil", transactional = false)
public class V20250429_Country_Migration {
    @Execution
    public void dumpingCountryDataIntoCollection(CountryRepository countryRepository, ObjectMapper objectMapper) {
        log.info("Migration started...");
        try {
            if (countryRepository.count() == 0) {
                ClassPathResource resource = new ClassPathResource("json/country.json");
                try (InputStream inputStream = resource.getInputStream()) {
                    List<Country> countryList = new ArrayList<>();
                    Set<String> countryNames = new HashSet<>();
                    String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    JSONArray jsonArray = new JSONArray(content);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject countryObject = jsonArray.getJSONObject(i);
                        String countryName = countryObject.getString("name");
                        if (!countryNames.contains(countryName)) {
                            countryList.add(objectMapper.readValue(countryObject.toString(), Country.class));
                            countryNames.add(countryName);
                        }
                    }
                    countryRepository.saveAll(countryList);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Migration completed...");
    }

    @RollbackExecution
    public void rollback() {
    }
}
