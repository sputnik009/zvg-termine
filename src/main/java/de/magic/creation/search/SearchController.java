package de.magic.creation.search;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import de.magic.creation.home.SearchSettings;

@Controller
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
  public String searchSubmit( @ModelAttribute SearchSettings settings, Model model)
  {
    log.debug( "Search: " + settings);
    List<ZvgObject> objs = searchManager.search( settings);
    log.debug( "Search returns: " + objs.size());
    model.addAttribute( "objects", objs);
    return "searchResult";
  }
}
