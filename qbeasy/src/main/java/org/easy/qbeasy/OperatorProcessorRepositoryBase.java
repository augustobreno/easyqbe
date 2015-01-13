package org.easy.qbeasy;

import java.util.HashMap;
import java.util.Map;

import org.easy.qbeasy.api.Operator;
import org.easy.qbeasy.api.OperatorProcessorRepository;
import org.easy.qbeasy.repository.criteria.OperatorProcessor;

/**
 * Implementação concreta de um DAO JPA, criada para minimizar a necessidade de criação
 * de DAOs concretos para cada entidade de domínio.
 * @author augusto
 *
 */
@SuppressWarnings("serial")
public class OperatorProcessorRepositoryBase implements OperatorProcessorRepository { 

	@SuppressWarnings("rawtypes")
	private Map<Class<? extends Operator>, Class<? extends OperatorProcessor>> processors;
	
	@SuppressWarnings("rawtypes")
	public OperatorProcessorRepositoryBase() {
		processors = new HashMap<Class<? extends Operator>, Class<? extends OperatorProcessor>>();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void register(Class<? extends Operator> operatorType, Class<? extends OperatorProcessor> processorType) {
		processors.put(operatorType, processorType);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<? extends OperatorProcessor> getProcessor(Class<? extends Operator> operatorType) {
		return processors.get(operatorType);
	}

}
