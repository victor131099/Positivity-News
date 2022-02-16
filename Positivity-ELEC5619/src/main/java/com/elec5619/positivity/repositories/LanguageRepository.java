package com.elec5619.positivity.repositories;

import com.elec5619.positivity.domains.Language;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LanguageRepository extends CrudRepository<Language, Integer> {

    @Query("select language_name from Language")
    List<String> getAllLanguageNames();
}