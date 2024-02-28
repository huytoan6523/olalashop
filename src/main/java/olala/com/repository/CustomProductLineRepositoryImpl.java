package olala.com.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import olala.com.constant.Constant;
import olala.com.model.PageData;
import olala.com.model.PageInfo;
import olala.com.model.ProductLineDto;
import olala.com.model.ProductLineInfo;

@Transactional
public class CustomProductLineRepositoryImpl implements CustomProductLineRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public PageData<ProductLineDto> findProductLineDtoByCategoryId(Long categoryId, PageInfo pageInfo) {

		// get pageNumber & Page Size & Keyword
		Integer pageNumber = pageInfo.getPageNumber();
		Integer pageSize = pageInfo.getPageSize();
		String keyword = pageInfo.getKeyword();

		// bodyQuery
		StringBuilder bodyQuery = new StringBuilder();
		bodyQuery.append(" FROM  	 ProductLine p ");
		bodyQuery.append(" JOIN  	 Category c ");
		bodyQuery.append(" ON 		 c.id = p.category.id ");
		bodyQuery.append(" JOIN  	 Product prod ");
		bodyQuery.append(" ON  		 prod.productLine.id = p.id ");
		bodyQuery.append(" WHERE  	 c.id IN ");
		bodyQuery.append(" 			 (SELECT ctgy.id ");
		bodyQuery.append(" 			 FROM 	Category ctgy ");
		bodyQuery.append(" 			 WHERE  ctgy.parentCategory.id =  " + categoryId + ")");
		bodyQuery.append(" GROUP BY  p.id, p.name, p.imgUrl1, p.discount");
		// default order By bestSeller
		bodyQuery.append(" 				ORDER BY sum(prod.sold) DESC");

		// query
		StringBuilder query = new StringBuilder();
		query.append(
				" SELECT 	 new ptit.com.model.ProductLineDto(p.id, p.name, p.imgUrl1, min(prod.price), max(prod.price), sum(prod.sold), p.discount  )");
		query.append(bodyQuery);

		// countQuery
		StringBuilder countQuery = new StringBuilder();
		countQuery.append(" SELECT 	 count(a.name) ");
		countQuery.append(" FROM ( SELECT 	 p.name");
		countQuery.append(" 		FROM  	 product_line p ");
		countQuery.append(" 		JOIN  	 Category c ");
		countQuery.append(" 		ON 		 c.id = p.category_id ");
		countQuery.append(" 		JOIN  	 Product prod ");
		countQuery.append(" 		ON  		 prod.product_line_id = p.id ");
		countQuery.append(" 		WHERE  	 c.id IN ");
		countQuery.append(" 			 	(SELECT ctgy.id ");
		countQuery.append(" 			 	FROM 	Category ctgy ");
		countQuery.append(" 			 	WHERE  ctgy.parent_category_id =  " + categoryId + ")");
		countQuery.append(" 				GROUP BY  p.name ) a");

		// totalElements
		Query countQ = entityManager.createNativeQuery(countQuery.toString());
		Integer totalElements = (Integer) countQ.getSingleResult();
		Integer totalPages = (int) Math.ceil(totalElements / (float) pageSize);

		// List Result
		TypedQuery<ProductLineDto> q = entityManager.createQuery(query.toString(), ProductLineDto.class);
		q.setFirstResult((pageNumber) * pageSize);
		q.setMaxResults(pageSize);
		List<ProductLineDto> list = q.getResultList();

		PageData<ProductLineDto> pageData = new PageData<>();
		pageData.setType(ProductLineDto.class);
		pageData.setContent(list);
		pageData.setTotalPages(totalPages);

