package app.controller;

import app.repository.TeammateRepository;
import app.domain.Teammate;
import app.domain.TeammateAndLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by tmolloy on 10/10/2015.
 */

/**
 *Controller for app endpoints which ar fully HATEOS and secured
 */
@Controller
public class TeammateController {

    private final TeammateRepository tmr;

    @Autowired
    public TeammateController(TeammateRepository tmr) {
        this.tmr = tmr;
    }

    @RequestMapping(value = "/teammates", method = GET)
    public ModelAndView getTeammates() {
        // Specify the view name
        return new ModelAndView("teammates")
                // Look up ALL teammates and wrap each with related links
                .addObject("teammates",
                        StreamSupport.stream(tmr.findAll().spliterator(), false)
                                .map(TeammateAndLink::new)
                                .toArray())
                        // new Teammate command object
                .addObject("teammate", new Teammate())
                .addObject("postLink",
                        linkTo(methodOn(TeammateController.class).newTeammate(null))
                                .withRel("Create"))
                .addObject("links", Arrays.asList(
                        linkTo(methodOn(TeammateController.class).getTeammates())
                                .withRel("All Teammates")
                ));
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/teammates", method = POST)
    public ModelAndView newTeammate (@ModelAttribute Teammate teammate){
        tmr.save(teammate);
        return getTeammates();
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/teammate/{id}", method = GET)
    public ModelAndView getTeammate (@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("teammate");
        // Look up the related teammate
        final Teammate teammate = tmr.findOne(id);
        modelAndView.addObject("teammate", teammate);

        List<Link> links = new ArrayList<>();

        links.add(linkTo(methodOn(TeammateController.class).getTeammates())
                .withRel("All Teammates"));

        if (SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream().anyMatch(
                        p -> p.getAuthority().equals("ROLE_ADMIN"))) {
            links.add(linkTo(methodOn(TeammateController.class).editTeammate(id))
                    .withRel("Edit"));
        }

        modelAndView.addObject("links", links);
        return modelAndView;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/teammates/{id}", method = PUT)
    public ModelAndView updateTeammate(@PathVariable Long id, @ModelAttribute Teammate t) {
        t.setId(id);
        tmr.save(t);
        return getTeammate(id);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/teammate/{id}/edit", method = GET)
    public ModelAndView editTeammate(@PathVariable Long id){
        final Teammate t = tmr.findOne(id);
        return new ModelAndView("edit")
                .addObject("teammate", t)
                .addObject("putLink",
                        linkTo(methodOn(TeammateController.class).getTeammate(id))
                            .withRel("Update"))
                .addObject("links", asList(linkTo(methodOn(TeammateController.class).getTeammate(id))
                            .withRel("Cancel")
                ));
    }

}
