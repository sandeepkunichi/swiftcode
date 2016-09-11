package data;

/**
 * Created by Sandeep.K on 9/11/2016.
 */
public class DashboardAlert {

    private String message;
    private String alertClass;

    public DashboardAlert(String message, String alertClass) {
        this.message = message;
        this.alertClass = alertClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlertClass() {
        return alertClass;
    }

    public void setAlertClass(String alertClass) {
        this.alertClass = alertClass;
    }
}
