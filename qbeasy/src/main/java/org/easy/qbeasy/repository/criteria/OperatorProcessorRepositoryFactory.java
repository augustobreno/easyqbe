package org.easy.qbeasy.repository.criteria;

import org.easy.qbeasy.OperatorProcessorRepositoryBase;
import org.easy.qbeasy.api.OperatorProcessorRepository;
import org.easy.qbeasy.api.operator.Between;
import org.easy.qbeasy.api.operator.Equal;
import org.easy.qbeasy.api.operator.GreaterThan;
import org.easy.qbeasy.api.operator.In;
import org.easy.qbeasy.api.operator.IsEmpty;
import org.easy.qbeasy.api.operator.IsNotEmpty;
import org.easy.qbeasy.api.operator.IsNotNull;
import org.easy.qbeasy.api.operator.IsNull;
import org.easy.qbeasy.api.operator.LessThan;
import org.easy.qbeasy.api.operator.Like;
import org.easy.qbeasy.api.operator.LikePrefix;
import org.easy.qbeasy.api.operator.LikeSufix;
import org.easy.qbeasy.api.operator.NotEqual;
import org.easy.qbeasy.api.operator.NotIn;
import org.easy.qbeasy.api.operator.SizeEqual;
import org.easy.qbeasy.api.operator.SizeGreaterThan;
import org.easy.qbeasy.api.operator.SizeLessThan;
import org.easy.qbeasy.api.operator.SizeNotEqual;
import org.easy.qbeasy.repository.criteria.operator.BetweenProcessor;
import org.easy.qbeasy.repository.criteria.operator.EqualProcessor;
import org.easy.qbeasy.repository.criteria.operator.GreaterThanProcessor;
import org.easy.qbeasy.repository.criteria.operator.InProcessor;
import org.easy.qbeasy.repository.criteria.operator.IsEmptyProcessor;
import org.easy.qbeasy.repository.criteria.operator.IsNotEmptyProcessor;
import org.easy.qbeasy.repository.criteria.operator.IsNotNullProcessor;
import org.easy.qbeasy.repository.criteria.operator.IsNullProcessor;
import org.easy.qbeasy.repository.criteria.operator.LessThanProcessor;
import org.easy.qbeasy.repository.criteria.operator.LikePrefixProcessor;
import org.easy.qbeasy.repository.criteria.operator.LikeProcessor;
import org.easy.qbeasy.repository.criteria.operator.LikeSufixProcessor;
import org.easy.qbeasy.repository.criteria.operator.NotEqualProcessor;
import org.easy.qbeasy.repository.criteria.operator.NotInProcessor;
import org.easy.qbeasy.repository.criteria.operator.SizeEqualProcessor;
import org.easy.qbeasy.repository.criteria.operator.SizeGreaterThanProcessor;
import org.easy.qbeasy.repository.criteria.operator.SizeLessThanProcessor;
import org.easy.qbeasy.repository.criteria.operator.SizeNotEqualProcessor;

/**
 * Para produção de DAOs concretos inseridos em um contexto de injeção de dependência. 
 * @author augusto
 */
public class OperatorProcessorRepositoryFactory {

	/**
	 * @return Um repositório com todos os processadores conhecidos pré-cadastrados.
	 * @throws Exception
	 */
	public static OperatorProcessorRepository create() {

		OperatorProcessorRepositoryBase operatorProcessorRepositoryBase = 
				new OperatorProcessorRepositoryBase();
		
		operatorProcessorRepositoryBase.register(Between.class, BetweenProcessor.class);
		operatorProcessorRepositoryBase.register(Equal.class, EqualProcessor.class);
		operatorProcessorRepositoryBase.register(GreaterThan.class, GreaterThanProcessor.class);
		operatorProcessorRepositoryBase.register(In.class, InProcessor.class);
		operatorProcessorRepositoryBase.register(IsEmpty.class, IsEmptyProcessor.class);
		operatorProcessorRepositoryBase.register(IsNotEmpty.class, IsNotEmptyProcessor.class);
		operatorProcessorRepositoryBase.register(IsNotNull.class, IsNotNullProcessor.class);
		operatorProcessorRepositoryBase.register(IsNull.class, IsNullProcessor.class);
		operatorProcessorRepositoryBase.register(LessThan.class, LessThanProcessor.class);
		operatorProcessorRepositoryBase.register(Like.class, LikeProcessor.class);
		operatorProcessorRepositoryBase.register(LikePrefix.class, LikePrefixProcessor.class);
		operatorProcessorRepositoryBase.register(LikeSufix.class, LikeSufixProcessor.class);
		operatorProcessorRepositoryBase.register(NotEqual.class, NotEqualProcessor.class);
		operatorProcessorRepositoryBase.register(NotIn.class, NotInProcessor.class);
		operatorProcessorRepositoryBase.register(SizeEqual.class, SizeEqualProcessor.class);
		operatorProcessorRepositoryBase.register(SizeGreaterThan.class, SizeGreaterThanProcessor.class);
		operatorProcessorRepositoryBase.register(SizeLessThan.class, SizeLessThanProcessor.class);
		operatorProcessorRepositoryBase.register(SizeNotEqual.class, SizeNotEqualProcessor.class);
		
		return operatorProcessorRepositoryBase;
		
	}
}
