package org.easy.qbeasy;

import java.text.ParseException;
import java.util.List;

import org.easy.qbeasy.QBEFilter;
import org.easy.qbeasy.XorContainer;
import org.easy.qbeasy.api.Operation;
import org.easy.qbeasy.api.OperationContainer;
import org.easy.qbeasy.api.QBERepository;
import org.easy.qbeasy.api.OperationContainer.ContainerType;
import org.easy.qbeasy.api.operator.Operators;
import org.easy.qbeasy.domain.Pessoa;
import org.easy.qbeasy.repository.criteria.CriteriaQbeRepository;
import org.easy.qbeasy.repository.criteria.OperatorProcessorRepositoryFactory;
import org.junit.Test;

/**
 * Classe de teste para as varia��es do uso do recurso de container
 * das consultas com QBE.
 * @author augusto
 *
 */
public class ContainerTest extends QbeTestBase {

	/**
	 * O container default configurado na classe FiltroQBE deve utilizar o conector AND
	 * quando a consulta cont�m mais de um operador configurado.
	 */
	@Test
	public void testContainerDefaultAND() {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, null, "11111111111", null);
		Pessoa p2 = new Pessoa("p2", null, null, "22222222222", null);
		Pessoa p3 = new Pessoa("p3", null, null, "33333333333", null);
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
		
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where p.nome = ? and p.cpf = ?";
		List<Pessoa> hqlLista = searchAndValidate(hql, p2.getNome(), p2.getCpf());
		
