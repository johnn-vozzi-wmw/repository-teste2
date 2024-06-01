package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.ProdutoConcorrente;
import br.com.wmw.lavenderepda.business.validation.PesquisaMercadoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaMercadoPdbxDao;
import totalcross.util.Vector;

public class PesquisaMercadoService extends CrudService {

    private PesquisaMercadoService() {
        //--
    }

    public static PesquisaMercadoService getInstance() {
        return new PesquisaMercadoService();
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PesquisaMercadoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
        PesquisaMercado pesquisaMercado = (PesquisaMercado) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(pesquisaMercado.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.EMPRESA_NOME_ENTIDADE);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(pesquisaMercado.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.REPRESENTANTE_NOME_ENTIDADE);
        }
        //vlUnitario
        if (!pesquisaMercado.isTipoPesquisaGondola()) {
        	validateVlUnitario(pesquisaMercado);
        }
        //cdConcorrente
        if (ValueUtil.isEmpty(pesquisaMercado.cdConcorrente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CONCORRENTE_NOME_ENTIDADE);
        }
        //cdProduto
        if (ValueUtil.isEmpty(pesquisaMercado.cdProduto) && !LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes()) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTO_NOME_ENTIDADE);
        }
		//fotos
		if (!(pesquisaMercado.isTipoPesquisaGondola() || pesquisaMercado.isTipoPesquisaValor()) && ValueUtil.isEmpty(pesquisaMercado.fotoList) && (LavenderePdaConfig.obrigaCadastroFotoProdutoNaPesquisaDeMercado()) && !SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado && pesquisaMercado.inInsertList) {
			PesquisaMercadoException e = new PesquisaMercadoException(FrameworkMessages.VALIDACAO_CAMPO_INVALIDO);
			e.isFotos = true;
			throw e;
		}
		//coordenadas
		if (!(pesquisaMercado.isTipoPesquisaGondola() || pesquisaMercado.isTipoPesquisaValor()) && (pesquisaMercado.cdLongitude != null && pesquisaMercado.cdLongitude == 0) && (pesquisaMercado.cdLatitude != null && pesquisaMercado.cdLatitude == 0) && (LavenderePdaConfig.obrigaCadastroCoordenadaNaPesquisaDeMercado() || LavenderePdaConfig.usaCadastroAutomaticoCoordenadaPesquisaDeMercado()) && !SessionLavenderePda.autorizadoPorSenhaCoordenadaPesquisaMercado && pesquisaMercado.inInsertList) {
			PesquisaMercadoException e = new PesquisaMercadoException(FrameworkMessages.VALIDACAO_CAMPO_INVALIDO);
			e.isCoordenada = true;
			throw e;
		}
		pesquisaMercado.isLiberadoParaSalvar = true;
    }

	private void validateVlUnitario(PesquisaMercado pesquisamercado) throws SQLException {
		if (pesquisamercado.vlUnitario == 0) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.PRODUTO_LABEL_VLUNITARIO);
		}
		String[] tabPrecosList = TabelaPrecoService.getInstance().findAllCdsTabPrecoByExample();
		int size = tabPrecosList.length;
		ItemTabelaPreco itemTabPreco;
		for (int i = 0; i < size; i++) {
			itemTabPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(tabPrecosList[i], pesquisamercado.cdProduto, SessionLavenderePda.getCliente().dsUfPreco);
			if (itemTabPreco == null || itemTabPreco.vlUnitario == 0) {
				continue;
			}
			if ((itemTabPreco.vlUnitario * 2) < pesquisamercado.vlUnitario) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PESQUISAMERCADO_MSG_VLUNITARIO, new String[] { StringUtil.getStringValueToInterface(pesquisamercado.vlUnitario), StringUtil.getStringValueToInterface(itemTabPreco.vlUnitario * 2)}));
			}
			break;
		}
		tabPrecosList = null;
	}

    public Vector findAllPesquisasForCliente(String cdCliente) throws SQLException {
    	PesquisaMercado pesqMercado = new PesquisaMercado();
    	pesqMercado.cdCliente = cdCliente;
    	pesqMercado.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	pesqMercado.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor() && !LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
    		pesqMercado.cdTipoPesquisa = PesquisaMercado.CDTIPOPESQUISA_VALOR;
    	} else if (!LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoValor() && LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
    		pesqMercado.cdTipoPesquisa = PesquisaMercado.CDTIPOPESQUISA_GONDOLA;
    	}
    	Vector result = findAllByExample(pesqMercado);
    	if (result == null) {
    		result = new Vector();
    	}
    	return result;
    }

	public void validaPesquisaMercadoValor(PesquisaMercado pesquisaMercado) throws SQLException {
		ProdutoConcorrente produtoConcorrente = ProdutoConcorrenteService.getInstance().getProdutoByPesqMercado(pesquisaMercado);
		if (pesquisaMercado.vlUnitario == 0) {
			if (pesquisaMercado.qtItem != 0 || ValueUtil.isNotEmpty(pesquisaMercado.dsObservacao) || (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado() && ValueUtil.isNotEmpty(pesquisaMercado.fotoList))) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PESQUISA_MERCADO_VLUNITARIO_OBRIGATORIO, new Object[] {produtoConcorrente.toString(), ConcorrenteService.getInstance().getDsConcorrente(produtoConcorrente.cdConcorrente)}));
			}
		}
		if (pesquisaMercado.vlUnitario != 0 && LavenderePdaConfig.obrigaCadastroFotoProdutoNaPesquisaDeMercado() && !SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado && ValueUtil.isEmpty(pesquisaMercado.fotoList)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PESQUISA_MERCADO_CAMPOS_OBRIGATORIOS, new Object[] {Messages.PESQUISA_MERCADO_FOTOS_OBRIGATORIAS, produtoConcorrente.toString(), ConcorrenteService.getInstance().getDsConcorrente(produtoConcorrente.cdConcorrente)}));
		}
	}
	
	public void validaPesquisaMercadoGondola(PesquisaMercado pesquisaMercado) throws SQLException {
		if (pesquisaMercado != null && (pesquisaMercado.qtItemFrente != 0 || pesquisaMercado.qtItemProfundidade != 0 || pesquisaMercado.qtItemAndar != 0 || pesquisaMercado.qtItemTotal != 0 || (LavenderePdaConfig.usaCadastroFotoProdutoNaPesquisaDeMercado() && ValueUtil.isNotEmpty(pesquisaMercado.fotoList)))) {
			ProdutoConcorrente produtoConcorrente = ProdutoConcorrenteService.getInstance().getProdutoByPesqMercado(pesquisaMercado);
			if (pesquisaMercado.qtItemFrente == 0) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PESQUISA_MERCADO_CAMPO_OBRIGATORIO, new Object[] {Messages.PESQUISA_MERCADO_QTITEMFRENTE, produtoConcorrente.toString(), ConcorrenteService.getInstance().getDsConcorrente(produtoConcorrente.cdConcorrente)}));
			}
			if (pesquisaMercado.qtItemProfundidade == 0) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PESQUISA_MERCADO_CAMPO_OBRIGATORIO, new Object[] {Messages.PESQUISA_MERCADO_QTITEMPROFUNDIDADE, produtoConcorrente.toString(), ConcorrenteService.getInstance().getDsConcorrente(produtoConcorrente.cdConcorrente)}));
			}
			if (pesquisaMercado.qtItemAndar == 0) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PESQUISA_MERCADO_CAMPO_OBRIGATORIO, new Object[] {Messages.PESQUISA_MERCADO_QTITEMANDAR, produtoConcorrente.toString(), ConcorrenteService.getInstance().getDsConcorrente(produtoConcorrente.cdConcorrente)}));
			}
			if (pesquisaMercado.qtItemTotal == 0) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PESQUISA_MERCADO_CAMPO_OBRIGATORIO, new Object[] {Messages.PESQUISA_MERCADO_QTITEMTOTAL, produtoConcorrente.toString(), ConcorrenteService.getInstance().getDsConcorrente(produtoConcorrente.cdConcorrente)}));
			}
			if (LavenderePdaConfig.obrigaCadastroFotoProdutoNaPesquisaDeMercado() && !SessionLavenderePda.autorizadoPorSenhaFotosPesquisaMercado && ValueUtil.isEmpty(pesquisaMercado.fotoList)) {
				throw new ValidationException(MessageUtil.getMessage(Messages.PESQUISA_MERCADO_CAMPOS_OBRIGATORIOS, new Object[] {Messages.PESQUISA_MERCADO_FOTOS_OBRIGATORIAS, produtoConcorrente.toString(), ConcorrenteService.getInstance().getDsConcorrente(produtoConcorrente.cdConcorrente)}));
			}
		}
	}

	public void insert(Vector pesquisaMercadoList) throws SQLException {
		int size = pesquisaMercadoList.size();
		for (int i = 0; i < size; i++) {
			PesquisaMercado pesquisaMercado = (PesquisaMercado) pesquisaMercadoList.items[i];
			insert(pesquisaMercado);
		}
	}
	
	public boolean isPedidoComPesqMercado(Pedido pedido) throws SQLException {
		PesquisaMercado filter = getPesquisaMercadoFilter(pedido);
		return countByExample(filter) > 0;
	}
	
	public void deletePesquisaMercadoByPedido(Pedido pedido) throws SQLException {
		PesquisaMercado filter = getPesquisaMercadoFilter(pedido);
		deleteAllByExample(filter);
	}

	private PesquisaMercado getPesquisaMercadoFilter(Pedido pedido) {
		PesquisaMercado filter = new PesquisaMercado();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.nuPedido = pedido.nuPedido;
		return filter;
	}
	
	public void clearPedidoFromPesquisaMercado(Pedido pedido) throws SQLException {
		PesquisaMercado filter = getPesquisaMercadoFilter(pedido);
		PesquisaMercadoPdbxDao.getInstance().clearPedidoFromPesquisaMercado(filter);
	}

	public double[] getCoordenadasPesquisaMercado() {
		return getCoordenadasPesquisaMercado(new double[2]);
	}
	
	public double[] getCoordenadasPesquisaMercado(double[] lat_lon) {
		if (VmUtil.isAndroid() || VmUtil.isIOS()) {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_COORD_COLETANDO);
			msg.popupNonBlocking();
			try {
				GpsData gpsData = GpsService.getInstance().forceReadData(LavenderePdaConfig.getTimeOutCoordenadaPesquisaDeMercado());
				if (gpsData.isGpsOff()) {
					throw new ValidationException(Messages.CAD_COORD_GPS_DESLIGADO);
				}
				if (gpsData.isSuccess()) {
					lat_lon[0] = gpsData.latitude;
					lat_lon[1] = gpsData.longitude;
				} else {
					throw new ValidationException(Messages.CAD_COORD_COLETA_ERRO);
				}
			} catch (ValidationException e) {
				throw new ValidationException(e.getMessage());
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			} finally {
				msg.unpop();
			}
		} else {
			throw new ValidationException(Messages.CAD_COORD_PLATAFORMA_NAO_SUPORTADA);
		}
		return lat_lon;
	}

}