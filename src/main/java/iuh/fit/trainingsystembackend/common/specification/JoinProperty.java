package iuh.fit.trainingsystembackend.common.specification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class JoinProperty {
	private String joinClass;
	private String joinAlias;
	private String joinProp;
	private JoinType joinType = JoinType.INNER;
	private String sourceClass;
	private String sourceAlias;
	private String sourceProp;

    public JoinProperty() {
    }

	public JoinProperty(String joinClass, String joinProp, JoinType joinType, String sourceClass, String sourceProp) {
		this.joinClass = joinClass.substring(joinClass.lastIndexOf('.') + 1 );
		this.joinAlias = this.joinClass.substring(0,1).toLowerCase() + this.joinClass.substring(1);
		this.joinProp = joinProp;
		this.joinType = joinType;
		this.sourceClass = sourceClass.substring(sourceClass.lastIndexOf('.') + 1 );
		this.sourceAlias = this.sourceClass.substring(0,1).toLowerCase() + this.sourceClass.substring(1);
		this.sourceProp = sourceProp;
	}
}

