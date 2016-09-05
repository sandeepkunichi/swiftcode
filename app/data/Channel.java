package data;

/**
 * Created by Sandeep.K on 9/5/2016.
 */
public class Channel {
    private String name;
    private String purpose;
    private String id;

    public Channel(String name, String purpose, String id) {
        this.name = name;
        this.purpose = purpose;
        this.id = id;
    }

    public Channel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
