package controllers.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.types.LanguageType;
import models.Language;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

/**
 * Created by Sandeep.K on 03-02-2017.
 */
public class LanguageController extends Controller {
    @Inject
    ObjectMapper objectMapper;

    public Result getLanguages(){
        return ok(objectMapper.valueToTree(Language.find.all()).toString());
    }

    public Result configureLanguage(String languageName){
        LanguageType languageType = LanguageType.valueOf(languageName);
        if(Language.find.where().eq("language_type", languageType.ordinal()).findUnique() != null){
            return ok("Language already configured");
        }
        Language language = new Language(languageType);
        Language.db().insert(language);
        return ok("Language Configured");
    }
}
