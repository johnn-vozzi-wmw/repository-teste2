package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpSync;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DivulgaInfoPdbxDao;
import totalcross.io.ByteArrayStream;
import totalcross.io.IOException;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class DivulgaInfoService extends CrudPersonLavendereService {

    private static DivulgaInfoService instance;

    public static DivulgaInfoService getInstance() { return instance == null ? instance = new DivulgaInfoService() : instance; }
    protected CrudDao getCrudDao() { return DivulgaInfoPdbxDao.getInstance(); }
    public void validate(BaseDomain domain) throws java.sql.SQLException {}
    
	public boolean hasDivulgaInfoByCliente(final Cliente cliente) {
		try {
			if (cliente == null) return false;
			return countByExample(getDivulgaInfoFilter(cliente)) > 0;
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}

	private DivulgaInfo getDivulgaInfoFilter(final Cliente cliente) {
		DivulgaInfo divulgaInfoFilter = new DivulgaInfo(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdCliente);
		divulgaInfoFilter.cdClienteInFilter = new String[] {divulgaInfoFilter.cdCliente, "0"};
		divulgaInfoFilter.dtInicial = divulgaInfoFilter.dtFinal = DateUtil.getCurrentDate();
		return divulgaInfoFilter;
	}

	public boolean apresentaDivulgaInfoCliente(Cliente cliente) throws SQLException {
		if (!LavenderePdaConfig.usaDivulgaInformacao || cliente == null) return false;
		boolean hasDivulgaInfo = hasDivulgaInfoByCliente(cliente);
		if (!hasDivulgaInfo) return false;
		if (LavenderePdaConfig.nuApresentaDivulgacao == 0) return true;
		int qtVisualizacoes = ValueUtil.getIntegerValue(ClienteService.getInstance().findColumnByRowKey(cliente.getRowKey(), Cliente.NUDIVULGAINFO));
		return qtVisualizacoes < LavenderePdaConfig.nuApresentaDivulgacao;
	}

	public Vector findAllByCliente(Cliente cliente) {
		try {
			if (cliente == null) return new Vector();
			Vector listDivulgaInfo = findAllByExample(getDivulgaInfoFilter(cliente));
			carregaImagensTipoLink(listDivulgaInfo);
			return listDivulgaInfo;
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return new Vector();
		}
	}

	private void carregaImagensTipoLink(Vector listDivulgaInfo) {
		if (LavenderePdaConfig.baixaImagemLinkAoExibir || ValueUtil.isEmpty(listDivulgaInfo)) return;
		int size = listDivulgaInfo.size();
		DivulgaInfo divulgaInfo;
		for (int i = 0; i < size; i++) {
			divulgaInfo = (DivulgaInfo) listDivulgaInfo.items[i];
			if (!divulgaInfo.isTipoLink() || ValueUtil.isEmpty(divulgaInfo.nmUrlImagemLink) || divulgaInfo.imageUrlLink != null) continue;
			divulgaInfo.imageUrlLink = downloadImageFromLink(divulgaInfo.nmUrlImagemLink, false);
		}
	}

	public Image downloadImageFromLink(final String imageUrl, boolean showLoader) {
		LoadingBoxWindow lb = null;
		ByteArrayStream bas = null;
		HttpSync httpSync = null;
		try {
			if (showLoader) {
				lb = new LoadingBoxWindow(Messages.MSG_CARREGANDO_FOTO_DIVULGA_INFO);
				lb.popupNonBlocking();
			}
			ParamsSync ps = new ParamsSync();
			ps.nuMaxTentativas = 1;
			ps.nuMaxTentativasFimEnvio = 0;
			ps.openTimeout = LavenderePdaConfig.nuTimeOutImagemUrl;
			ps.readTimeout = LavenderePdaConfig.nuTimeOutImagemUrl;
			ps.readTimeoutFimEnvio = 0;
			ps.baseUrl = imageUrl;
			httpSync = new HttpSync();
			httpSync.paramsSync = ps;
			httpSync.open();
			bas = httpSync.executePostBy(ValueUtil.VALOR_NI, null, HttpSync.CONTENTTYPE_JSON, false);
			return new Image(bas);
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
			return null;
		} finally {
			if (bas != null) bas.close();
			if (httpSync != null) {
				try {
				httpSync.close();
				} catch (IOException ex) {
					ExceptionUtil.handle(ex);
				}
			}
			if (lb != null) lb.unpop();
		}
	}

	public Vector findAllNaoAlteradosToDownloadFotos() throws SQLException {
    	return DivulgaInfoPdbxDao.getInstance().findAllNaoAlteradosToDownloadFotos();
	}

	public void resetaVisualizacoesCliente() {
		ClienteService.getInstance().resetaDivulgaInfo();
	}

	public void acrescentaVisualizacao(Cliente cliente) throws SQLException {
    	if (LavenderePdaConfig.nuApresentaDivulgacao == 0) return;
		ClienteService.getInstance().updateVisualizacaoDivulgaInfo(cliente.getRowKey());
	}
	public boolean possuiFotoGeradaCargaInicial() throws SQLException {
		final Date dataAtual = DateUtil.getCurrentDate();
		DivulgaInfo divulgaInfoFilter = new DivulgaInfo();
		divulgaInfoFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		divulgaInfoFilter.flTipoDivulgaInfo = DivulgaInfo.TIPO_DIVULGA_IMAGEM;
		divulgaInfoFilter.dtInicial = dataAtual;
		divulgaInfoFilter.dtFinal = dataAtual;
		return countByExample(divulgaInfoFilter) > 0;
	}

	public void updateResetReceberFotosNovamente() throws SQLException {
		((DivulgaInfoPdbxDao)getCrudDao()).updateResetReceberFotosNovamente();
	}

	public void updateAllFlTipoAlteracaoInserido() throws SQLException {
		((DivulgaInfoPdbxDao)getCrudDao()).updateAllFlTipoAlteracaoInserido();
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		((DivulgaInfoPdbxDao)getCrudDao()).updateReceberFotosAoFalharRecebimentoCargaInicial();
	}
	
	public DivulgaInfo findNextNaoAlterado() throws SQLException {
		return (DivulgaInfo) DivulgaInfoPdbxDao.getInstance().findNextNaoAlterado();
	}
	
}
