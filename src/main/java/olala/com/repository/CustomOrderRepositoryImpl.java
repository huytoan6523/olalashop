package olala.com.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import olala.com.entities.Orders;
import olala.com.model.OrderInfo;
import olala.com.model.PageData;
import olala.com.model.StatDto;
import olala.com.model.StatInfo;

@Transactional
public class CustomOrderRepositoryImpl implements CustomOrderRepository {
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public PageData<Orders> findByOrderInfo(OrderInfo orderInfo) {

		// get pageNumber & Page Size & Keyword
		Integer pageNumber = orderInfo.getPageNumber();
		Integer pageSize = orderInfo.getPageSize();

		// userId
		String userId = "";
		if (orderInfo.getUserId() != null) {
			userId = orderInfo.getUserId();
		}
		// formDate
		LocalDate fromDate = orderInfo.getFromDate();

		// toDate
		LocalDate toDate = orderInfo.getToDate();

		// status
		Integer status = orderInfo.getStatus();

		// bodyQuery
		StringBuilder query = new StringBuilder();
		query.append(" SELECT  	 o ");
		query.append(" FROM  	 Orders o ");
		query.append(" JOIN  	 Users u ");
		query.append(" 			ON 		 o.users.id = u.id");
		query.append(" WHERE  	 o.status = " + status);
		query.append(" 		AND  o.createdDate	BETWEEN 	" + "'" + 
				fromDate + " 00:00:00'" + " AND " + "'" + toDate + " 23:59:59'");
		// check if has UserId
		if (!orderInfo.getUserId().isEmpty()) {
			query.append(" AND 		 u.id = " + userId);
		}

		// bodyQuery
		StringBuilder countQuery = new StringBuilder();
		countQuery.append(" SELECT  	 count(o.id) ");
		countQuery.append(" FROM  	 Orders o ");
		countQuery.append(" JOIN  	 Users u ");
		countQuery.append(" 		ON 		 o.users.id = u.id");
		countQuery.append(" WHERE  	 o.status = " + status);
		countQuery.append(" 		AND  o.createdDate	BETWEEN 	" + "'" + 
				fromDate + " 00:00:00'" + " AND " + "'" + toDate + " 23:59:59'");
		// check if has UserId
		if (!orderInfo.getUserId().isEmpty()) {
			countQuery.append(" AND 		 u.id = " + userId);
		}

		Query countQ = entityManager.createQuery(countQuery.toString());
		Long totalElements = (Long) countQ.getSingleResult();
		Integer totalPages = (int) Math.ceil(totalElements / (float) pageSize);

		// List Result
		TypedQuery<Orders> q = entityManager.createQuery(query.toString(), Orders.class);
		q.setFirstResult((pageNumber) * pageSize);
		q.setMaxResults(pageSize);
		List<Orders> list = q.getResultList();

		PageData<Orders> pageData = new PageData<>();
		pageData.setType(Orders.class);
		pageData.setContent(list);
		pageData.setTotalPages(totalPages);

		return pageData;
	}

	@Override
	public List<StatDto> findStatByStatInfo(StatInfo statInfo) {
		
		String productLineName = "'%" +statInfo.getProductName() +"%'";
		LocalDate fromDate = statInfo.getFromDate();
		LocalDate toDate = statInfo.getToDate();
		
		StringBuilder query = new StringBuilder();
		query.append(" SELECT  new ptit.com.model.StatDto(p.id, pl.name,pl.imgUrl1,p.price, sum(p.sold), sum(p.sold)*p.price)");
		query.append(" FROM  	 Orders o ");
		query.append(" JOIN  	 ProductItem pi ");
		query.append(" 		ON 		 o.id = pi.orders.id");
		query.append(" JOIN		 Product p ");
		query.append("   	ON		 p.id = pi.product.id ");
		query.append(" JOIN		 ProductLine pl ");
		query.append("   	ON		 pl.id = p.productLine.id ");
		query.append("  WHERE pl.name LIKE "+productLineName);
		query.append(" 		AND  o.createdDate	BETWEEN 	" + "'" + 
				fromDate + " 00:00:00'" + " AND " + "'" + toDate + " 23:59:59'");
		query.append("  	AND o.status = 3");
		query.append(" GROUP BY  pl.name, pl.imgUrl1,p.price, p.id ");
		if( statInfo.getSortType().equals("bestSeller")) {
			query.append(" ORDER BY sum(p.sold) DESC");
		} else {
			query.append(" ORDER BY sum(p.sold)*p.price DESC");
		}
		// check if has UserId
		TypedQuery<StatDto> q = entityManager.createQuery(query.toString(), StatDto.class);
		List<StatDto> list = q.getResultList();
		return list;
	}
	
	
	
	

}
