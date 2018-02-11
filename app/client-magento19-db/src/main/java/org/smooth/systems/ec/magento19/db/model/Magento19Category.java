package org.smooth.systems.ec.magento19.db.model;

import lombok.Data;
import org.smooth.systems.ec.migration.model.AbstractTreeNode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Monichi <david.monichi@smooth-systems.solutions> on 09.02.18.
 */
@Data
@Entity
@Table(name="catalog_category_entity")
public class Magento19Category implements AbstractTreeNode<Magento19Category> {

	@Id
	@Column(name = "entity_id")
	private Long id;

	private Long level;

	private Long parentId;
//	private boolean enabled;

	// TEXT
//	private String metaKeywords;



//	private String description;


//	private String metaDescription;

	// VARCHAR
//	private String name;
//	private String friendlyUrl;


	@Transient
	private List<Magento19Category> childrens = new ArrayList<>();
}