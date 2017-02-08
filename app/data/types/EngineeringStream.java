package data.types;

/**
 * Created by Sandeep.K on 07-02-2017.
 */
public enum EngineeringStream {
    CS("Computer Science & Engineering"),
    IS("Information Science & Engineering"),
    ECE("Electronics & Communication Engineering");

    private final String displayName;

    EngineeringStream(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
