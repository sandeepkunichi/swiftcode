package data;

/**
 * Created by Sandeep.K on 9/5/2016.
 */
public class Message {

    private String type;
    private String user;
    private String text;
    private String ts;

    public Message(String type, String user, String text, String ts) {
        this.type = type;
        this.user = user;
        this.text = text;
        this.ts = ts;
    }

    public Message() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTs() {
        return this.ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
