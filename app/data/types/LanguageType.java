package data.types;

/**
 * Created by Sandeep.K on 26-01-2017.
 */
public enum LanguageType {
    PYTHON("Python"),
    JAVA("Java"),
    C("C"),
    CPP("C++");

    private final String displayName;
    LanguageType(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
