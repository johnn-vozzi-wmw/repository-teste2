package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FotoPesqMerProdConc;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoPesqMerProdConcDbxDao;
import totalcross.util.Vector;

public class FotoPesqMerProdConcService extends CrudService {

	private static FotoPesqMerProdConcService instance;

	public static FotoPesqMerProdConcService getInstance() {
		if (instance == null) {
			instance = new FotoPesqMerProdConcService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return FotoPesqMerProdConcDbxDao.getInstance();
	}

	public int getSequencialCdFotoPesquisaMercado(PesquisaMercadoConfig pesquisaMercadoConfig, PesquisaMercadoProduto pesquisaMercadoProduto) throws SQLException {
		FotoPesqMerProdConc fotoPesquisaMercado = getDefaultFotoPesquisaMercadoFilter(pesquisaMercadoConfig, pesquisaMercadoProduto);
		return ValueUtil.getIntegerValue(maxByExample(fotoPesquisaMercado, "cdFoto"));
	}

	public FotoPesqMerProdConc getDefaultFotoPesquisaMercadoFilter(PesquisaMercadoConfig pesquisaMercadoConfig, PesquisaMercadoProduto pesquisaMercadoProduto) {
		FotoPesqMerProdConc fotoPesqMerProdConc = new FotoPesqMerProdConc();
		fotoPesqMerProdConc.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		fotoPesqMerProdConc.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		fotoPesqMerProdConc.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			fotoPesqMerProdConc.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			fotoPesqMerProdConc.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		if (pesquisaMercadoProduto != null) {
			fotoPesqMerProdConc.cdProduto = pesquisaMercadoProduto.cdProduto;
		} else {
			fotoPesqMerProdConc.cdProduto = FotoPesqMerProdConc.DEFAULT_CDPRODUTO;
		}
		return fotoPesqMerProdConc;
	}

	public void excluiFotoPesquisaMercado(String nmFoto, FotoPesqMerProdConc fotoPesqMerProdConc) throws SQLException {
		fotoPesqMerProdConc.nmFoto = nmFoto;
		FileUtil.deleteFile(FotoPesqMerProdConc.getImagePath() + "/" + nmFoto);
		delete(fotoPesqMerProdConc);
	}

	public void atualizaFotoAposEnvio(Vector nmFotoList, String flEnviadoServidor) throws SQLException {
		int size = nmFotoList.size();
		for (int i = 0; i < size; i++) {
			String nmFoto = (String) nmFotoList.items[i];
			FotoPesqMerProdConc fotoPesqMerProdConcFilter = new FotoPesqMerProdConc();
			fotoPesqMerProdConcFilter.nmFoto = nmFoto;
			Vector fotoPesqMerProdConcList = findAllByExample(fotoPesqMerProdConcFilter);
			if (ValueUtil.isNotEmpty(fotoPesqMerProdConcList)) {
				FotoPesqMerProdConc fotoPesqMerProdConc = (FotoPesqMerProdConc) fotoPesqMerProdConcList.items[0];
				fotoPesqMerProdConc.flEnviadoServidor = flEnviadoServidor;
				FotoPesqMerProdConcDbxDao.getInstance().updateFlEnviadoServidor(fotoPesqMerProdConc, flEnviadoServidor);
			}
		}
	}

	@Override
	protected void setDadosAlteracao(BaseDomain domain) {
		domain.cdUsuario = Session.getCdUsuario();
	}

	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector fotoPesqMerProdConcList = FotoPesqMerProdConcService.getInstance().findAllAlterados();
		int size = fotoPesqMerProdConcList.size();
		for (int i = 0; i < size; i++) {
			FotoPesqMerProdConc fotoPesqMerProdConc = (FotoPesqMerProdConc) fotoPesqMerProdConcList.items[i];
			if (!fotoPesqMerProdConc.isFotoPesquisaMercadoEnviadaServidor()) {
				imageList.addElement(fotoPesqMerProdConc.nmFoto);
			}
		}
		return imageList;
	}

