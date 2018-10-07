package org.smooth.systems.ec.migration.model;

import lombok.Data;

import java.util.List;

@Data
public class RelatedProducts {

	private Long productId;

	private List<Long> relatedProductIds;
}
