package models.test;

import com.avaje.ebean.Model;
import data.types.LanguageType;
import models.Language;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sandeep.K on 02-02-2017.
 */
@Entity
public class CodeSessionConfiguration extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public Boolean execute;

    @Constraints.Required
    public Boolean compile;

    @ManyToMany
    public List<Language> languages;

    public static Model.Finder<Long, CodeSessionConfiguration> find = new Model.Finder<>(CodeSessionConfiguration.class);
}
