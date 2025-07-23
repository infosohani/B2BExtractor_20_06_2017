package com.businessextractor.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Layer supertype defined for the domain model. This is an application of the Layer Supertype 
 * design pattern from Patterns of Enterprise Application Architecture. It is the basic type from
 * wich all application bound entites will descend.
 * @author user
 *
 */
@MappedSuperclass
public abstract class AbstractEntity {
	
	/**
	 *  Primary key field used in all entities in the application
	 */
	private Long id;

	/**
	 * Returns the id of this entity.
	 * @return The entity id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	@DocumentId
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of this entity
	 * @param id The entity id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof AbstractEntity)) {
			return false;
		}
		AbstractEntity rhs = (AbstractEntity)object;
		return new EqualsBuilder().appendSuper(super.equals(object))
			.append(this.id, rhs.id)
			.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-1453383665, -775124205).appendSuper(super.hashCode())
			.append(this.id)
			.toHashCode();
	}
	

	
	
}
