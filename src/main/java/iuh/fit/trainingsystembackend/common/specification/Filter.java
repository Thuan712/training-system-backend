package com.thinkvitals.common.specification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode
@ToString
public class Filter {
	private FilterType type = FilterType.PROPERTY;
	private String property;
	private FilterOperation operation;
	private Object value;
	private List<Filter> filters = new ArrayList<>();
	private boolean useJoin = false;
	private JoinProperty joinProperty;

    public Filter() {
    }

    public Filter(String property, FilterOperation operation, Object value) {
        this.property = property;
        this.operation = operation;
        this.value = value;
    }

	public Filter(String property, FilterOperation operation, Object value, boolean useJoin, JoinProperty joinProperty) {
		this.property = property;
		this.operation = operation;
		this.value = value;
		this.useJoin = useJoin;
		this.joinProperty = joinProperty;
	}

	public Filter(List<Filter> filters){
		this.type = FilterType.OR;
		this.filters = filters;
	}

	/**
	 * @param value the value to set
	 */
	public void setStringValue(String value) {
		this.value = value;
	}

	/**
	 * Sets for Long values
	 * @param value
	 */
	public void setLongValue(Long value) {
		this.value = value;
	}

	/**
	 * Sets for Integer values
	 * @param value
	 */
	public void setIntValue(Integer value) {
		this.value = value;
	}

	/**
	 * Sets for Boolean values
	 * @param value
	 */
	public void setBoolValue(Boolean value) {
		this.value = value;
	}

	/**
	 * Sets for Date values
	 * @param value
	 */
	public void setDateValue(Date value) {
		this.value = value;
	}

	/**
	 * Sets for Float values
	 * @param value
	 */
	public void setFloatValue(Float value) {
		this.value = value;
	}

	/**
	 * When the value represents a range, then it
	 * is actually a list of 2 elements
	 *
	 * @return
	 */
	public Object getRangedValue() {
		return (List<Object>) value;
	}
}