	public boolean hasFotoPesquisaMercadoRegistrada(PesquisaMercadoConfig pesquisaMercadoConfig, PesquisaMercadoProduto pesquisaMercadoProduto) throws SQLException {
		FotoPesqMerProdConc fotoPesqMerProdConc = new FotoPesqMerProdConc();
		fotoPesqMerProdConc.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		fotoPesqMerProdConc.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		fotoPesqMerProdConc.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			fotoPesqMerProdConc.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			fotoPesqMerProdConc.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		if (pesquisaMercadoProduto == null) {
			fotoPesqMerProdConc.cdProduto = FotoPesqMerProdConc.DEFAULT_CDPRODUTO;
		} else {
			fotoPesqMerProdConc.cdProduto = pesquisaMercadoProduto.cdProduto;
		}
		return FotoPesqMerProdConcDbxDao.getInstance().countByExample(fotoPesqMerProdConc) > 0;
	}

	public boolean hasFotoPesquisaMercadoRegistrada(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		return hasFotoPesquisaMercadoRegistrada(pesquisaMercadoConfig, null);
	}

	public void deleteAllFotosByExample(PesquisaMercadoConfig pesquisaMercadoConfig, PesquisaMercadoProduto pesquisaMercadoProduto) throws SQLException {
		FotoPesqMerProdConc fotoPesqMerProdConcFilter = getDefaultFotoPesquisaMercadoFilter(pesquisaMercadoConfig, pesquisaMercadoProduto);
		Vector fotoPesqMerProdConcList = findAllByExample(fotoPesqMerProdConcFilter);
		deleteFotoList(fotoPesqMerProdConcList);
	}

	private void deleteFotoList(Vector fotoPesqMerProdConcList) throws SQLException {
		int size = fotoPesqMerProdConcList.size();
		for (int i = 0; i < size; i++) {
			FotoPesqMerProdConc fotoPesqMerProdConc = (FotoPesqMerProdConc) fotoPesqMerProdConcList.items[i];
			FileUtil.deleteFile(FotoPesqMerProdConc.getImagePath() + "/" + fotoPesqMerProdConc.nmFoto);
			delete(fotoPesqMerProdConc);
		}
	}

	public void updateFlTipoAlteracao(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		FotoPesqMerProdConc fotoPesqMerProdConc = new FotoPesqMerProdConc();
		fotoPesqMerProdConc.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		fotoPesqMerProdConc.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		fotoPesqMerProdConc.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			fotoPesqMerProdConc.cdCliente = ValueUtil.VALOR_ZERO;
		} else if (SessionLavenderePda.getCliente() != null && SessionLavenderePda.getCliente().cdCliente != null) {
			fotoPesqMerProdConc.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		fotoPesqMerProdConc.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
		FotoPesqMerProdConcDbxDao.getInstance().updateFlTipoAlteracaoByExample(fotoPesqMerProdConc);
	}

	public void deleteFotoByPesquisaReg(PesquisaMercadoReg pesquisaMercadoReg) throws SQLException {
		FotoPesqMerProdConc fotoPesqMerProdConcFilter = new FotoPesqMerProdConc();
		fotoPesqMerProdConcFilter.cdEmpresa = pesquisaMercadoReg.cdEmpresa;
		fotoPesqMerProdConcFilter.cdRepresentante = pesquisaMercadoReg.cdRepresentante;
		fotoPesqMerProdConcFilter.cdPesquisaMercadoConfig = pesquisaMercadoReg.cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			fotoPesqMerProdConcFilter.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			fotoPesqMerProdConcFilter.cdCliente = pesquisaMercadoReg.cdCliente;
		}
		fotoPesqMerProdConcFilter.cdProduto = pesquisaMercadoReg.cdProduto;
		deleteFotoList(findAllByExample(fotoPesqMerProdConcFilter));
	}

}
