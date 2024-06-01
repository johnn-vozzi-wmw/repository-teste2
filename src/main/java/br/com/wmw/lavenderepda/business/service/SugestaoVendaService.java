package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoIndustria;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugestaoVendaDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class SugestaoVendaService extends CrudService {

	private static SugestaoVendaService instance;

	private SugestaoVendaService() {
		//--
	}

	public static SugestaoVendaService getInstance() {
		if (instance == null) {
			instance = new SugestaoVendaService();
		}
		return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return SugestaoVendaDbxDao.getInstance();
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public String getDsSugestaoVenda(String cdSugestaoVenda) throws SQLException {
		if (!ValueUtil.isEmpty(cdSugestaoVenda)) {
			SugestaoVenda sugestaoVendaFilter = new SugestaoVenda();
			sugestaoVendaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			sugestaoVendaFilter.cdSugestaoVenda = cdSugestaoVenda;
			//--
			String dsSugestaoVenda = SugestaoVendaDbxDao.getInstance().findColumnByRowKey(sugestaoVendaFilter.getRowKey(), "DSSUGESTAOVENDA");
			if (!ValueUtil.isEmpty(dsSugestaoVenda)) {
				return dsSugestaoVenda;
			}
		}
		return cdSugestaoVenda;
	}

	public boolean isHasSugestoesPendentesNoPedido(Pedido pedido, String flTipoSugestaoVenda, boolean sugestoesObrigatorias, boolean isFechamentoPedido) throws SQLException {
		return findAllSugestoesPendentesNoPedido(pedido, flTipoSugestaoVenda, sugestoesObrigatorias, isFechamentoPedido).size() > 0;
	}

	public Vector findAllSugestoesPendentesNoPedido(Pedido pedido, String flTipoSugestaoVenda, boolean sugestoesObrigatorias, boolean isFechamentoPedido) throws SQLException {
		Vector sugestaoList = findAllSugestoesValidas(flTipoSugestaoVenda, pedido.getCliente(), sugestoesObrigatorias, isFechamentoPedido);
		Vector sugestoesFinalList = new Vector(0);
		for (int i = 0; i < sugestaoList.size(); i++) {
			SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestaoList.items[i];
			if (SugestaoVendaService.getInstance().isSugestaoVendaPendente(sugestaoVenda, pedido)) {
				sugestoesFinalList.addElement(sugestaoVenda);
			}
		}
		return sugestoesFinalList;
	}

	public Vector findAllSugestoesValidas(String flTipoSugestaoVenda, Cliente cliente, boolean sugestoesObrigatorias, boolean isFechamentoPedido) throws SQLException {
		return findAllSugestoesValidas(flTipoSugestaoVenda, cliente, sugestoesObrigatorias, null, isFechamentoPedido);
	}

	private Vector findAllSugestoesValidas(String flTipoSugestaoVenda, Cliente cliente, boolean sugestoesObrigatorias, String cdEmpresa, boolean isFechamentoPedido) throws SQLException {
		Vector sugestoesVendaFinalList = VectorUtil.concatVectors(findAllSugestoesVendaComVigencia(flTipoSugestaoVenda, cliente, cdEmpresa), findAllSugestoesVendaSemVigencia(flTipoSugestaoVenda, cliente, cdEmpresa));
		sugestoesVendaFinalList = getSugestoesVendaByRep(sugestoesVendaFinalList, cdEmpresa);
		sugestoesVendaFinalList = getSugestoesByObrigatoriedade(sugestoesVendaFinalList, sugestoesObrigatorias);
		sugestoesVendaFinalList = getSugestoesByRamoAtividade(sugestoesVendaFinalList, cliente.cdRamoAtividade, false);
		if (!ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedido)) {
			sugestoesVendaFinalList = getSugestoesByflFechamento(sugestoesVendaFinalList, isFechamentoPedido);
		}
		return sugestoesVendaFinalList;
	}

	public Vector findAllSugestoesValidas(String flTipoSugestaoVenda, Cliente cliente, boolean sugestoesObrigatorias) throws SQLException {
		return findAllSugestoesValidas(flTipoSugestaoVenda, cliente, sugestoesObrigatorias, null);
	}

	private Vector findAllSugestoesValidas(String flTipoSugestaoVenda, Cliente cliente, boolean sugestoesObrigatorias, String cdEmpresa) throws SQLException {
		Vector sugestoesVendaFinalList = VectorUtil.concatVectors(findAllSugestoesVendaComVigencia(flTipoSugestaoVenda, cliente, cdEmpresa), findAllSugestoesVendaSemVigencia(flTipoSugestaoVenda, cliente, cdEmpresa));
		sugestoesVendaFinalList = getSugestoesVendaByRep(sugestoesVendaFinalList, cdEmpresa);
		sugestoesVendaFinalList = getSugestoesByObrigatoriedade(sugestoesVendaFinalList, sugestoesObrigatorias);
		sugestoesVendaFinalList = getSugestoesByRamoAtividade(sugestoesVendaFinalList, cliente.cdRamoAtividade, false);
		return sugestoesVendaFinalList;
	}

	public Vector findAllSugestoesVigentesNaoObrigatoriaSemQtdFechamentoPedido(final Cliente cliente, String cdEmpresa) throws SQLException {
		Vector sugestoesVendaFinalList = findAllSugestoesVendaComVigencia(SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, cliente, cdEmpresa, true);
		sugestoesVendaFinalList = getSugestoesVendaByRep(sugestoesVendaFinalList, cdEmpresa);
		sugestoesVendaFinalList = getSugestoesByObrigatoriedade(sugestoesVendaFinalList, false);
		sugestoesVendaFinalList = getSugestoesByflFechamento(sugestoesVendaFinalList, true);
		sugestoesVendaFinalList = getSugestoesByRamoAtividade(sugestoesVendaFinalList, cliente.cdRamoAtividade, true);
		return sugestoesVendaFinalList;
	}

	private Vector findAllSugestoesVendaComVigencia(String flTipoSugestaoVenda, Cliente cliente, String cdEmpresa) throws SQLException {
		return findAllSugestoesVendaComVigencia(flTipoSugestaoVenda, cliente, cdEmpresa, false);
	}
	
	private Vector findAllSugestoesVendaComVigencia(String flTipoSugestaoVenda, final Cliente cliente, String cdEmpresa, boolean filterByCdRamoAtividade) throws SQLException {
		SugestaoVenda sugestaoVendaFilter = new SugestaoVenda();
		sugestaoVendaFilter.cdEmpresa = ValueUtil.isNotEmpty(cdEmpresa) ? cdEmpresa : SessionLavenderePda.cdEmpresa;
		sugestaoVendaFilter.cdRamoAtividade = getCdRamoAtividadeFilter(cliente.cdRamoAtividade, flTipoSugestaoVenda, filterByCdRamoAtividade);
		sugestaoVendaFilter.flTipoSugestaoVenda = flTipoSugestaoVenda;
		sugestaoVendaFilter.dtInicial = DateUtil.getCurrentDate();
		sugestaoVendaFilter.dtFinal = DateUtil.getCurrentDate();
		sugestaoVendaFilter.cdCanal = cliente.cdCanal;
		sugestaoVendaFilter.cdSegmento = cliente.cdSegmento;
		sugestaoVendaFilter.flPossuiVigencia = ValueUtil.VALOR_SIM;
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sugestaoVendaFilter.cdClassificacao = cliente.cdClassificacao;
		}
		return findAllByExample(sugestaoVendaFilter);
	}

	private Vector findAllSugestoesVendaSemVigencia(String flTipoSugestaoVenda, Cliente cliente, String cdEmpresa) throws SQLException {
		SugestaoVenda sugestaoVendaFilter = new SugestaoVenda();
		sugestaoVendaFilter.cdEmpresa = ValueUtil.isNotEmpty(cdEmpresa) ? cdEmpresa : SessionLavenderePda.cdEmpresa;
		sugestaoVendaFilter.cdRamoAtividade = getCdRamoAtividadeFilter(cliente.cdRamoAtividade, flTipoSugestaoVenda, false);
		sugestaoVendaFilter.cdCanal = cliente.cdCanal;
		sugestaoVendaFilter.cdSegmento = cliente.cdSegmento;
		sugestaoVendaFilter.flTipoSugestaoVenda = flTipoSugestaoVenda;
		sugestaoVendaFilter.flPossuiVigencia = ValueUtil.VALOR_NAO;
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sugestaoVendaFilter.cdClassificacao = cliente.cdClassificacao;
		}
		return findAllByExample(sugestaoVendaFilter);
	}
	
	private String getCdRamoAtividadeFilter(String cdRamoAtividade, String flTipoSugestaoVenda, boolean filterByCdRamoAtividade) {
		if (filterByCdRamoAtividade || ((SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE.equals(flTipoSugestaoVenda) && LavenderePdaConfig.isUsaRamoAtividadeSugestaoComQtdMinima()) ||
				(SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE.equals(flTipoSugestaoVenda) && LavenderePdaConfig.isUsaRamoAtividadeSugestaoSemQtdMinima()))) {
			return cdRamoAtividade;
		}
		return null;
	}

	private Vector getSugestoesVendaByRep(Vector sugestoesList, String cdEmpresa) throws SQLException {
		Vector sugestoesRepLogadoList = SugestaoVendaRepService.getInstance().findAllSugestoesVendaRepLogado(cdEmpresa);
		Vector listFinal = new Vector(0);
		if (ValueUtil.isNotEmpty(sugestoesList)) {
			for (int i = 0; i < sugestoesList.size(); i++) {
				SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestoesList.items[i];
				SugestaoVendaRep sugestaoVendaRep = new SugestaoVendaRep();
				sugestaoVendaRep.cdEmpresa = sugestaoVenda.cdEmpresa;
				sugestaoVendaRep.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
				sugestaoVendaRep.cdSugestaoVenda = sugestaoVenda.cdSugestaoVenda;
				if (sugestoesRepLogadoList.indexOf(sugestaoVendaRep) != -1) {
					listFinal.addElement(sugestaoVenda);
				} else {
					if (SugestaoVendaRepService.getInstance().countSugestoesVendaRepBySugestaoVenda(cdEmpresa, sugestaoVendaRep.cdSugestaoVenda) == 0) {
						listFinal.addElement(sugestaoVenda);
					}
				}
			}
		}
		return listFinal;
	}
	
	private Vector getSugestoesByRamoAtividade(Vector sugestoesList, String cdRamoAtividade, boolean ignoraQuantidade) {
		Vector sugestoesVendaFinal = new Vector(0);
		for (int i = 0; i < sugestoesList.size(); i++) {
			SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestoesList.items[i];
			if (ignoraQuantidade || ((sugestaoVenda.isSugestaoVendaComQuantidade() && LavenderePdaConfig.isUsaRamoAtividadeSugestaoComQtdMinima()) ||
					(sugestaoVenda.isSugestaoVendaSemQuantidade() && LavenderePdaConfig.isUsaRamoAtividadeSugestaoSemQtdMinima()))) {
				if (ValueUtil.isEmpty(cdRamoAtividade) || !cdRamoAtividade.equals(sugestaoVenda.cdRamoAtividade)) {
					continue;
				}
			}
			sugestoesVendaFinal.addElement(sugestaoVenda);
		}
		return sugestoesVendaFinal;
	}

	private Vector getSugestoesByObrigatoriedade(Vector sugestoesList, boolean isObrigatoria) {
		Vector sugestoesVendaFinal = new Vector(0);
		for (int i = 0; i < sugestoesList.size(); i++) {
			SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestoesList.items[i];
			if (isObrigatoria && sugestaoVenda.isObrigaVenda()) {
				sugestoesVendaFinal.addElement(sugestaoVenda);
			} else if (!isObrigatoria && !sugestaoVenda.isObrigaVenda()) {
				sugestoesVendaFinal.addElement(sugestaoVenda);
			}
		}
		return sugestoesVendaFinal;
	}


	private Vector getSugestoesByflFechamento(Vector sugestoesList, boolean isFechamentoPedido) {
		Vector sugestoesVendaFinal = new Vector(0);
		for (int i = 0; i < sugestoesList.size(); i++) {
			SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestoesList.items[i];
			if (isFechamentoPedido && sugestaoVenda.isFlFechamento()) {
				sugestoesVendaFinal.addElement(sugestaoVenda);
			} else if (!isFechamentoPedido && !sugestaoVenda.isFlFechamento()) {
				sugestoesVendaFinal.addElement(sugestaoVenda);
			}
		}
		return sugestoesVendaFinal;
	}

	public boolean isSugestaoVendaPendente(SugestaoVenda sugestaoVenda, Pedido pedidoAtual) throws SQLException {
		if (sugestaoVenda.isObrigaVenda()) {
			return isSugestaoVendaPendenteNoHistoricoPedidos(sugestaoVenda, pedidoAtual, false) && !isSugestaoVendaLiberadoSenha(sugestaoVenda, pedidoAtual.getCliente());
		} else {
			return isSugestaoVendaPendenteNoPedido(sugestaoVenda, pedidoAtual);
		}
	}

	public boolean isSugestaoVendaPendenteNoHistoricoPedidos(SugestaoVenda sugestaoVenda, Pedido pedidoAtual, boolean onDeletePedido) throws SQLException {
		if (pedidoAtual.isPedidoBonificacao()) {
			return false;
		}
		Vector pedidoHistoricoList = PedidoService.getInstance().findAllPedidosByFrequenciaSugestaoVenda(pedidoAtual.cdRepresentante, pedidoAtual.cdCliente, sugestaoVenda, onDeletePedido);
		if (ValueUtil.isNotEmpty(pedidoHistoricoList)) {
			for (int i = 0; i < pedidoHistoricoList.size(); i++) {
				Pedido pedido = (Pedido) pedidoHistoricoList.items[i];
				PedidoService.getInstance().findItemPedidoList(pedido);
				if (isVigenciaValidaParaPedido(sugestaoVenda, pedido)) {
					if (!isSugestaoVendaPendenteNoPedido(sugestaoVenda, pedido)) {
						return false;
					}
				}
			}
		}
		if (onDeletePedido) {
			return pedidoHistoricoList.size() > 0;
		}
		return isSugestaoVendaPendenteNoPedido(sugestaoVenda, pedidoAtual);
	}

	private boolean isSugestaoVendaPendenteNoPedido(SugestaoVenda sugestaoVenda, Pedido pedido) throws SQLException {
		if (pedido.isPedidoBonificacao()) {
			return false;
		}
		if (sugestaoVenda.isSugestaoVendaSemQuantidade()) {
			return SugestaoVendaProdService.getInstance().isHasSugestaoVendaSemQtdPendenteNoPedido(sugestaoVenda, pedido);
		} else if (sugestaoVenda.isSugestaoVendaComQuantidade()) {
			return SugestaoVendaGrupoService.getInstance().isHasSugestaoVendaComQtdePendenteNoPedido(sugestaoVenda, pedido);
		}
		return false;
	}
	
	private boolean isVigenciaValidaParaPedido(SugestaoVenda sugestaoVenda, Pedido pedido) {
		return ValueUtil.isEmpty(sugestaoVenda.dtInicial) || sugestaoVenda.dtInicial.isBefore(pedido.dtEmissao) || sugestaoVenda.dtInicial.equals(pedido.dtEmissao);
	}

	public boolean isValidaSugestaoVendaObrigatoriaMultiplasEmpresas(Pedido pedido, String flTipoSugestaoVenda, boolean isFechamentoPedido) throws SQLException {
		if (LavenderePdaConfig.validaSugestaoVendaMultiplasEmpresas == 1 && !pedido.isPedidoBonificacao()) {
			return true;
		} else if (LavenderePdaConfig.validaSugestaoVendaMultiplasEmpresas == 2 && !pedido.isPedidoBonificacao()) {
			Vector sugestoesVendaObrigatorias = SugestaoVendaService.getInstance().findAllSugestoesValidas(flTipoSugestaoVenda, pedido.getCliente(), true, isFechamentoPedido);
			return ValueUtil.isNotEmpty(sugestoesVendaObrigatorias);
		}
		return false;
	}

	public boolean isHasSugestoesObrigatoriasPendentesOutrasEmpresas(Pedido pedido, String flTipoSugestaoVenda, boolean isFechamentoPedido) throws SQLException {
		if (!pedido.isPedidoBonificacao()) {
			Vector sugestaoVendaList = findAllSugestoesVendaObrigatoriasPendentesOutrasEmpresas(pedido, flTipoSugestaoVenda, isFechamentoPedido);
			if (ValueUtil.isNotEmpty(sugestaoVendaList)) {
				for (int i = 0; i < sugestaoVendaList.size(); i++) {
					SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestaoVendaList.items[i];
					if (isSugestaoVendaPendente(sugestaoVenda, pedido)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public Vector findAllSugestoesVendaObrigatoriasPendentesOutrasEmpresas(Pedido pedido, String flTipoSugestaoVenda, boolean isFechamentoPedido) throws SQLException {
		Vector sugestoesOutrasEmpresasList = new Vector(0);
		Vector empresaList = EmpresaService.getInstance().findAllByRepresentante(pedido.cdRepresentante);
		if (ValueUtil.isNotEmpty(empresaList)) {
			for (int i = 0; i < empresaList.size(); i++) {
				String cdEmpresa = ((Empresa) empresaList.items[i]).cdEmpresa;
				if (!ValueUtil.valueEquals(cdEmpresa, pedido.cdEmpresa)) {
					Cliente cliente = ClienteService.getInstance().getCliente(cdEmpresa, pedido.cdRepresentante, pedido.cdCliente);
					if (SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE.equals(flTipoSugestaoVenda) || (SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE.equals(flTipoSugestaoVenda) && ValueUtil.isNotEmpty(cliente.cdRamoAtividade))) {
						sugestoesOutrasEmpresasList = VectorUtil.concatVectors(sugestoesOutrasEmpresasList, findAllSugestoesValidas(flTipoSugestaoVenda, cliente, true, cdEmpresa, isFechamentoPedido));
					}
				}
			}
		}
		return sugestoesOutrasEmpresasList;
	}


	public boolean isSugestaoVendaLiberadoSenha(SugestaoVenda sugestaoVenda, Cliente cliente) throws SQLException {
		if (sugestaoVenda.isTipoFrequenciaDiario()) {
			return isPossuiConfigInternoSugestaoVendaByFrequencia(cliente, sugestaoVenda.flTipoSugestaoVenda, SugestaoVenda.FLTIPOFREQUENCIA_DIARIO);
		}
		if (sugestaoVenda.isTipoFrequenciaSemanal()) {
			return isPossuiConfigInternoSugestaoVendaByFrequencia(cliente, sugestaoVenda.flTipoSugestaoVenda, SugestaoVenda.FLTIPOFREQUENCIA_SEMANAL);
		}
		if (sugestaoVenda.isTipoFrequenciaMensal()) {
			return isPossuiConfigInternoSugestaoVendaByFrequencia(cliente, sugestaoVenda.flTipoSugestaoVenda, SugestaoVenda.FLTIPOFREQUENCIA_MENSAL);
		}
		return false;
	}

	private boolean isPossuiConfigInternoSugestaoVendaByFrequencia(Cliente cliente, String flTipoSugestaoVenda, String flTipoFrequencia) throws SQLException {
		ConfigInterno configInterno = ConfigInternoService.getInstance().getNovoConfigInternoBySugestaoVenda(cliente, flTipoSugestaoVenda, flTipoFrequencia);
		ConfigInterno configInterno2 = (ConfigInterno) ConfigInternoService.getInstance().findByRowKey(configInterno.getRowKey());
		if (configInterno2 != null) {
			return ValueUtil.valueEquals(configInterno.vlConfigInterno, configInterno2.vlConfigInterno);
		}
		return false;
	}
	
	public Vector findSugestaoVendaVigenteSemFechamento(String cdEmpresa, Cliente cliente) throws SQLException {
		Vector sugestaoVendaFinalList = new Vector();
		SugestaoVenda sugestaoVendaFilter = new SugestaoVenda();
		sugestaoVendaFilter.cdEmpresa = cdEmpresa;
		sugestaoVendaFilter.cdRamoAtividade = cliente.cdRamoAtividade;
		sugestaoVendaFilter.flFechamentoDif = ValueUtil.VALOR_SIM;
		sugestaoVendaFilter.cdCanal = cliente.cdCanal;
		sugestaoVendaFilter.cdSegmento = cliente.cdSegmento;
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			sugestaoVendaFilter.cdClassificacao = cliente.cdClassificacao;
		}
		Vector sugestaoVendaList = getSugestoesVendaByRep(findAllByExample(sugestaoVendaFilter), cdEmpresa);
		int size = sugestaoVendaList.size();
		Date currentDate = DateUtil.getCurrentDate();
		for (int i = 0; i < size; i++) {
			SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestaoVendaList.items[i];
			if (ValueUtil.VALOR_SIM.equals(sugestaoVenda.flPossuiVigencia)) {
				if (ValueUtil.isNotEmpty(sugestaoVenda.dtInicial) && ValueUtil.isNotEmpty(sugestaoVenda.dtFinal) && !currentDate.isBefore(sugestaoVenda.dtInicial) && !currentDate.isAfter(sugestaoVenda.dtFinal)) {
					sugestaoVenda.sortAtributte = SugestaoVenda.NMCOLUNA_DSSUGESTAOVENDA;
					sugestaoVendaFinalList.addElement(sugestaoVenda);
				}
			}
		}
		return sugestaoVendaFinalList;
	}
	
	public SugestaoVenda findSugestaoVendaByProdutoIndustria(ProdutoIndustria prodInd) throws SQLException {
		SugestaoVenda sug = new SugestaoVenda();
 		sug.cdSugestaoVenda = prodInd.cdSugestaoVenda;
 		sug.cdEmpresa = prodInd.cdEmpresa;
 		return (SugestaoVenda) SugestaoVendaService.getInstance().findByRowKey(sug.getRowKey());
	}
	
}