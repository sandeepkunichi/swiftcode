package models.forum;

import com.avaje.ebean.Model;
import models.AppUser;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
@Entity
public class Comment extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Constraints.Required
    @ManyToOne
    public Post post;

    @Constraints.Required
    public String text;

    @Constraints.Required
    @ManyToOne
    public AppUser commentRaiser;

    public static Finder<Long, Comment> find = new Finder<Long, Comment>(Comment.class);
}
