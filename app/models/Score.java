package models;

import java.util.Arrays;
import java.util.List;

public class Score {

	private String name;
	private String slug;
	
	public Score(String name, String slug) {
		this.name = name;
		this.slug = slug;
	}
	public String getName() {
		return name;
	}
	public String getSlug() {
		return slug;
	}
	
	public static List<Score> findAll() {
		return Arrays.asList(new Score("Minuet en Sol mayor", "bach-minuet-in-G"), new Score("Invensión de Bach", "bach-invension"), new Score("Noche de paz", "noche-de-paz"));
	}
	
	public static Score findBySlug(String slug) {
		if (slug != null) {
			List<Score> all = findAll();
			for(Score score : all) {
				if(slug.equals(score.getSlug())) {
					return score;
				}
			}
		}
		return null;
	}
}
