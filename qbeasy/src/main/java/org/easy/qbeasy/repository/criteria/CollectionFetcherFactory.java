package org.easy.qbeasy.repository.criteria;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.easy.qbeasy.api.Filter;
import org.easy.qbeasy.api.Identifiable;
import org.easy.qbeasy.api.OperatorProcessorRepository;
import org.easy.qbeasy.api.exception.QbeException;
import org.easy.qbeasy.repository.criteria.FetchesManualProcessor.PropertyGroup;
import org.easy.qbeasy.util.ReflectionUtil;
import org.easy.qbeasy.util.StringUtil;
import org.hibernate.Session;

/**
 * Fábrica de Fetchers, criando uma instância de acordo com as características
 * da propriedade para fetch.
 * 
 * @author augusto
 */
public class CollectionFetcherFactory {

	public static CollectionFetcher create(OperatorProcessorRepository operatorProcessorRepository, Session session,
			Map<Identifiable, Collection<Identifiable>> fetchResultMap, Filter<?> filter, PropertyGroup groupToFetch,
			List<?> inProjectionToCollectionFetch) {

		CollectionFetcher fetcher = null;

		if (isMappedByCollection(filter, groupToFetch)) {
			fetcher = new CollectionMappedByFetcher(operatorProcessorRepository, session, fetchResultMap, filter,
					groupToFetch, inProjectionToCollectionFetch);
		} else if (isJoinTableCollection(filter, groupToFetch)) {
			fetcher = new CollectionJoinTableFetcher(operatorProcessorRepository, session, fetchResultMap, filter,
					groupToFetch, inProjectionToCollectionFetch);
		} else {
			throw new QbeException(
					"Apenas é possível fazer fetch de uma coleção quando esta é mapeada com @JoinTable ou @OneToMany. "
					+ "Neste último caso também é necessário configurar a propriedade 'mappedBy'");
		}

		return fetcher;
	}

	private static boolean isMappedByCollection(Filter<?> filter, PropertyGroup groupToFetch) {
		/*
		 * Relacionamentos com collections são implementados, geralmente, com
		 * OneToMany
		 */
		String colProperty = groupToFetch.getPrimaryProperty().getProperty();
		try {
			Field colField = ReflectionUtil.getField(filter.getEntityClass(), colProperty);

			if (colField.isAnnotationPresent(OneToMany.class)) {
				OneToMany oneToMany = colField.getAnnotation(OneToMany.class);
				return !StringUtil.isStringEmpty(oneToMany.mappedBy());

			} else {
				throw new QbeException("Não foi encontrado a anotação @OneToMany na relação ." + colProperty);
			}

		} catch (QbeException e) {
			throw e;

		} catch (Exception e) {
			throw new QbeException("Não foi possível encontrar informações sobre a coleção "
					+ filter.getEntityClass().getSimpleName() + "." + colProperty, e);
		}
	}

	private static boolean isJoinTableCollection(Filter<?> filter, PropertyGroup groupToFetch) {

		String colProperty = groupToFetch.getPrimaryProperty().getProperty();
		try {
			Field colField = ReflectionUtil.getField(filter.getEntityClass(), colProperty);
			return colField.isAnnotationPresent(JoinTable.class);
		} catch (Exception e) {
			throw new QbeException("Não foi possível encontrar informações sobre a coleção "
					+ filter.getEntityClass().getSimpleName() + "." + colProperty, e);
		}
	}

}
