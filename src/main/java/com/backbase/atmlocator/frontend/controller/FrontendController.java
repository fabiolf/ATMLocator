package com.backbase.atmlocator.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This controller handles the frontend access. A call to localhost:8080 (for
 * instance) will be handled by this controller and will return the root page
 * for the frontend.
 * 
 * @author Fabio Fonseca
 *
 */
@Controller
public class FrontendController {
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homepage() {
        return "index.html";
    }

}
