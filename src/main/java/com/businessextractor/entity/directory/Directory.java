package com.businessextractor.entity.directory;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

import com.businessextractor.entity.AbstractEntity;
import com.businessextractor.entity.business.Business;
import com.businessextractor.entity.websiteroot.WebsiteRoot;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a business holding web directory.
 * @author Bogdan Vlad
 */
@Entity(name = "Directory")
@Table(name = "directory")
public class Directory extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "source_name", nullable = false)
	private String sourceName;

	@Column(name = "source_url", nullable = false)
	private String sourceURL;

	@Column(name = "activated")
	private boolean activated;

	@ManyToOne(optional = false)
	@JoinColumn(name = "websiteroot_id", nullable = false)
	private WebsiteRoot websiteRoot;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(
			name = "directory_business",
			joinColumns = @JoinColumn(name = "directory_id"),
			inverseJoinColumns = @JoinColumn(name = "business_id")
	)
	private Set<Business> businesses;

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceURL() {
		return sourceURL;
	}

	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public WebsiteRoot getWebsiteRoot() {
		return websiteRoot;
	}

	public void setWebsiteRoot(WebsiteRoot websiteRoot) {
		this.websiteRoot = websiteRoot;
	}

	public Set<Business> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(Set<Business> businesses) {
		this.businesses = businesses;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("id", this.getId())
				.append("sourceName", this.sourceName)
				.append("sourceURL", this.sourceURL)
				.append("activated", this.activated)
				.append("websiteRoot", this.websiteRoot)
				.toString();
	}
}
