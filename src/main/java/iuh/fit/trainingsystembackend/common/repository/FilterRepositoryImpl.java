package com.thinkvitals.common.repository;

import com.thinkvitals.common.specification.Filter;
import com.thinkvitals.common.specification.FilterType;
import com.thinkvitals.common.specification.JoinProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Repository
public class FilterRepositoryImpl implements FilterRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Long getFilteredCount(List<Filter> filters, String tClass) throws IllegalArgumentException {
        // get sub-queries
        final Map<String, JoinProperty> joinTables = getFilterJoinTables(filters);

        String className = "";
        if (tClass.lastIndexOf('.') > 0) {
            className = tClass.substring( tClass.lastIndexOf('.') + 1 );
        }

        String mainAlias = className.substring(0,1).toLowerCase() + className.substring(1);

        StringBuffer queryBuffer = new StringBuffer();

        // SELECT CLAUSE
        queryBuffer.append(" SELECT COUNT(" + mainAlias + ") FROM " + className + " AS " + mainAlias + " ");

        // JOIN TABLE IF EXIST
        if(!joinTables.isEmpty()){
            queryBuffer.append(buildQueryJoinTable(joinTables));
        }

        // WHERE CLAUSE
        queryBuffer.append(buildQueryFilterCondition(filters, mainAlias));

        Query query = em.createQuery(queryBuffer.toString());

        // set the parameters
        setQueryParameters(query, mainAlias, filters);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<Long> getIdFiltered(List<Filter> filters, String tClass) throws IllegalArgumentException {
        // get sub-queries
        final Map<String, JoinProperty> joinTables = getFilterJoinTables(filters);

        String className = "";
        if (tClass.lastIndexOf('.') > 0) {
            className = tClass.substring( tClass.lastIndexOf('.') + 1 );
        }

        String mainAlias = className.substring(0,1).toLowerCase() + className.substring(1);

        StringBuffer queryBuffer = new StringBuffer();

        // SELECT CLAUSE
        queryBuffer.append(" SELECT DISTINCT " + mainAlias.concat(".id"));
        // FROM CLAUSE
        queryBuffer.append(" FROM " + className + " AS " + mainAlias + " ");

        // JOIN TABLE IF EXIST
        if(!joinTables.isEmpty()){
            queryBuffer.append(buildQueryJoinTable(joinTables));
        }

        // WHERE CLAUSE
        queryBuffer.append(buildQueryFilterCondition(filters, mainAlias));

        Query query = em.createQuery(queryBuffer.toString());

        // set the parameters
        setQueryParameters(query, mainAlias, filters);

        return (List<Long>) query.getResultList();
    }

