/*
package com.innocito.axcl.changelogs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innocito.axcl.entity.Language;
import com.innocito.axcl.repository.LanguageRepository;
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
@ChangeUnit(id = "V20250729_Language_Migration", order = "0002", author = "nikhil", transactional = false)
public class V20250729_Language_Migration {
    @Execution
    public void dumpingLanguageDataIntoCollection(LanguageRepository languageRepository, ObjectMapper objectMapper) {
        log.info("Migration started...");
        try {
            if (languageRepository.count() == 0) {
                ClassPathResource resource = new ClassPathResource("json/languages.json");
                try (InputStream inputStream = resource.getInputStream()) {
                    List<Language> languageList = new ArrayList<>();
                    Set<String> languageNames = new HashSet<>();
                    String content = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));
                    JSONArray jsonArray = new JSONArray(content);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject countryObject = jsonArray.getJSONObject(i);
                        String languageName = countryObject.getString("name");
                        if (!languageNames.contains(languageName)) {
                            languageList.add(objectMapper.readValue(countryObject.toString(), Language.class));
                            languageNames.add(languageName);
                        }
                    }
                    languageRepository.saveAll(languageList);
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
*/
