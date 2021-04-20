package ollek.mateusz.githubuserslookup.rest;

import ollek.mateusz.githubuserslookup.logic.ReposGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import ollek.mateusz.githubuserslookup.logic.StarsCountResponseHolder;


@RestController
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/starscount/{user}")
    public EntityModel<StarsCountResponseHolder> starsCount(@PathVariable String user){
        logger.info(user);
        StarsCountResponseHolder response = new ReposGetter(user).calcStarsCount();
        return EntityModel.of(response,
                linkTo(methodOn(AppController.class).starsCount(user)).withSelfRel());
    }

    @GetMapping("/repos/{user}")
    public EntityModel<ReposGetter> repos(@PathVariable String user){
        logger.info(user);
        ReposGetter response = new ReposGetter(user);
        response.searchForRepos();
        return EntityModel.of(response,
                linkTo(methodOn(AppController.class).repos(user)).withSelfRel());
    }
}
