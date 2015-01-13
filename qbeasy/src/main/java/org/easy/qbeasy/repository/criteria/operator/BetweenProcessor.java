package org.easy.qbeasy.repository.criteria.operator;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.exception.OperatorException;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.repository.criteria.CriteriaOperatorProcessorBase;
import org.hibernate.criterion.Restrictions;

/**
 * Processador do operador {@link Between} 
 * @author augusto
 */
@SuppressWarnings("serial")
public class BetweenProcessor extends CriteriaOperatorProcessorBase<Object> {

	@Override
	protected void executeOperation(String property, Operator<Object> operator, Object... values) {
		getJunction().add(Restrictions.between(property, values[0], values[1]));
	}

	/**
	 * Para este operador devem ser informados exatamente 2 valores, ambos do tipo Date ou Number.
	 */
	@Override
	protected void validate(String property, Operator<Object> operator, Object[] values) {
		super.validate(property, operator, values);
		if (values.length != 2 
				&& (!(OperatorProcessorUtil.isDate(values[0]) && OperatorProcessorUtil.isDate(values[1])) // é data
						|| !(OperatorProcessorUtil.isNumber(values[0]) && OperatorProcessorUtil.isNumber(values[1]) // ou é número
								))) {
			throw new OperatorException("O operador Between exige a informação de 2 parâmetros do tipo Date ou Number.");
		}
	}
}
