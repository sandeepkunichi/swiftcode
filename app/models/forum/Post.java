package models.forum;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.AppUser;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by Sandeep.K on 8/14/2016.
 */
@Entity
public class Post extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @JsonIgnore
    public AppUser postCreator;

    @Constraints.Required
    public String text;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    public List<Comment> comments;

    public static Model.Finder<Long, Post> find = new Model.Finder<Long, Post>(Post.class);

    public static String findAll() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(Post.find.all());
    }
}
