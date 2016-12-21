package de.magic.creation.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailsController
{
  private final Logger  log = LoggerFactory.getLogger( DetailsController.class);

  private SearchManager searchManager;

  @Autowired
  public DetailsController( SearchManager searchManager)
  {
    this.searchManager = searchManager;
  }

  @GetMapping("/details/{id}")
  public String details( @PathVariable(value = "id") final String id, Model model)
  {
    log.debug( "details: " + id);
    ZvgObject zvgObject = searchManager.getObject( id);
    ZvgObjectDetail zvgDetail = searchManager.details( zvgObject.getId(), zvgObject.getLandAbk());
    log.debug( "found obj: " + zvgObject);
    log.debug( "found detials: " + zvgDetail);

    model.addAttribute( "zvgObj", zvgObject);
    model.addAttribute( "zvgDet", zvgDetail);

    return "details";
  }
}
