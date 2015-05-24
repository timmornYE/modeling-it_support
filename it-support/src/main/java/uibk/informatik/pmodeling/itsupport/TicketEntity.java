package uibk.informatik.pmodeling.itsupport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;
 
@Entity
public class TicketEntity implements Serializable {
	
	private static  final long serialVersionUID = 1L;
	 
	@Id
	@GeneratedValue
	protected Long id;
	
	@Version
	protected long version;
	
	protected String teachercode;
	protected String suptype;
	protected String supDescription;
	
	//@TODO: Only for testing; should be String with supporter Name
	protected String supportercode;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getTeachercode() {
		return teachercode;
	}

	public void setTeachercode(String teachercode) {
		this.teachercode = teachercode;
	}

	public String getSuptype() {
		return suptype;
	}

	public void setSuptype(String suptype) {
		this.suptype = suptype;
	}

	public String getSupportercode() {
		return supportercode;
	}

	public void setSupportercode(String supportercode) {
		this.supportercode = supportercode;
	}

	public String getSupDescription() {
		return supDescription;
	}

	public void setSupDescription(String supDescription) {
		this.supDescription = supDescription;
	}

	

	
	
}
