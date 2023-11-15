package iuh.fit.trainingsystembackend.common.repository;

import iuh.fit.trainingsystembackend.common.specification.Filter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FilterRepository {
    Long getFilteredCount(final List<Filter> filters, String className) throws IllegalArgumentException;
    List<Long> getIdFiltered(final List<Filter> filters, String className) throws IllegalArgumentException;
    <T> List<T> getFiltered(List<Filter> filters, Pageable pageable, Class<T> tClass) throws IllegalArgumentException;
}
