package transcloud.app.neonblogger.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NeonBlog {

	private long id;
	private String title;
	private String content;
	private String[] tags;
	private NeonBlogStatus status;
	private Date created;
	private Date lateUpdated;
	private NeonUser author;
	private List<NeonComments> comments;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public NeonBlogStatus getStatus() {
		return status;
	}
	public void setStatus(NeonBlogStatus status) {
		this.status = status;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getLateUpdated() {
		return lateUpdated;
	}
	public void setLateUpdated(Date lateUpdated) {
		this.lateUpdated = lateUpdated;
	}
	public NeonUser getAuthor() {
		return author;
	}
	public void setAuthor(NeonUser author) {
		this.author = author;
	}
	public List<NeonComments> getComments() {
		return comments;
	}
	public void setComments(List<NeonComments> comments) {
		this.comments = comments;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		DateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
		sb.append(this.getTitle()).append("\n").append("Created: ").append(dateFormat.format(this.created));
		return sb.toString();
	}
}
