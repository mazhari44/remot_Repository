
public class Page {
	private String url;
	private String fetch_time;
	private int length;
	private String modified_time;
	private String type;
	
	public Page() {
		url=fetch_time=type=modified_time=null;
		// TODO Auto-generated constructor stub
		length=0;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFetch_time() {
		return fetch_time;
	}

	public void setFetch_time(String fetch_time) {
		this.fetch_time = fetch_time;
	}
	

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getModified_time() {
		return modified_time;
	}

	public void setModified_time(String modified_time) {
		this.modified_time = modified_time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String toString(){
		return url+" "+fetch_time+" "+length+" "+modified_time+" "+type;
	}

}
