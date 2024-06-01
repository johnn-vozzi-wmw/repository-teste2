package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConcorrente;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoProduto;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoReg;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaMercadoRegDbxDao;
import totalcross.util.Vector;

public class PesquisaMercadoRegService extends CrudService {

	private static PesquisaMercadoRegService instance;

	private PesquisaMercadoRegService() {
	}

	public static PesquisaMercadoRegService getInstance() {
		if (instance == null) {
			instance = new PesquisaMercadoRegService();
		}
		return instance;
	}


	@Override
	public void validate(BaseDomain domain) throws SQLException {

	}

	@Override
	protected void setDadosAlteracao(BaseDomain domain) {
		domain.cdUsuario = Session.getCdUsuario();
	}

	@Override
	protected CrudDao getCrudDao() {
		return PesquisaMercadoRegDbxDao.getInstance();
	}

	public void updateCoordenadasByExample(PesquisaMercadoReg pesquisaMercadoReg) throws SQLException {
		PesquisaMercadoRegDbxDao.getInstance().updateCoordenadasByExample(pesquisaMercadoReg);
	}

	public double[] getCoordenadasBackground() {
		double[] lat_lon = new double[2];
		if (VmUtil.isAndroid() || VmUtil.isIOS()) {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_COORD_COLETANDO);
			msg.popupNonBlocking();
			try {
				GpsData gpsData = GpsService.getInstance().forceReadData(LavenderePdaConfig.timeOutCoordenadaAutoPesquisaMercado());
				if (gpsData.isSuccess()) {
					lat_lon[0] = gpsData.latitude;
					lat_lon[1] = gpsData.longitude;
				}
			} catch (ValidationException e) {
				throw new ValidationException(e.getMessage());
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
			} finally {
				msg.unpop();
			}
		}
		return lat_lon;
	}

	public boolean pedidoHasPesquisaMercadoRelacionada(Pedido pedido) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = getPesquisaMercadoRegFromPedido(pedido);
		return countByExample(pesquisaMercadoReg) > 0;
	}

	public void deletePesquisaMercadoRegByPedido(Pedido pedido) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = getPesquisaMercadoRegFromPedido(pedido);
		if (LavenderePdaConfig.usaInclusaoFotoPesquisaMercado() || LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado()) {
			FotoPesqMerProdConcService.getInstance().deleteFotoByPesquisaReg(pesquisaMercadoReg);
		}
		deleteAllByExample(pesquisaMercadoReg);
	}

	public void clearNuPedidoFromPesquisaMercadoReg(Pedido pedido) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = getPesquisaMercadoRegFromPedido(pedido);
		PesquisaMercadoRegDbxDao.getInstance().clearNuPedidoAfterDeletePedido(pesquisaMercadoReg);
	}

	private PesquisaMercadoReg getPesquisaMercadoRegFromPedido(Pedido pedido) {
		PesquisaMercadoReg pesquisaMercadoReg = new PesquisaMercadoReg();
		pesquisaMercadoReg.cdEmpresa = pedido.cdEmpresa;
		pesquisaMercadoReg.cdRepresentante = pedido.cdRepresentante;
		pesquisaMercadoReg.nuPedido = pedido.nuPedido;
		pesquisaMercadoReg.flOrigemPedido = pedido.flOrigemPedido;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			pesquisaMercadoReg.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			pesquisaMercadoReg.cdCliente = pedido.cdCliente;
		}
		return pesquisaMercadoReg;
	}

	public boolean hasPesquisaMercadoForProduto(PesquisaMercadoProduto pesquisaMercadoProduto, PesquisaMercadoConfig pesquisaMercadoConfig, boolean todosConcorrentes) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = new PesquisaMercadoReg();
		pesquisaMercadoReg.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoReg.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		pesquisaMercadoReg.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			pesquisaMercadoReg.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			pesquisaMercadoReg.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		if (pesquisaMercadoProduto != null) {
			pesquisaMercadoReg.cdProduto = pesquisaMercadoProduto.cdProduto;
		}
		if (todosConcorrentes) {
			PesquisaMercadoConcorrente pesquisaMercadoConcorrente = new PesquisaMercadoConcorrente();
			pesquisaMercadoConcorrente.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
			pesquisaMercadoConcorrente.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
			return countByExample(pesquisaMercadoReg) == PesquisaMercadoConcorrenteService.getInstance().countByExample(pesquisaMercadoConcorrente);
		} else {
			return countByExample(pesquisaMercadoReg) > 0;
		}
	}

	public boolean hasPesquisaMercadoForConfig(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		return hasPesquisaMercadoForProduto(null, pesquisaMercadoConfig, false);
	}

	public void updateFlFinalizada(PesquisaMercadoConfig pesquisaMercadoConfig, String flFinalizada) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = new PesquisaMercadoReg();
		pesquisaMercadoReg.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoReg.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		pesquisaMercadoReg.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			pesquisaMercadoReg.cdCliente = ValueUtil.VALOR_ZERO;
		} else if (SessionLavenderePda.getCliente() != null && SessionLavenderePda.getCliente().cdCliente != null) {
			pesquisaMercadoReg.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		PesquisaMercadoRegDbxDao.getInstance().updateFlFinalizada(pesquisaMercadoReg, flFinalizada);
	}

	public boolean isPesquisaFinalizada(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = new PesquisaMercadoReg();
		pesquisaMercadoReg.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoReg.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		pesquisaMercadoReg.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		if (LavenderePdaConfig.usaIgnoraClientePesquisaMercado()) {
			pesquisaMercadoReg.cdCliente = ValueUtil.VALOR_ZERO;
		} else {
			pesquisaMercadoReg.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		}
		Vector pesquisaMercadoRegList = PesquisaMercadoRegDbxDao.getInstance().findAllByExample(pesquisaMercadoReg);
		return ValueUtil.isNotEmpty(pesquisaMercadoRegList) && ((PesquisaMercadoReg) pesquisaMercadoRegList.items[0]).isFinalizada();
	}

	public Vector findAllPesquisaMercadoRegExpirada(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		PesquisaMercadoReg pesquisaMercadoReg = new PesquisaMercadoReg();
		pesquisaMercadoReg.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoReg.cdRepresentante = pesquisaMercadoConfig.cdRepresentante;
		pesquisaMercadoReg.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		pesquisaMercadoReg.flFinalizada = ValueUtil.VALOR_SIM;
		return PesquisaMercadoRegDbxDao.getInstance().findAllByExample(pesquisaMercadoReg);
	}

	public void deletePesquisaMercadoSemConfig() throws SQLException {
		PesquisaMercadoReg pesquisaMercadoRegFilter = new PesquisaMercadoReg();
		pesquisaMercadoRegFilter.flFinalizada = ValueUtil.VALOR_SIM;
		Vector pesquisaMercadoList = PesquisaMercadoRegDbxDao.getInstance().findAllByExample(pesquisaMercadoRegFilter);
		int size = pesquisaMercadoList.size();
		for (int i = 0; i < size; i++) {
			PesquisaMercadoReg pesquisaMercadoReg = (PesquisaMercadoReg) pesquisaMercadoList.items[i];
			PesquisaMercadoConfig pesquisaMercadoConfig = new PesquisaMercadoConfig();
			pesquisaMercadoConfig.cdEmpresa = pesquisaMercadoReg.cdEmpresa;
			pesquisaMercadoConfig.cdRepresentante = pesquisaMercadoReg.cdRepresentante;
			pesquisaMercadoConfig.cdPesquisaMercadoConfig = pesquisaMercadoReg.cdPesquisaMercadoConfig;
			if (PesquisaMercadoConfigService.getInstance().findByPrimaryKey(pesquisaMercadoConfig) == null) {
				delete(pesquisaMercadoReg);
				if (LavenderePdaConfig.usaInclusaoFotoPesquisaMercado() || LavenderePdaConfig.usaInclusaoFotoProdPesquisaMercado()) {
					FotoPesqMerProdConcService.getInstance().updateFlTipoAlteracao(pesquisaMercadoConfig);
				}
			}
		}
	}
}
