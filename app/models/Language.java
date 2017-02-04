package models;

import com.avaje.ebean.Model;
import data.types.LanguageType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Sandeep.K on 02-02-2017.
 */
@Entity
public class Language extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String name;

    public LanguageType languageType;

    public Language(LanguageType languageType) {
        this.languageType = languageType;
        this.name = languageType.getDisplayName();
    }

    public static Model.Finder<Long, Language> find = new Model.Finder<>(Language.class);
}