		return pageData;
	}

	@Override
	public ProductLineDto findProductLineDtoById(Long productLineId) {

		// bodyQuery
		StringBuilder query = new StringBuilder();
		query.append(
				" SELECT 	 new ptit.com.model.ProductLineDto(p.id, p.name, min(prod.price), max(prod.price), sum(prod.stock), p.discount )");
		query.append(" FROM  	 ProductLine p ");
		query.append(" JOIN  	 Product prod ");
		query.append(" ON  		 prod.productLine.id = p.id ");
		query.append(" WHERE  	 p.id = " + productLineId);
		query.append(" GROUP BY  p.id, p.name, p.discount");

		TypedQuery<ProductLineDto> q = entityManager.createQuery(query.toString(), ProductLineDto.class);
		ProductLineDto productLineDto = q.getSingleResult();
		return productLineDto;
	}

	@Override
	public PageData<ProductLineDto> findProductLineDtoProductLineInfo(ProductLineInfo productLineInfo) {
		Integer pageNumber = productLineInfo.getPageNumber();
		System.out.println(pageNumber+" pageNUmber");
		Integer pageSize = productLineInfo.getPageSize();
		System.out.println(pageSize+" pageSize");

		// keyword
		String keyword = "'%" + productLineInfo.getKeyword() + "%'";

		// categoryIds
		List<Long> categoryIds = productLineInfo.getCategoryIds();
		StringBuilder categoriesQuery = new StringBuilder();

		if (categoryIds.size() > 1) {
			categoriesQuery.append(" WHERE ( c.id = " + categoryIds.get(0));
			for (int i = 1; i < categoryIds.size(); i++) {
				categoriesQuery.append(" OR c.id = " + categoryIds.get(i));

			}
			categoriesQuery.append(" ) ");
		} else {
			categoriesQuery.append(" WHERE c.id = " + categoryIds.get(0));
		}

		// orderBy
		String orderBy = productLineInfo.getOrderBy();

		// minPrice
		Long minPrice = productLineInfo.getMinPrice();
		if (minPrice == null) {
			minPrice = Long.valueOf(0);
		}
		Long maxPrice = productLineInfo.getMaxPrice();
		if (maxPrice == null) {
			maxPrice = Long.valueOf(Integer.MAX_VALUE);
		}

		// query
		StringBuilder query = new StringBuilder();
		query.append(
				" SELECT 	 new ptit.com.model.ProductLineDto(p.id, p.name, p.imgUrl1 ,min(prod.price), max(prod.price), sum(prod.sold), p.discount  )");
		query.append(" FROM  	 ProductLine p ");
		query.append(" JOIN  	 Category c ");
		query.append(" ON 		 c.id = p.category.id ");
		query.append(" JOIN  	 Product prod ");
		query.append(" ON  		 prod.productLine.id = p.id ");
		query.append(categoriesQuery);
		query.append(" AND  	 p.name LIKE" + keyword);
		query.append(" AND  	 prod.price >= " + minPrice);
		query.append(" AND  	 prod.price <= " + maxPrice);
		query.append(" GROUP BY  p.id, p.name, p.imgUrl1, p.discount");

		// ORDER BY
		switch (orderBy) {
		case Constant.ProductLineOrderBy.BEST_SELLER:
			query.append(" ORDER BY sum(prod.sold) DESC");
			break;
		case Constant.ProductLineOrderBy.NEWEST:
			query.append(" ORDER BY max(prod.updateDate) DESC");
			break;
		case Constant.ProductLineOrderBy.PRICE_HIGH_TO_LOW:
			query.append(" ORDER BY avg(prod.price) DESC");
			break;
		case Constant.ProductLineOrderBy.PRICE_LOW_TO_HIGH:
			query.append(" ORDER BY avg(prod.price)");
			break;
		}

		// System.out.println(query.toString());
		// countQuery
		StringBuilder countQuery = new StringBuilder();
		countQuery.append(" SELECT 	 count(a.name) ");
		countQuery.append(" FROM	(SELECT 	 p.name ");
		countQuery.append(" 		FROM  	 product_line p ");
		countQuery.append(" 		JOIN  	 Category c ");
		countQuery.append(" 		ON 		 c.id = p.category_id ");
		countQuery.append(" 		JOIN  	 Product prod ");
		countQuery.append(" 		ON  		 prod.product_line_id = p.id ");
		countQuery.append(categoriesQuery);
		countQuery.append(" 		AND  	 p.name LIKE" + keyword);
		countQuery.append(" 		AND  	 prod.price >= " + minPrice);
		countQuery.append(" 		AND  	 prod.price <= " + maxPrice);
		countQuery.append("			GROUP BY p.name ) a");

		// List Result
		TypedQuery<ProductLineDto> q = entityManager.createQuery(query.toString(), ProductLineDto.class);
		q.setFirstResult((pageNumber) * pageSize);
		q.setMaxResults(pageSize);
		List<ProductLineDto> list = q.getResultList();

		// totalElements
		Query countQ = entityManager.createNativeQuery(countQuery.toString());
		Integer totalElements = (Integer) countQ.getSingleResult();
		Integer totalPages = (int) Math.ceil(totalElements / (float) pageSize);
		System.out.println(totalPages+"xxx");

		PageData<ProductLineDto> pageData = new PageData<>();
		pageData.setType(ProductLineDto.class);
		pageData.setContent(list);
		pageData.setTotalPages(totalPages);

		return pageData;
	}
	
	// find BestSeller of Shop
	@Override
	public PageData<ProductLineDto> findBestSellerProductLineDto(PageInfo pageInfo) {

		// get pageNumber & Page Size & Keyword
		Integer pageNumber = pageInfo.getPageNumber();
		Integer pageSize = pageInfo.getPageSize();

		// bodyQuery
		StringBuilder bodyQuery = new StringBuilder();
		bodyQuery.append(" FROM  	 ProductLine p ");
		bodyQuery.append(" JOIN  	 Product prod ");
		bodyQuery.append(" ON  		 prod.productLine.id = p.id ");
		bodyQuery.append(" GROUP BY  p.id, p.name, p.imgUrl1, p.discount");
		// default order By bestSeller
		bodyQuery.append(" 				ORDER BY sum(prod.sold) DESC");

		// query
		StringBuilder query = new StringBuilder();
		query.append(
				" SELECT 	 new ptit.com.model.ProductLineDto(p.id, p.name, p.imgUrl1, min(prod.price), max(prod.price), sum(prod.sold), p.discount  )");
		query.append(bodyQuery);

		// countQuery
		StringBuilder countQuery = new StringBuilder();
		countQuery.append(" SELECT 	 count(a.name) ");
		countQuery.append(" FROM ( SELECT 	 p.name");
		countQuery.append(" 		FROM  	 product_line p ");
		countQuery.append(" 		JOIN  	 Product prod ");
		countQuery.append(" 		ON  	 prod.product_line_id = p.id ");
		countQuery.append(" 		GROUP BY  p.name ) a");

		// totalElements
		Query countQ = entityManager.createNativeQuery(countQuery.toString());
		Integer totalElements = (Integer) countQ.getSingleResult();
		Integer totalPages = (int) Math.ceil(totalElements / (float) pageSize);

		// List Result
		TypedQuery<ProductLineDto> q = entityManager.createQuery(query.toString(), ProductLineDto.class);
		q.setFirstResult((pageNumber) * pageSize);
		q.setMaxResults(pageSize);
		List<ProductLineDto> list = q.getResultList();

		PageData<ProductLineDto> pageData = new PageData<>();
		pageData.setType(ProductLineDto.class);
		pageData.setContent(list);
		pageData.setTotalPages(totalPages);

		return pageData;
	}
	
	
	
}