    @Override
    public <T> List<T> getFiltered(List<Filter> filters, Pageable pageable, Class<T> tClass) throws IllegalArgumentException {
        // get sub-queries
        final Map<String, JoinProperty> joinTables = getFilterJoinTables(filters);

        String className = tClass.getName();
        if (className.lastIndexOf('.') > 0) {
            className = className.substring( className.lastIndexOf('.') + 1 );
        }

        String mainAlias = className.substring(0,1).toLowerCase() + className.substring(1);

        StringBuffer queryBuffer = new StringBuffer();

        // SELECT CLAUSE
        queryBuffer.append(" SELECT DISTINCT " + mainAlias);
        // FROM CLAUSE
        queryBuffer.append(" FROM " + className + " AS " + mainAlias + " ");

        // JOIN TABLE IF EXIST
        if(!joinTables.isEmpty()){
            queryBuffer.append(buildQueryJoinTable(joinTables));
        }

        // WHERE CLAUSE
        queryBuffer.append(buildQueryFilterCondition(filters, mainAlias));

        // ORDER BY
        queryBuffer.append(buildQueryOrder(mainAlias, pageable));

        Query query = em.createQuery(queryBuffer.toString(), tClass);

        // set the parameters
        setQueryParameters(query, mainAlias, filters);

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    private Map<String, JoinProperty> getFilterJoinTables(List<Filter> filters){
        Map<String, JoinProperty> joinTables = new HashMap<>();

        for (Filter filter : filters) {
            if (filter.isUseJoin() && filter.getJoinProperty()!=null) {
                joinTables.put(filter.getJoinProperty().getJoinClass(), filter.getJoinProperty());
            }

            for(Filter orFilter : filter.getFilters()){
                if (orFilter.isUseJoin() && orFilter.getJoinProperty()!=null && joinTables.get(orFilter.getJoinProperty().getJoinClass())!=null) {
                    joinTables.put(orFilter.getJoinProperty().getJoinClass(), orFilter.getJoinProperty());
                }
            }
        }

        return joinTables;
    }

    private String buildQueryJoinTable(Map<String, JoinProperty> joinTables){
        StringBuilder stringBuilder = new StringBuilder();

        joinTables.forEach((k,v) -> {
//            String joinAlias = k.substring(0,1).toLowerCase() + k.substring(1);
//            String sourceAlias = v.getSourceClass().substring(0,1).toLowerCase() + v.getSourceClass().substring(1);

            stringBuilder.append(v.getJoinType() + " JOIN " + k + " AS " + v.getJoinAlias() + " ");
            stringBuilder.append("ON " + v.getSourceAlias() + "." + v.getSourceProp() + " = " + v.getJoinAlias() + "." + v.getJoinProp() + " ");
        });

        return stringBuilder.toString();
    }

    private String buildFilterCondition(String mainAlias, Filter filter){
        /*
         *  Set the filter property.
         *  There are 3 cases here :
         *   - when we have a direct field with no .
         *   - when we have a sub-field which contains a .
         */
        String property = "";

        if(filter.getProperty()!=null && !filter.getProperty().isEmpty()){
            if(filter.isUseJoin()){
                property = filter.getJoinProperty().getJoinAlias().concat(".").concat(filter.getProperty());
            }else{
                property = mainAlias.concat(".").concat(filter.getProperty());
            }
        }

        String properyParam = property.replaceAll("\\.", "_");

        StringBuilder stringBuilder = new StringBuilder();

        switch (filter.getOperation()){
            case BETWEEN:
                stringBuilder.append(property + " BETWEEN :" + properyParam + "0 AND " + properyParam + "1");
                break;
            case CONTAINS:
                stringBuilder.append(property + " LIKE :" + properyParam);
                break;
            case IN:
                stringBuilder.append(property + " IN (:" + properyParam + ")");
                break;
            case NOT_IN:
                stringBuilder.append(property + " NOT IN (:" + properyParam + ")");
                break;
            case IS_NULL:
                stringBuilder.append(property + " IS NULL");
                break;
            case IS_NOT_NULL:
                stringBuilder.append(property + " IS NOT NULL");
                break;
            case IS_EMPTY:
                stringBuilder.append(property + "=''");
                break;
            case IS_NOT_EMPTY:
                stringBuilder.append(property + "!=''");
                break;
            case NOT:
                stringBuilder.append(property + "!=:" + properyParam);
                break;
            case EQUAL:
                stringBuilder.append(property + "=:" + properyParam);
                break;
            default:
                break;
        }

        return stringBuilder.toString();
    }

    private String buildQueryFilterCondition(List<Filter> filters, String mainAlias){
        StringBuilder stringBuilder = new StringBuilder();

        if (filters.size() > 0) stringBuilder.append("WHERE ");

        boolean first = true;
        for (Filter filter : filters) {
            if (!first) stringBuilder.append(" AND ");

            switch (filter.getType()){
                case PROPERTY:
                    stringBuilder.append(buildFilterCondition(mainAlias, filter));
                    break;
                case OR:
                    stringBuilder.append(" (");
                    boolean firstOr = true;
                    for(Filter orFilter : filter.getFilters()){
                        if(!firstOr) stringBuilder.append(" OR ");
                        stringBuilder.append(buildFilterCondition(mainAlias, orFilter));
                        firstOr = false;
                    }
                    stringBuilder.append(" )");
                    break;
                default:
                    break;
            }

            first = false;
        }

        return stringBuilder.toString();
    }

    private String buildQueryOrder(String mainAlias, Pageable pageable){
        StringBuilder stringBuilder = new StringBuilder();

        Sort sort = pageable.getSort();

        StringBuilder stringSort = new StringBuilder();

        for (Iterator<Sort.Order> iter = sort.iterator(); iter.hasNext(); ){
            Sort.Order order=iter.next();
            stringSort.append(mainAlias.concat(".").concat(order.getProperty()).concat(" ").concat(order.getDirection().name()));
        }

        if(!stringSort.toString().isEmpty()){
            stringBuilder.append(" ORDER BY ");
            stringBuilder.append(stringSort.toString());
        }

        return stringBuilder.toString();
    }

    private void setFilterParameter(Query query, String mainAlias, Filter filter){
        String property = "";

        if(filter.getProperty()!=null && !filter.getProperty().isEmpty()){
            if(filter.isUseJoin()){
                property = filter.getJoinProperty().getJoinAlias().concat(".").concat(filter.getProperty());
            }else{
                property = mainAlias.concat(".").concat(filter.getProperty());
            }
        }

        String properyParam = property.replaceAll("\\.", "_");

        switch (filter.getOperation()){
            case BETWEEN:
                List<Object> values = (List<Object>) filter.getValue();
                query.setParameter(properyParam.concat("0"), values.get(0));
                query.setParameter(properyParam.concat("1"), values.get(1));
                break;
            case CONTAINS:
                query.setParameter(properyParam, "%" + filter.getValue().toString() + "%");
                break;
            case IN:
            case NOT_IN:
            case EQUAL:
            case NOT:
                query.setParameter(properyParam, filter.getValue());
                break;
            default:
                break;
        }
    }

    private void setQueryParameters(Query query, String mainAlias, List<Filter> filters){
        for (Filter filter : filters) {
            if (filter.getType().equals(FilterType.PROPERTY)) {
                setFilterParameter(query, mainAlias, filter);
            }
            if (filter.getType().equals(FilterType.OR) && filter.getFilters().size() > 0){
                for(Filter orFilter : filter.getFilters()){
                    setFilterParameter(query, mainAlias, orFilter);
                }
            }
        }
    }
}
