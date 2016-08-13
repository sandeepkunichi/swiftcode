package models.forum;

import models.AppUser;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    public AppUser postCreator;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> comments;

}
