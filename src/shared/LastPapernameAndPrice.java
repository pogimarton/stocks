package shared;

import java.io.Serializable;
import java.util.Vector;

public class LastPapernameAndPrice implements Serializable {

	private static final long serialVersionUID = 1L;

	private Vector<String> favPaperNames;

	
	public LastPapernameAndPrice(Vector<String> favPaperNames) {
		this.favPaperNames = favPaperNames;

	}


	public Vector<String> getFavPaperNames() {
		return favPaperNames;
	}


	public void setFavPaperNames(Vector<String> favPaperNames) {
		this.favPaperNames = favPaperNames;
	}





}