		// o filtro utiliza o container ra�z AND como default
		Pessoa exemplo = new Pessoa(p2.getNome(), null, null, p2.getCpf(), null);
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);
	}
	
	/**
	 * Configura o container default para trabalhar com OR.
	 */
	@Test
	public void testContainerDefaultOR() {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, null, "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p2", null, null, "22222222222", "email@email.com");
		Pessoa p3 = new Pessoa("p3", null, null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
		
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where p.nome = ? or p.cpf = ? or p.email=?";
		List<Pessoa> hqlLista = searchAndValidate(hql, p2.getNome(), p2.getCpf(), p2.getEmail());
		
		// o filtro utiliza o container ra�z configurado com OR
		Pessoa exemplo = new Pessoa(p2.getNome(), null, null, p2.getCpf(), p2.getEmail());
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		filtro.setRootContainerType(ContainerType.OR);
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);
	}
	
	@Test
	public void testContainerOR() {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, null, "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p2", null, null, "22222222222", "email@email.com");
		Pessoa p3 = new Pessoa("p3", null, null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
		
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where p.email=? and (p.nome = ? or p.cpf = ?) ";
		List<Pessoa> hqlLista = searchAndValidate(hql, p2.getEmail(), p2.getNome(), p2.getCpf());
		
		// Configura o filtro
		Pessoa exemplo = new Pessoa(p2.getNome(), null, null, p2.getCpf(), p2.getEmail());
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		
		filtro.addOr(new Operation("nome", Operators.equal()),
					 new Operation("cpf", Operators.equal()));
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);
	}
	
	@Test
	public void testContainerNegacao() {
		
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, null, "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p2", null, null, "22222222222", "2@email.com");
		Pessoa p3 = new Pessoa("p3", null, null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
		
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where not (p.cpf = ? or p.email = ?) ";
		List<Pessoa> hqlLista = searchAndValidate(hql, p1.getCpf(), p3.getEmail());
		
		// Configura o filtro
		Pessoa exemplo = new Pessoa(null, null, null, p1.getCpf(), p3.getEmail());
		
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		filtro.setRootContainerType(ContainerType.OR);
		filtro.getRootContainer().negate();
		
		filtro.filterBy(new Operation("cpf", Operators.equal()),
					  	   new Operation("email", Operators.equal()));
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);		
	}
	
	@Test
	public void testContainerAND() {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, null, "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p2", null, null, "22222222222", "email@email.com");
		Pessoa p3 = new Pessoa("p3", null, null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
		
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where p.cpf=? or (p.nome = ? and p.email = ?) ";
		List<Pessoa> hqlLista = searchAndValidate(hql, p2.getCpf(), p2.getNome(), p2.getEmail());
		
		// Configura o filtro
		Pessoa exemplo = new Pessoa(p2.getNome(), null, null, p2.getCpf(), p2.getEmail());
		
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		filtro.setRootContainerType(ContainerType.OR);
		
		filtro.addAnd(new Operation("nome", Operators.equal()),
					  new Operation("email", Operators.equal()));
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);
	}
	
	@Test
	public void testContainerDuploAND_OR() {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, null, "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p2", null, null, "22222222222", "email@email.com");
		Pessoa p3 = new Pessoa("p3", null, null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
		
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where (p.cpf=? and p.email=?) or (p.nome = ? and p.email = ?) ";
		List<Pessoa> hqlLista = searchAndValidate(hql, p2.getCpf(), p2.getEmail(), p1.getNome(), p2.getEmail());
		
		// Configura o filtro
		Pessoa exemplo = new Pessoa(p1.getNome(), null, null, p2.getCpf(), p2.getEmail());
		
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		filtro.setRootContainerType(ContainerType.OR);
		
		filtro.addAnd(new Operation("cpf", Operators.equal()),
	  				  new Operation("email", Operators.equal()));
		
		filtro.addAnd(new Operation("nome", Operators.equal()),
				 	  new Operation("email", Operators.equal()));
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);
	}
	
	@Test
	public void testContainerDuploOR_AND() {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, null, "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p2", null, null, "22222222222", "email@email.com");
		Pessoa p3 = new Pessoa("p3", null, null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
		
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where (p.cpf=? or p.nome=?) and (p.cpf = ? or p.email = ?) ";
		List<Pessoa> hqlLista = searchAndValidate(hql, p2.getCpf(), p1.getNome(), p2.getCpf(), p1.getEmail());
		
		// Configura o filtro
		Pessoa exemplo = new Pessoa(p1.getNome(), null, null, p2.getCpf(), p2.getEmail());
		
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		
		filtro.addOr(new Operation("cpf", Operators.equal()),
	  				 new Operation("nome", Operators.equal()));
		
		filtro.addOr(new Operation("cpf", Operators.equal()),
				 	 new Operation("email", Operators.equal()));
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);
	}	
	
	@Test
	public void testContainerAninhadoOR_AND() throws ParseException {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, formatDate("01/01/2010"), "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p2", null, formatDate("01/01/2012"), "22222222222", "email@email.com");
		Pessoa p3 = new Pessoa("p3", null,                     null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
		
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where p.email=? and ( p.cpf=? or (p.dataNascimento is null  or p.dataNascimento > ?) ) ";
		List<Pessoa> hqlLista = searchAndValidate(hql, p2.getEmail(), p1.getCpf(), p1.getDataNascimento());
		
		// Configura o filtro
		Pessoa exemplo = new Pessoa(null, null, p2.getDataNascimento(), p2.getCpf(), p2.getEmail());
		
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		
		filtro.filterBy("email", Operators.equal());
		
		OperationContainer containerOR = filtro.addOr(new Operation("cpf", Operators.equal()));
		containerOR.addOr(new Operation("dataNascimento", Operators.isNotNull()),
				 	      new Operation("dataNascimento", Operators.greater()));
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);
	}
	
	@Test
	public void testContainerXOR2Operacoes() throws ParseException {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, formatDate("01/01/2010"), "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p1", null, formatDate("01/01/2012"), "22222222222", "email@email.com");
		Pessoa p3 = new Pessoa("p3", null,                     null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
	
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where (p.nome=? and not p.cpf=?) or (not p.nome=? and p.cpf=?) ";
		List<Pessoa> hqlLista = searchAndValidate(hql, p1.getNome(), p2.getCpf(), p1.getNome(), p2.getCpf());		

		Pessoa exemplo = new Pessoa(p1.getNome(), null, null , p2.getCpf(), null); // p1 e p3
		
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		
		XorContainer xor = new XorContainer();
		xor.addOperation(new Operation("nome", Operators.equal()), new Operation("cpf", Operators.equal()));
		filtro.addContainerOperation(xor);
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);		
	}
	
	@Test
	public void testContainerXOR3Operacoes() throws ParseException {
		// Cria algumas pessoas pra consulta
		Pessoa p1 = new Pessoa("p1", null, formatDate("01/01/2010"), "11111111111", "email@email.com");
		Pessoa p2 = new Pessoa("p1", null, formatDate("01/01/2012"), "22222222222", "email@email.com");
		Pessoa p3 = new Pessoa("p3", null,                     null, "22222222222", "email@email.com");
		getJpa().save(p1);
		getJpa().save(p2);
		getJpa().save(p3);
		getJpa().getEm().flush();
		getJpa().getEm().clear();
	
		// utiliza uma consulta hql para compara��o
		String hql = "from Pessoa p where (p.nome=? and not(p.cpf=? or p.dataNascimento is not null)) " +
				"or (p.cpf=? and not(p.nome=? or p.dataNascimento is not null)) " +
				"or (p.dataNascimento is not null and not(p.cpf=? or p.nome=?))";
		
		String nome = p1.getNome(); // "p1"
		String cpf = p2.getCpf();   // "22222222222"
		List<Pessoa> hqlLista = searchAndValidate(hql, nome, cpf, cpf, nome, cpf, nome); // p1, p3		

		Pessoa exemplo = new Pessoa(p1.getNome(), null, null , p2.getCpf(), null); 
		
		QBEFilter<Pessoa> filtro = new QBEFilter<Pessoa>(exemplo);
		
		XorContainer xor = new XorContainer();
		xor.addOperation(new Operation("nome", Operators.equal()), 
						new Operation("cpf", Operators.equal()),
						new Operation("dataNascimento", Operators.isNotNull()));
		filtro.addContainerOperation(xor);
		
		// realiza a mesma consulta com qbe
		QBERepository qbe = new CriteriaQbeRepository(getJpa().getEm(), OperatorProcessorRepositoryFactory.create());;
		List<Pessoa> qbeLista = qbe.search(filtro);
		
		// compara as listas
		assertContentEqual(hqlLista, qbeLista);		
	}	
	
}
