package com.macro.mall.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.macro.mall.common.IdUtil;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yyq
 */
@Data
public abstract class AbstractEntity implements Entity {
	private static final long serialVersionUID = -2223925633174050597L;

	protected Long id = IdUtil.nextId();

	/** 版本 */
	@JsonIgnore
	private int version;
	/** 创建时间 */
	@JsonIgnore
	protected LocalDateTime createDate;
	/** 修改时间 */
	@JsonIgnore
	protected LocalDateTime editDate;
	/** 创建人 */
	@JsonIgnore
	protected String creator;
	/** 修改人 */
	@JsonIgnore
	protected String editor;
	/** 是否删除 */
	@JsonIgnore
	protected Integer isDel;
	/** 备注 */
	@JsonIgnore
	protected String remark;

	/**
	 * 获取ID
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean sameValueAs(Object other) {
		return false;
	}
}
