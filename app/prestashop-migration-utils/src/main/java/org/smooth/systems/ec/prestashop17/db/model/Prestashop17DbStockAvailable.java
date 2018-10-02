package org.smooth.systems.ec.prestashop17.db.model;

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
@Table(name="ps_stock_available")
public class Prestashop17DbStockAvailable {

	@Id
	@Column(name = "id_stock_available")
	private Long id;

	@Column(name = "id_product")
	private Long productId;

	@Column(name = "id_product_attribute")
	private Long productAttributeId;

	@Column(name = "id_shop")
	private Long shopId;

	@Column(name = "id_shop_group")
	private Long shopGroupId;

	@Column(name = "quantity")
	private Long quantity;

	@Column(name = "physical_quantity")
	private Long physical_quantity;

	@Column(name = "reserved_quantity")
	private Long reserved_quantity;

	@Column(name = "depends_on_stock")
	private Long depends_on_stock;

	@Column(name = "out_of_stock")
	private Long out_of_stock;
}