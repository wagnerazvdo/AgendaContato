package br.com.acme.agenda.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.acme.agenda.model.Contato;
import br.com.acme.agenda.service.ContatoService;
import br.com.acme.agenda.service.ContatoServiceImpl;
import br.com.acme.agenda.utils.Constantes;

@WebServlet("/contatoControllerServlet")
public class ContatoControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Contato contato;
	private ContatoService service;
	private List<Contato> contatos;

	public ContatoControllerServlet() {
		//this.setContato(new Contato());
		this.contato = new Contato();
		this.service = new ContatoServiceImpl();
		this.contatos = new ArrayList<Contato>();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// switch
		String id = request.getParameter("id");
		String action = request.getParameter("action");
		String fuction = request.getParameter("fuction");
		String ativar = request.getParameter("ativar");
		
		if (action != null) {
			this.service.remover(Long.parseLong(id));
		}
		if (fuction != null) {
			this.service.editarContato(Long.parseLong(id));
		}
		if (ativar != null) {
			this.service.ativarDesativarContato(Long.parseLong(id));
			request.setAttribute("ativar/desativar", "Contato desativado/ativado cadastrado com sucesso");
		}
		this.contatos = this.service.listarContatos();
		RequestDispatcher rd = request.getRequestDispatcher(Constantes.LISTAR_CONTATOS);
		request.setAttribute("contatos", this.contatos);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.contato = new Contato(); // contato instanciado

		this.contato.setNome(request.getParameter("nome"));
		this.contato.setEmail(request.getParameter("email"));
		this.contato.setTelefone(request.getParameter("telefone"));
		this.contato.setAtivo(true);
		this.service.salvar(this.contato);
		response.getWriter().append("Salvo com sucesso!");

	}

}