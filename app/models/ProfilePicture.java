package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by Sandeep.K on 07-02-2017.
 */
@Entity
public class ProfilePicture extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Lob
    public byte[] fileData;

    @OneToOne
    public CandidateInformation candidateInformation;

    public static Finder<Long, ProfilePicture> find = new Finder<>(ProfilePicture.class);
}
