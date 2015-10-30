package app.domain;

import app.controller.TeammateController;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Wrapper class for a teammate and link
 */
public class TeammateAndLink {

    private final Teammate teammate;
    private final Link link;

    public TeammateAndLink(Teammate teammate) {
        this.teammate = teammate;
        this.link = linkTo(methodOn(TeammateController.class)
                .getTeammate(teammate.getId()))
                .withRel(teammate.getFirstName() + " " + teammate.getLastName());
    }

    public Teammate getTeammate() {
        return teammate;
    }

    public Link getLink() {
        return link;
    }
}
