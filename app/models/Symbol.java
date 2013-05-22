package models;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.util.List;

@JsonSerialize(include=Inclusion.NON_NULL)
public abstract class Symbol {

	public abstract String getName();
	public abstract int getDuration();

	public List<String> getSounds() {
		return null;
	}

}
