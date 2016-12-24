package de.magic.creation.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import de.magic.creation.home.SearchSettings;

@Controller
@SessionAttributes("searchSettings")
public class SearchController
{
  private final Logger  log = LoggerFactory.getLogger( SearchController.class);

  private SearchManager searchManager;

  @Autowired
  public SearchController( SearchManager searchManager)
  {
    this.searchManager = searchManager;
  }

  @PostMapping("/search")
  public String searchSubmit( @ModelAttribute("searchSettings") SearchSettings settings, Model model)
  {
    log.debug( "searchSubmit (post)");
    return searchInternal( settings, model);
  }

  @GetMapping("/search")
  public String searchSession( @ModelAttribute("searchSettings") SearchSettings settings, Model model)
  {
    log.debug( "searchSubmit (get)");
    return searchInternal( settings, model);
  }

  private String searchInternal( SearchSettings settings, Model model)
  {
    log.debug( "Search (internal): " + settings);
    model.addAttribute( "searchSettings", settings);

    List<ZvgObject> objs = searchManager.search( settings);

    log.debug( "Search returns: " + objs.size());
    model.addAttribute( "objects", objs);

    return "searchResult";
  }

  @ModelAttribute("searchSettings")
  public SearchSettings getSearchSettings()
  {
    return new SearchSettings();
  }
}
