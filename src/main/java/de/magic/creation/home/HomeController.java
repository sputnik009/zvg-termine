package de.magic.creation.home;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.magic.creation.repo.EKind;
import de.magic.creation.repo.ELand;

@Controller
@SessionAttributes("searchSettings")
public class HomeController
{
  private final Logger log = LoggerFactory.getLogger( HomeController.class);

  @GetMapping("/home")
  public String homeForm( Model model)
  {
    log.debug( "homeForm");
    return "home";
  }

  @ModelAttribute("allKinds")
  public List<EKind> getAllKinds()
  {
    log.debug( "getAllKinds");
    return EKind.valuesAsList();
  }
  
  @ModelAttribute("allLands")
  public List<ELand> getAllLands()
  {
    log.debug( "getAllLands");
    return ELand.valuesAsList();
  }
  
  @ModelAttribute("searchSettings")
  public SearchSettings getSearchSettings()
  {
    return new SearchSettings();
  }
}
