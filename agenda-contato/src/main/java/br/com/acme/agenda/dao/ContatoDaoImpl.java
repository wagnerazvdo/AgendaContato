package br.com.acme.agenda.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import br.com.acme.agenda.model.Contato;
import br.com.acme.agenda.service.ContatoService;
import br.com.acme.agenda.utils.JPAUtil;

/**
 * @author TERCEIRA CAMADA
 *
 */
public class ContatoDaoImpl implements ContatoDao {

	@PersistenceContext
	public EntityManager getEm() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("default");
		return factory.createEntityManager();
	}

	private Contato contato;
	private ContatoService service;
	private List<Contato> contatos;

	@Override
	public void salvar(Contato contato) { // salva o novo cadastro
		EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.merge(contato);
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Override
	public List<Contato> listarContatos() {
		EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();

		Query consulta = entityManager.createQuery("SELECT c FROM Contato c");

		@SuppressWarnings("unchecked")
		List<Contato> listaDeContatosDoBancoDeDados = consulta.getResultList();
		return listaDeContatosDoBancoDeDados;
	}

	@Override
	public void remover(Long idContato) {
		EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

		try {
			entityManager.getTransaction().begin();
			// vou no banco de dados recupero o contato e mando remover
			Contato contato = entityManager.find(Contato.class, idContato);
			entityManager.remove(contato);// para remover precisa passar a instancia "contato".
			entityManager.getTransaction().commit();
			entityManager.close();
		} catch (NoResultException e) {
			e.getMessage();
		} finally {
			entityManager.close();
		}
	}

	@Override
	public void editarContato(Long idContato, Contato contato) {
		EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin(); // Inicializando um gerenciador de entidade
		Contato contatoQueTavaNoBanco = entityManager.find(Contato.class, idContato);
		
		contatoQueTavaNoBanco.setAtivo(contato.isAtivo());
		contatoQueTavaNoBanco.setEmail(contato.getEmail());
		contatoQueTavaNoBanco.setTelefone(contato.getTelefone());
		contatoQueTavaNoBanco.setNome(contato.getNome());
		entityManager.merge(contatoQueTavaNoBanco);
		entityManager.getTransaction().commit(); // Fechando a transação
		entityManager.close();// Liberando os recursos da fábrica
	}

	

	@Override
	public Contato buscarPorIdContato(Long idContato) {

		EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		Contato contato = entityManager.find(Contato.class, idContato);
		entityManager.getTransaction().commit();
		entityManager.close();
		return contato;
	}

	@Override
	public Contato buscaContatoPorEmail(String email) {
		EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		try {
			return entityManager.createNamedQuery("Contato.buscaContatoPorEmail", Contato.class)
					.setParameter("email", email).getSingleResult();
		} catch (NoResultException e) {
			e.getMessage();
		}
		return buscaContatoPorEmail(email); // devem arrumar uma forma de retirar isso daqui
	}

	@Override
	public void ativarDesativarContato(Long id) {
		EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
		entityManager.getTransaction().begin();
		Contato contatoQueTavaNoBanco = entityManager.find(Contato.class, id);
		if (contatoQueTavaNoBanco != null) {
			contatoQueTavaNoBanco.setAtivo(false);
		}
		// entityManager.merge(contatoQueTavaNoBanco); // tive que comentar porque
		// atrapalha a função deletar
		entityManager.getTransaction().commit();
		entityManager.close();
	}
	
	
	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public ContatoService getService() {
		return service;
	}

	public void setService(ContatoService service) {
		this.service = service;
	}

	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}
}
