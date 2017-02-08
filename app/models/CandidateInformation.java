package models;

import com.avaje.ebean.Model;
import data.types.EngineeringStream;
import data.types.Gender;
import play.data.format.Formats;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Sandeep.K on 07-02-2017.
 */
@Entity
public class CandidateInformation extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public String firstName;
    public String middleName;
    public String lastName;
    @Formats.DateTime(pattern = "dd/mm/yyyy")
    public Date dateOfBirth;
    public String contactNumber;
    public Gender gender;
    public String mobileNumber;
    @Column(columnDefinition = "TEXT")
    public String address;
    public String institution;
    public Double highSchoolMarks;
    public Integer highSchoolYear;
    public Double secondaryMarks;
    public Integer secondaryYear;
    public Double sem1Marks;
    public Double sem2Marks;
    public Double sem3Marks;
    public Double sem4Marks;
    public Double sem5Marks;
    public Double sem6Marks;
    public Double sem7Marks;
    public Double sem8Marks;
    public Double underGraduateAggregate;
    public EngineeringStream engineeringStream;
    public String underGraduateUniversity;

    public Boolean isComplete(){
        if(firstName == null || firstName.isEmpty()){
            return false;
        }
        if(lastName == null || lastName.isEmpty()){
            return false;
        }
        if(dateOfBirth == null){
            return false;
        }
        if(contactNumber == null || contactNumber.isEmpty()){
            return false;
        }
        if(mobileNumber == null || mobileNumber.isEmpty()){
            return false;
        }
        if(address == null || address.isEmpty()){
            return false;
        }
        if(institution == null || institution.isEmpty()){
            return false;
        }
        if(underGraduateUniversity == null || underGraduateUniversity.isEmpty()){
            return false;
        }
        return true;
    }

    public String getDateOfBirthString(){
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        return (dateOfBirth == null) ? null : df.format(dateOfBirth);
    }

    public CandidateInformation(Map<String, String> data) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        this.id = Long.valueOf(data.get("id"));
        this.firstName = data.get("firstName");
        this.middleName = data.get("middleName");
        this.lastName = data.get("lastName");
        this.dateOfBirth = (data.get("dateOfBirth") == null || data.get("dateOfBirth").isEmpty()) ? null : df.parse(data.get("dateOfBirth"));
        this.contactNumber = data.get("contactNumber");
        this.gender = Gender.valueOf(data.get("gender"));
        this.mobileNumber = data.get("mobileNumber");
        this.address = data.get("address");
        this.institution = data.get("institution");
        this.highSchoolMarks = (data.get("highSchoolMarks") == null || data.get("highSchoolMarks").isEmpty()) ? null : Double.valueOf(data.get("highSchoolMarks"));
        this.highSchoolYear = (data.get("highSchoolYear") == null || data.get("highSchoolYear").isEmpty()) ? null : Integer.valueOf(data.get("highSchoolYear"));
        this.secondaryMarks = (data.get("secondaryMarks") == null || data.get("secondaryMarks").isEmpty()) ? null : Double.valueOf(data.get("secondaryMarks"));
        this.secondaryYear = (data.get("secondaryYear") == null || data.get("secondaryYear").isEmpty()) ? null : Integer.valueOf(data.get("secondaryYear"));
        this.sem1Marks = (data.get("sem1Marks") == null || data.get("sem1Marks").isEmpty()) ? null : Double.valueOf(data.get("sem1Marks"));
        this.sem2Marks = (data.get("sem2Marks") == null || data.get("sem2Marks").isEmpty()) ? null : Double.valueOf(data.get("sem2Marks"));
        this.sem3Marks = (data.get("sem3Marks") == null || data.get("sem3Marks").isEmpty()) ? null : Double.valueOf(data.get("sem3Marks"));
        this.sem4Marks = (data.get("sem4Marks") == null || data.get("sem4Marks").isEmpty()) ? null : Double.valueOf(data.get("sem4Marks"));
        this.sem5Marks = (data.get("sem5Marks") == null || data.get("sem5Marks").isEmpty()) ? null : Double.valueOf(data.get("sem5Marks"));
        this.sem6Marks = (data.get("sem6Marks") == null || data.get("sem6Marks").isEmpty()) ? null : Double.valueOf(data.get("sem6Marks"));
        this.sem7Marks = (data.get("sem7Marks") == null || data.get("sem7Marks").isEmpty()) ? null : Double.valueOf(data.get("sem7Marks"));
        this.sem8Marks = (data.get("sem8Marks") == null || data.get("sem8Marks").isEmpty()) ? null : Double.valueOf(data.get("sem8Marks"));
        this.underGraduateAggregate = (data.get("underGraduateAggregate") == null || data.get("underGraduateAggregate").isEmpty()) ? null : Double.valueOf(data.get("underGraduateAggregate"));
        this.engineeringStream = EngineeringStream.valueOf(data.get("engineeringStream"));
        this.underGraduateUniversity = data.get("underGraduateUniversity");
    }

    public CandidateInformation(){

    }

    public static Model.Finder<Long, CandidateInformation> find = new Model.Finder<>(CandidateInformation.class);

}
