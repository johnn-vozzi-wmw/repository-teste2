package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.domain.DapLaudoAtua;
import br.com.wmw.lavenderepda.business.domain.dto.DapLaudoDTO;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DapLaudoPdbxDao;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.json.JSONObject;
import totalcross.sys.Convert;
import totalcross.ui.image.Image;

import java.sql.SQLException;

public class DapLaudoService extends CrudService {

	public static final String ASSINATURAPATH = DapLaudo.getImagePath();

	private static DapLaudoService instance;
	
	public static DapLaudoService getInstance() {
		if (instance == null) {
			instance = new DapLaudoService();
		}
		return instance;
	}
	@Override
	protected CrudDao getCrudDao() {
		return DapLaudoPdbxDao.getInstance();
	}

	@Override
	public void insert(BaseDomain domain) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		DapLaudoAtua dapLaudoAtua = new DapLaudoAtua(dapLaudo);
		if (dapLaudo.isDapFechado() || dapLaudo.isDapEditado()) {
			validate(domain);
			DapLaudoAtuaService.getInstance().deleteLaudosAtuaNaoEnviados(dapLaudoAtua);
			dapLaudoAtua.cdDapLaudoAtua = DapLaudoAtuaService.getInstance().generateIdGlobal();
			DapLaudoAtuaService.getInstance().insert(dapLaudoAtua);
		}
		try {
			setDadosAlteracao(domain);
			if (isRegistroExiste(domain)) {
				getCrudDao().update(domain);
			} else {
				getCrudDao().insert(domain);
			}
		} catch (Throwable e) {
			DapLaudoAtuaService.getInstance().delete(dapLaudoAtua);
			throw e;
		}
	}

	private boolean isRegistroExiste(BaseDomain domain) throws SQLException {
		return findColumnByRowKey(domain.getRowKey(), "ROWKEY") != null;
	}
	
	@Override
	public void update(BaseDomain domain) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		
		if (dapLaudo.isDapFechado()) {
			validate(domain);
		}
		
		setDadosAlteracao(domain);
		getCrudDao().update(domain);
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		DapLaudo dapLaudo = (DapLaudo) domain;
		if (DapLaudo.FLSTATUSLAUDO_FECHADO.equals(dapLaudo.flStatusLaudo)) {
			if (dapLaudo.imAssTecnico == null) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_DAPLAUDO, new Object[]{Messages.CAD_DAP_CAMPO_ASSINATURA_TEC}));
			}
			if (dapLaudo.imAssCliente == null) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_DAPLAUDO, new Object[]{Messages.CAD_DAP_CAMPO_ASSINATURA_CLI}));
			}
		}
	}
	
	public void validateInformacoesDap(DapLaudo dapLaudo) {
		if (ValueUtil.isEmpty(dapLaudo.cdLongitude) || ValueUtil.isEmpty(dapLaudo.cdLongitude)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_DAPLAUDO, new Object[] {Messages.CAD_DAP_CAMPO_POS}));
		}
		if (ValueUtil.isEmpty(dapLaudo.qtArea)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_DAPLAUDO, new Object[] {Messages.CAD_DAP_CAMPO_AREA}));
		}
		if (ValueUtil.isEmpty(dapLaudo.dsAspectoCultura)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_DAPLAUDO, new Object[] {Messages.CAD_DAP_CAMPO_ASPECTO}));
		}
		if (ValueUtil.isEmpty(dapLaudo.dsRecomendacoes)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_CAMPO_OBRIGATORIO_DAPLAUDO, new Object[] {Messages.CAD_DAP_CAMPO_RECOMENDACAO}));
		}
	}

	public String getDapLaudoJson(DapLaudo dapLaudo) {
		DapLaudoDTO dapLaudoDTO = new DapLaudoDTO(dapLaudo);
		JSONObject jsonDapLaudo = new JSONObject(dapLaudoDTO);
		return jsonDapLaudo.toString();
	}

	public byte[] getImageToBytes(String path) throws IOException {
		File file = null;
		byte[] buf = null;
		try {
			file = FileUtil.openFile(path, File.READ_WRITE);
			buf = new byte[file.getSize()];
			file.readBytes(buf, 0, file.getSize());
		} catch (Throwable e) {
			return null;
		} finally {
			FileUtil.closeFile(file);
		}
		return buf;
	}

	public void geraImagemAssinaturaDap(byte[] imArray, String nmImg) {
		File file = null;
		try {
			FileUtil.createDirIfNecessaryQuietly(ASSINATURAPATH);
			file = FileUtil.criaFile(Convert.appendPath(ASSINATURAPATH, nmImg + FotoUtil.DSEXTESAO_JPG));
			Image img = UiUtil.getImage(imArray.clone());
			img.createJpg(file, 100);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			FileUtil.closeFile(file);
		}
	}

	public boolean isDapEnviadoServidor(DapLaudo dapLaudo) throws SQLException {
		return ValueUtil.isEmpty(dapLaudo.flTipoAlteracao) || DapLaudoAtuaService.getInstance().isDapLaudoAtuaEnviadoServidor(dapLaudo);
	}

}
