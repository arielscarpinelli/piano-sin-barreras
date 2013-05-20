package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import models.Score;
import models.ScoreContent;

import org.xml.sax.SAXException;

import play.api.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.audiveris.proxymusic.ScorePartwise;
import com.audiveris.proxymusic.util.Marshalling;

public class Scores extends Controller {
	
    public static Result view(String slug) {
        return ok(views.html.scoreView.render(Score.findBySlug(slug).getName(), slug));
    }
  
    public static Result list() {
        return ok(views.html.scoreList.render(Score.findAll()));
    }

    public static Result get(String slug) throws FileNotFoundException, JAXBException, SAXException, ParserConfigurationException {

		FileInputStream fileInputStream = new FileInputStream(Play.current().getFile("scores/" + slug + ".xml"));
		ScorePartwise score = Marshalling.unmarshal(fileInputStream);
		
		return ok(Json.toJson(ScoreContent.fromScorePartwise(score)));
	}
	
}
