package ollek.mateusz.githubuserslookup.rest;

import ollek.mateusz.githubuserslookup.logic.ReposGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import ollek.mateusz.githubuserslookup.logic.StarsCountResponseHolder;

import javax.servlet.http.HttpServletRequest;


@RestController
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/starscount/{user}")
    public EntityModel<StarsCountResponseHolder> starsCount(@PathVariable String user) throws Exception{
        logger.info("Fetch results for "+user);
        StarsCountResponseHolder response = new ReposGetter(user).calcStarsCount();
        if(response == null)
            throw new Exception("User not found");
        return EntityModel.of(response,
                linkTo(methodOn(AppController.class).starsCount(user)).withSelfRel());
    }

    @GetMapping("/repos/{user}")
    public EntityModel<ReposGetter> repos(@PathVariable String user) throws Exception {
        logger.info("Fetch results for "+user);
        ReposGetter response = new ReposGetter(user);
        response.searchForRepos();
        if(response.getRepos() == null)
            throw new Exception("User not found");
        return EntityModel.of(response,
                linkTo(methodOn(AppController.class).repos(user)).withSelfRel());
    }

    @ExceptionHandler({Exception.class})
    public String errorHandler(HttpServletRequest req, Exception ex){
        return ex.getMessage();
    }
}
