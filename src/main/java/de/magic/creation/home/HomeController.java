package de.magic.creation.home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController
{
  private final Logger log = LoggerFactory.getLogger( HomeController.class);
  
  @GetMapping("/home")
  public String homeForm( Model model)
  {
    log.debug( "homeForm");
    model.addAttribute( "searchSettings", new SearchSettings());
    return "home";
  }

  @ModelAttribute("allKinds")
  public EKind[] getAllKinds()
  {
    log.debug( "getAllKinds");
    return EKind.values();
  }
}
