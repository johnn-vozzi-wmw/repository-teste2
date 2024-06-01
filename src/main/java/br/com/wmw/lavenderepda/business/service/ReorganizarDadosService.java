package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.service.BaseService;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.WtoolsUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteAtua;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.ItemConsignacao;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoClienteAna;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import br.com.wmw.lavenderepda.business.domain.Recado;
import br.com.wmw.lavenderepda.business.domain.RelNovSolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeProd;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.domain.ResumoDia;
import br.com.wmw.lavenderepda.business.domain.ValorizacaoProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CargaProdutoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteAtuaDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovoClienteAnaDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovoClientePdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaMercadoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RecadoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelNovidadeProdPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RemessaEstoqueDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ValorizacaoProdDbxDao;
import totalcross.io.File;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ReorganizarDadosService extends BaseService {

	public static String DS_EXTENSAO_LOG = ".log";

	public boolean isReorganizarDadosNecessario() throws SQLException {
		String dtHrUltimoReoganizar = ConfigInternoService.getInstance()
				.getVlConfigInterno(ConfigInterno.dataHoraUltimoReorganizarDados);
		if (ValueUtil.isEmpty(dtHrUltimoReoganizar)) {
			return true;
		}
		int nuSegundosUltimoReorganizar = TimeUtil.getSecondsRealBetween(TimeUtil.getCurrentTime(),
				TimeUtil.getTime(dtHrUltimoReoganizar));
		if (nuSegundosUltimoReorganizar != 0) {
			return nuSegundosUltimoReorganizar > TimeUtil.DIA_ASLONG;
		} else {
			updateNuSegundosUltimoReorganizarDados();
			return false;
		}
	}

	/**
	 * Atualiza a configuração da última ocorrência do reorganizar dados para que
	 * possa ser usado para definir a próxima ocorrência
	 * 
	 * @throws SQLException
	 */
	public void updateNuSegundosUltimoReorganizarDados() throws SQLException {
		ConfigInternoService.getInstance().addValue(ConfigInterno.dataHoraUltimoReorganizarDados,
				StringUtil.getStringValue(TimeUtil.getTimeAsLong()));
	}

	/**
	 * Apaga os pedidos com status diferente de aberto e fechado, e que tenham data
	 * de emissão anterior a data atual menos os dias configurados no parâmetro
	 * 'nuDiasPermanenciaPedido'
	 * 
	 * @throws SQLException
	 */
	public void deletePedidosAntigos() throws SQLException {
		if (LavenderePdaConfig.nuDiasPermanenciaPedido > 0) {
			Vector pedidoList = PedidoService.getInstance().findAllPedidosPDAAntigos();
			int size = pedidoList.size();
			Pedido pedido;
			for (int i = 0; i < size; i++) {
				pedido = (Pedido) pedidoList.items[i];
				PedidoService.getInstance().findItemPedidoList(pedido);
				PedidoService.getInstance().delete(pedido);
			}
			pedidoList = null;
		}
	}
	
	/**
	 * Apaga os pedidos com status perdido, e que tenham
	 * data de emissão anterior a data atual menos os dias configurados na
	 * propriedade 'nuDiasPermanenciaPedidoPerdido' do parametro 1850
	 * @throws SQLException 
	 */
	public void deletePedidosPerdidosAntigos() throws SQLException {
		if (LavenderePdaConfig.usaPedidoPerdido && LavenderePdaConfig.nuDiasPermanenciaPedidoPerdido > 0) {
			Vector pedidoList = PedidoService.getInstance().findAllPedidosPDAPerdidosAntigos();
			int size = pedidoList.size();
			Pedido pedido;
	        for (int i = 0; i < size; i++) {
				pedido = (Pedido)pedidoList.items[i];
				PedidoService.getInstance().findItemPedidoList(pedido);
				PedidoService.getInstance().delete(pedido);
			}
	        pedidoList = null;
		}
	}

	public void deleteConsignacaoAndItensConsigAntigos() throws SQLException {
		if (LavenderePdaConfig.usaModuloConsignacao) {
			Vector consignacaoList = ConsignacaoService.getInstance().findAllConsigSentAndPaid();
			int size = consignacaoList.size();
			if (size > 0) {
				int maxCarimboConsignacaoOld = ConsignacaoService.getInstance().getMaxCarimbo();
				int maxCarimboitemConsignacaoOld = ItemConsignacaoService.getInstance().getMaxCarimbo();
				for (int i = 0; i < size; i++) {
					Consignacao consignacao = (Consignacao) consignacaoList.items[i];
					ItemConsignacao itemConsignacao = new ItemConsignacao();
					itemConsignacao.cdEmpresa = consignacao.cdEmpresa;
					itemConsignacao.cdRepresentante = consignacao.cdRepresentante;
					itemConsignacao.cdCliente = consignacao.cdCliente;
					itemConsignacao.cdConsignacao = consignacao.cdConsignacao;
					ItemConsignacaoService.getInstance().deleteAllByExample(itemConsignacao);
					ConsignacaoService.getInstance().delete(consignacao);
				}
				ConsignacaoService.getInstance().updateCarimbo(maxCarimboConsignacaoOld,
						ConsignacaoService.getInstance().getMaxCarimbo());
				ItemConsignacaoService.getInstance().updateCarimbo(maxCarimboitemConsignacaoOld,
						ItemConsignacaoService.getInstance().getMaxCarimbo());
			}
		}
	}

	/**
	 * Apaga os clienteAtua antigos do Pda
	 * 
	 * @throws SQLException
	 */
	public void deleteClienteAtuaAntigos() throws SQLException {
		if (LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente > 0
				|| LavenderePdaConfig.nuDiasAtualizaCadastroCliente > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			int dias = LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente > 0
					? -LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente
					: -LavenderePdaConfig.nuDiasAtualizaCadastroCliente;
			dataLimite.advance(dias);
			ClienteAtua clienteAtua = new ClienteAtua();
			clienteAtua.dtAtualizacaoMaxima = dataLimite;
			ClienteAtuaDbxDao.getInstance().deleteAllByExample(clienteAtua);
		}
	}

	/**
	 * Apaga os recados antigos do Pda
	 */
	public void deleteRecadosAntigos() {
		if (LavenderePdaConfig.nuDiasPermanenciaRecado > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-LavenderePdaConfig.nuDiasPermanenciaRecado);
			Recado recado = new Recado();
			recado.dtCadastro = dataLimite;
			RecadoPdbxDao.getInstance().deleteAllByDtEmissao(recado);
		}
	}

	/**
	 * Apaga os Resumos do Dia antigos do Pda
	 * 
	 * @throws SQLException
	 */
	public void deleteResumosDiaAntigos() throws SQLException {
		if (LavenderePdaConfig.nuDiasPermanenciaResumoDia > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-LavenderePdaConfig.nuDiasPermanenciaResumoDia);
			ResumoDia resumoDia = new ResumoDia();
			resumoDia.dtResumoMenorIgualFilter = dataLimite;
			ResumoDiaService.getInstance().deleteAllByExample(resumoDia);
		}
	}

	/**
	 * Apaga as novidades de produtos antigos do Pda
	 */
	public void deleteNovidadesAntigas() {
		if (LavenderePdaConfig.nuDiasPermanenciaNovidadesProduto > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-LavenderePdaConfig.nuDiasPermanenciaNovidadesProduto);
			RelNovidadeProd relNovidadeProduto = new RelNovidadeProd();
			relNovidadeProduto.dtEmissaoRelatorio = dataLimite;
			RelNovidadeProdPdbxDao.getInstance().deleteAllByDtEmissao(relNovidadeProduto);
		}
	}

	/**
	 * Apaga as pesquisas de mercado antigas do Pda
	 */
	public void deletePesquisasMercadoAntigas() {
		if (LavenderePdaConfig.qtDiasPermanenciaPesquisaDeMercado > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-LavenderePdaConfig.qtDiasPermanenciaPesquisaDeMercado);
			PesquisaMercado pesquisaMercado = new PesquisaMercado();
			pesquisaMercado.dtEmissao = dataLimite;
			PesquisaMercadoPdbxDao.getInstance().deleteAllByDtEmissao(pesquisaMercado);
		}
	}

	/**
	 * Apaga as visitas antigas do pda.
	 */
	public void deletaVisitasAntigas() {
		if ((LavenderePdaConfig.isUsaAgendaDeVisitas() || LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita)
				&& !SessionLavenderePda.isUsuarioSupervisor()) {
			VisitaService.getInstance().deletaVisitasAntigas();
		}
	}

	/**
	 * Apaga as Cargas de Pedido antigas do pda.
	 * 
	 * @throws SQLException
	 */
	public void deleteCargasPedidoAntigos() throws SQLException {
		if (LavenderePdaConfig.nuDiasPermanenciaCargaPedido > 0) {
			CargaPedidoService.getInstance().deleteCargasPedidoAntigas();
		}
	}

	public void deleteLogWGps() {
		if (LavenderePdaConfig.nuDiasPermanenciaLogsWgps > 0) {
			try (File file = WtoolsUtil.getLogDir()) {
				String[] files = file.listFiles();
				int size = files.length;
				for (int i = 0; i < size; i++) {
					String fileName = files[i];
					if (DS_EXTENSAO_LOG.equals(FileUtil.getExtensaoArquivo(fileName))) {
						String fileDate = fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf("."));
						Date data = DateUtil.getDateValue(ValueUtil.getIntegerValue(fileDate.substring(6, 8)),
								ValueUtil.getIntegerValue(fileDate.substring(4, 6)),
								ValueUtil.getIntegerValue(fileDate.substring(0, 4)));
						DateUtil.addDay(data, LavenderePdaConfig.nuDiasPermanenciaLogsWgps);
						if (data.isBefore(DateUtil.getCurrentDate())) {
							FileUtil.deleteFile(file.getPath() + "/" + fileName);
						}
					}
				}
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			}
		}
	}

	/**
	 * Apaga os pedidos de consignação antigos do pda.
	 * 
	 * @throws SQLException
	 */
	public void deletePedidoConsignacaoLimitePrazo() throws SQLException {
		if (LavenderePdaConfig.isLigadoNuDiasPermanenciaPedidoConsignacao()) {
			PedidoConsignacaoService.getInstance().executaLimpezaPedidoConsignacao();
		}
	}

	public void deleteNovoClienteAnaAntigo() throws SQLException {
		int nuDiasPermanencia = LavenderePdaConfig.getNuDiasPermanenciaAnaliseNovoCliente();
		if (nuDiasPermanencia > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-nuDiasPermanencia);
			NovoClienteAna novoClienteAna = new NovoClienteAna();
			novoClienteAna.dtCadastro = dataLimite;
			NovoClienteAnaDbxDao.getInstance().deleteAllByDtLimite(novoClienteAna);
		}
	}

	public void deleteRemessasEstoque() throws SQLException {
		int nuDiasPermanencia = LavenderePdaConfig.nuDiasPermanenciaRemessaEstoque;
		Date dataLimite = DateUtil.getCurrentDate();
		dataLimite.advance(-nuDiasPermanencia);
		RemessaEstoque remessaEstoque = new RemessaEstoque();
		remessaEstoque.dtRemessa = dataLimite;
		RemessaEstoqueDbxDao.getInstance().deleteAllByExample(remessaEstoque);
	}

	/**
	 * Apaga os registros de Valorização do Pda que sejam antigos
	 * 
	 * @throws SQLException
	 */
	public void deleteValorizacaoProdutoAntigos() throws SQLException {
		int dias = LavenderePdaConfig.nuDiasPermanenciaRegistroValorizacaoProd > 0
				? LavenderePdaConfig.nuDiasPermanenciaRegistroValorizacaoProd
				: 180;
		Date dataLimite = DateUtil.getCurrentDate();
		dataLimite.advance(-dias);
		ValorizacaoProd valorizacao = new ValorizacaoProd();
		valorizacao.dtValorizacao = dataLimite;
		ValorizacaoProdDbxDao.getInstance().deleteAllByDtEmissao(valorizacao);
	}

	public void deleteRegistroNovoClienteAntigos() throws SQLException {
		if (LavenderePdaConfig.isUsaCadastroNovoCliente()
				&& LavenderePdaConfig.nuDiasPermanenciaRegistroNovoCliente > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-LavenderePdaConfig.nuDiasPermanenciaRegistroNovoCliente);
			NovoCliente novoClienteFilter = new NovoCliente();
			novoClienteFilter.dtCadastro = dataLimite;
			Vector novoClienteList = NovoClientePdbxDao.getInstance().findAllByExample(novoClienteFilter);
			for (int i = 0; i < novoClienteList.size(); i++) {
				NovoCliente novoCliente = (NovoCliente) novoClienteList.items[i];
				if (!PedidoService.getInstance().existePedidoParaNovoCliente(novoCliente)
						&& NovoClienteAnaService.getInstance().countByExample(novoCliente.toNovoClienteAna()) == 0) {
					FotoNovoClienteService.getInstance().excluiFotosNovoCliente(novoCliente);
					NovoCliEnderecoService.getInstance().deleteRegistrosNovoCliEndereco(novoCliente);
					NovoClienteService.getInstance().delete(novoCliente);
				}
			}
		}
	}

	/**
	 * Apaga os registros de DepositoBancario e FechamentoDiario antigos, mantendo o
	 * ultimo fechamento
	 * 
	 * @throws SQLException
	 */

	public void deleteDepositoBancarioEFechamentoDiarioAntigos() throws SQLException {
		if (LavenderePdaConfig.usaConfigFechamentoDiarioVendas()) {
			int dias = LavenderePdaConfig.getNuDiasPermanenciaFechamentoDiario();
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-dias);
			CrudDbxDao.getCurrentDriver().startTransaction();
			try {
				dataLimite = FechamentoDiarioService.getInstance().getDataFechamentoDiarioNaoExcluido(dataLimite);
				DepositoBancarioService.getInstance().deleteDepositoBancarioMantendoUltimoFechamentoDiario(dataLimite);
			} catch (Throwable ex) {
				CrudDbxDao.getCurrentDriver().rollback();
				ExceptionUtil.handle(ex);
			} finally {
				CrudDbxDao.getCurrentDriver().finishTransaction();
			}
		}
	}

	public void deleteEstoqueRepAntigos() throws SQLException {
		if (LavenderePdaConfig.usaDevolucaoTotalEstoqueRemessaRepresentante) {
			int dias = LavenderePdaConfig.getNuDiasPermanenciaDadosEstoqueRep();
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-dias);
			EstoqueRepService.getInstance().deleteEstoqueRepAntigos(dataLimite);
		}
	}

	public void deleteCargaProduto() {
		String vlParam = LavenderePdaConfig.nuDiasPermanenciaRegistroCargaProduto;
		if (vlParam != null && !ValueUtil.VALOR_NAO.equals(vlParam)) {
			int nuDias = ValueUtil.getIntegerValue(vlParam);
			nuDias = nuDias > 0 ? nuDias : 180;
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-nuDias);
			CargaProdutoDbxDao.getInstance().deleteAllByDtCadastro(dataLimite);
		}
	}

	public void deleteVerbaSaldoForaVigencia() throws SQLException {
		if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			VerbaSaldoVigenciaService.getInstance().deleteVerbaSaldoForaVigencia();
		}
	}

	public void deleteLogAppEnviadoServidor() {
		LogAppService.getInstance().deleteLogEnviadoServidor();
	}

	public void deleteRegistrosProspect() throws SQLException {
		if (LavenderePdaConfig.nuDiasPermanenciaRegistroProspect > 0) {
			Date limite = DateUtil.getCurrentDate();
			limite.advance(-LavenderePdaConfig.nuDiasPermanenciaRegistroProspect);
			ProspectService.getInstance().deleteRegistrosProspectsAntigos(limite);
		}
	}

	public void deletePesquisaMercadoProdutoConcorrentesAntigas() throws SQLException {
		if (!LavenderePdaConfig.usaConfigPesquisaMercadoProdutoConcorrente()) {
			return;
		}
		Vector pesquisaMercadoConfigExpiradaList = PesquisaMercadoConfigService.getInstance().findAllPesquisaMercadoExpirada();
		PesquisaMercadoRegService.getInstance().deletePesquisaMercadoSemConfig();
		if (ValueUtil.isNotEmpty(pesquisaMercadoConfigExpiradaList)) {
			int size = pesquisaMercadoConfigExpiradaList.size();
			for (int i = 0; i < size; i++) {
				PesquisaMercadoConfig pesquisaMercadoConfig = (PesquisaMercadoConfig) pesquisaMercadoConfigExpiradaList.items[i];
				PesquisaMercadoConcorrenteService.getInstance().deleteRegistrosAntigos(pesquisaMercadoConfig);
				PesquisaMercadoProdutoService.getInstance().deleteRegistrosAntigos(pesquisaMercadoConfig);
				deletePesquisaMercadoRegExpirada(PesquisaMercadoRegService.getInstance().findAllPesquisaMercadoRegExpirada(pesquisaMercadoConfig));
				finalizaPesquisaMercadoPendente(pesquisaMercadoConfig);
				RelNovidadePesquisaMercadoService.getInstance().deleteRelNovidadePesquisaMercadoExpirada(pesquisaMercadoConfig);
				PesquisaMercadoConfigService.getInstance().delete(pesquisaMercadoConfig);
			}
		}
	}

	private void finalizaPesquisaMercadoPendente(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		PesquisaMercadoRegService.getInstance().updateFlFinalizada(pesquisaMercadoConfig, ValueUtil.VALOR_SIM);
		if (LavenderePdaConfig.usaInclusaoFotoPesquisaMercado() || LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado()) {
			FotoPesqMerProdConcService.getInstance().updateFlTipoAlteracao(pesquisaMercadoConfig);
		}
	}

	private void deletePesquisaMercadoRegExpirada(Vector pesquisaMercadoRegExpiradaList) throws SQLException {
		if (ValueUtil.isNotEmpty(pesquisaMercadoRegExpiradaList)) {
			int size = pesquisaMercadoRegExpiradaList.size();
			for (int i = 0; i < size; i++) {
				PesquisaMercadoReg pesquisaMercadoReg = (PesquisaMercadoReg) pesquisaMercadoRegExpiradaList.items[i];
				if (LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado() || LavenderePdaConfig.usaInclusaoFotoPesquisaMercado()) {
					FotoPesqMerProdConcService.getInstance().deleteFotoByPesquisaReg(pesquisaMercadoReg);
				}
				PesquisaMercadoRegService.getInstance().delete(pesquisaMercadoReg);
			}
		}
	}
	
	public void limparNuOrdemManualAgendaVisita() throws SQLException {
		if (LavenderePdaConfig.usaMelhorRotaSistema) {
			AgendaVisitaService.getInstance().limpaNuOrdemManualAgenda();
		}
	}

	/**
	 * Apaga as novidades de Soliticação de Autorização antigas do Pda
	 */
	public void deleteNovidadesSolAutorizacaoAntigas() {
		if (LavenderePdaConfig.geraNovidadeAutorizacao && LavenderePdaConfig.nuDiasPermanenciaNovidadesProduto > 0) {
			Date dataLimite = DateUtil.getCurrentDate();
			dataLimite.advance(-LavenderePdaConfig.nuDiasPermanenciaNovidadesProduto);
			RelNovSolAutorizacao relNovSolAutorizacao = new RelNovSolAutorizacao();
			relNovSolAutorizacao.dtEmissaoRelatorio = dataLimite;
			RelNovSolAutorizacaoService.getInstance().deleteAllByDtEmissao(relNovSolAutorizacao);
		}
	}

	public void deletePdfPedido() {
		VmUtil.debug("Início exclusão pdf pedidos");
		try (File file = FileUtil.openFile(new Pedido().getDsFilePathPdfPedido(), File.DONT_OPEN)) {
			String[] files = file.listFiles();
			int size = files.length;
			for (int i = 0; i < size; i++) {
				String fileName = files[i];
				FileUtil.deleteFile(file.getPath() + "/" + fileName);
			}
		} catch (Throwable e) {
			VmUtil.debug("Erro exclusão pdf pedidos " + e.getMessage());
		} finally {
			VmUtil.debug("Fim exclusão pdf pedidos.");
		}
	}

	public void executaLimpezaAgendasDataFinalUltrapassada() {
		AgendaVisitaService.getInstance().excluiAgendaVisitaDataFinalUltrapassada();
		AgendaCadastroService.getInstance().excluiAgendaCadastroDataFinalUltrapassada();
	}

}
