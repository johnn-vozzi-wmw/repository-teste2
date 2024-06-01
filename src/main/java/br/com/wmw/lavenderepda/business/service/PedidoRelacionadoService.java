package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoRelacionado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoRelacionadoPdbxDao;
import totalcross.util.Vector;

public class PedidoRelacionadoService extends CrudPersonLavendereService {

	private static PedidoRelacionadoService instance;
	public static int validationFechamentoCount;
	public static int validationFechamentoListCount;
	public Vector pedidoEnvioServidorList = new Vector();
	public boolean ignoreQuantidadeMinimaCaixasPedido;
	
	private PedidoRelacionadoService() {
		//--
	}

	public static PedidoRelacionadoService getInstance() {
		if (PedidoRelacionadoService.instance == null) {
			PedidoRelacionadoService.instance = new PedidoRelacionadoService();
		}
		return PedidoRelacionadoService.instance;
	}
	
	@Override
	protected CrudDao getCrudDao() {
		return PedidoRelacionadoPdbxDao.getInstance();
	}
	
	public void insereMultiplosPedidosRelacionados(Pedido pedido, Vector pedidosRelacionados) throws SQLException {
		int size = pedidosRelacionados.size();
		if (size == 0 ) return;
		deletePedidosRelacionados(pedido);
		PedidoRelacionado pedidoRel = new PedidoRelacionado();
		pedidoRel.cdEmpresa = pedido.cdEmpresa;
		pedidoRel.cdRepresentante = pedido.cdRepresentante;
		pedidoRel.nuPedido = pedido.nuPedido;
		pedidoRel.flOrigemPedido = pedido.flOrigemPedido;
		pedidoRel.flTipoAlteracao = PedidoRelacionado.FLTIPOALTERACAO_INSERIDO;
		pedidoRel.cdUsuario = pedido.cdUsuario;
		if (pedido.isPedidoVendaProducao()) pedidoRel.flTipoRelacionamento = PedidoRelacionado.FLTIPORELACIONAMENTO_PEDIDO_PRODUCAO;
		for (int i = 0; i < size; i++) {
			Pedido pedidoRelacionado = (Pedido) pedidosRelacionados.items[i];
			pedidoRel.cdEmpresaRel = pedidoRelacionado.cdEmpresa;
			pedidoRel.cdRepresentanteRel = pedidoRelacionado.cdRepresentante;
			pedidoRel.nuPedidoRel = pedidoRelacionado.nuPedido;
			pedidoRel.flOrigemPedidoRel = pedidoRelacionado.flOrigemPedido;
			PedidoRelacionadoPdbxDao.getInstance().insert(pedidoRel);
		}
	}
	
	public void insereUnicoPedidoRelacionado(Pedido pedido, Pedido pedidoRelacionado) throws SQLException {
		if (pedidoRelacionado == null) return;
		deletePedidosRelacionados(pedido);
		PedidoRelacionado pedidoRel = new PedidoRelacionado();
		pedidoRel.cdEmpresa = pedido.cdEmpresa;
		pedidoRel.cdRepresentante = pedido.cdRepresentante;
		pedidoRel.nuPedido = pedido.nuPedido;
		pedidoRel.flOrigemPedido = pedido.flOrigemPedido;
		pedidoRel.cdEmpresaRel = pedidoRelacionado.cdEmpresa;
		pedidoRel.cdRepresentanteRel = pedidoRelacionado.cdRepresentante;
		pedidoRel.nuPedidoRel = pedidoRelacionado.nuPedido;
		pedidoRel.flOrigemPedidoRel = pedidoRelacionado.flOrigemPedido;
		pedidoRel.flTipoAlteracao = PedidoRelacionado.FLTIPOALTERACAO_INSERIDO;
		pedidoRel.cdUsuario = pedido.cdUsuario;
		if (pedido.isPedidoVendaProducao()) pedidoRel.flTipoRelacionamento = PedidoRelacionado.FLTIPORELACIONAMENTO_PEDIDO_PRODUCAO;
		PedidoRelacionadoPdbxDao.getInstance().insert(pedidoRel);
	}
	
	public Vector getPedidosRelacionadosByPedido(Pedido pedido) throws SQLException {
		return PedidoRelacionadoPdbxDao.getInstance().findPedidosRelacionadosByPedido(pedido);
	}
	
	public void deletePedidosRelacionados(Pedido pedido) throws SQLException {
		Vector pedidoRelList = PedidoRelacionadoService.getInstance().getPedidosRelacionadosByPedido(pedido);
		if (ValueUtil.isEmpty(pedidoRelList)) return;
		PedidoRelacionado pedidoRelacionado = new PedidoRelacionado();
		pedidoRelacionado.cdEmpresa = pedido.cdEmpresa;
		pedidoRelacionado.cdRepresentante = pedido.cdRepresentante;
		pedidoRelacionado.nuPedido = pedido.nuPedido;
		pedidoRelacionado.flOrigemPedido = pedido.flOrigemPedido;
		if (pedido.isPedidoVendaProducao()) pedidoRelacionado.flTipoRelacionamento = PedidoRelacionado.FLTIPORELACIONAMENTO_PEDIDO_PRODUCAO;
		int size = pedidoRelList.size();
		for (int i = 0; i < size; i++) {
			Pedido pedidoRel = (Pedido) pedidoRelList.items[i];
			pedidoRelacionado.cdEmpresaRel = pedidoRel.cdEmpresa;
			pedidoRelacionado.cdRepresentanteRel = pedidoRel.cdRepresentante;
			pedidoRelacionado.nuPedidoRel = pedidoRel.nuPedido;
			pedidoRelacionado.flOrigemPedidoRel = pedidoRel.flOrigemPedido;
			delete(pedidoRelacionado);
		}
	}

	public Pedido findUnicoPedidoRelacionadoByPedido(Pedido pedido) throws SQLException {
		return PedidoRelacionadoPdbxDao.getInstance().findUnicoPedidoRelacionadoByPedido(pedido);
	}

}
