package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.lavenderepda.business.domain.DapLaudoAtua;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DapLaudoAtuaDbxDao;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.util.Vector;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;

public class DapLaudoAtuaService extends CrudService {
	
	private static DapLaudoAtuaService instance;
	
	public static DapLaudoAtuaService getInstance() {
		if (instance == null) {
			instance = new DapLaudoAtuaService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return DapLaudoAtuaDbxDao.getInstance();
	}
	
	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector imgAssinaturaList = findAllAlterados();
		int size = imgAssinaturaList.size();
		for (int i = 0; i < size; i++) {
			DapLaudoAtua dapLaudoAtua = (DapLaudoAtua) imgAssinaturaList.items[i];
			recuperaImgAss(dapLaudoAtua);
			imageList.addElement(dapLaudoAtua.nmImgAssCliente);
			imageList.addElement(dapLaudoAtua.nmImgAssTecnico);
		}
		return imageList;
	}
	private boolean isFotoExists(String nameImage) {
		boolean ret = false;
		File file = null;
		try {
			file = new File(DapLaudo.getImagePath() + nameImage);
			ret = file.exists();
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		} finally {
			FileUtil.closeFile(file);
		}
		return ret;
	}

	private void recuperaImgAss(DapLaudoAtua dapLaudoAtua) {
		try {
			if (!isFotoExists(dapLaudoAtua.nmImgAssCliente) || !isFotoExists(dapLaudoAtua.nmImgAssTecnico)) {
				DapLaudo dapLaudoFilter = new DapLaudo(dapLaudoAtua);
				DapLaudo dapLaudo = (DapLaudo) DapLaudoService.getInstance().findByPrimaryKey(dapLaudoFilter);
				if (!isFotoExists(dapLaudoAtua.nmImgAssCliente) && dapLaudo.imAssCliente != null) {
					DapLaudoService.getInstance().geraImagemAssinaturaDap(dapLaudo.imAssCliente, dapLaudo.getNmAssinaturaCli());
				}
				if (!isFotoExists(dapLaudoAtua.nmImgAssTecnico) && dapLaudo.imAssTecnico != null) {
					DapLaudoService.getInstance().geraImagemAssinaturaDap(dapLaudo.imAssTecnico, dapLaudo.getNmAssinaturaTec());
				}
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}


	public boolean isDapLaudoAtuaEnviadoServidor(DapLaudo dapLaudo) throws SQLException {
		DapLaudoAtua dapLaudoAtuaFilter = new DapLaudoAtua(dapLaudo);
		dapLaudoAtuaFilter.flStatusLaudo = DapLaudo.FLSTATUSLAUDO_FECHADO;
		dapLaudoAtuaFilter.somenteLaudosNaoEnviados = true;
		Vector vector = DapLaudoAtuaDbxDao.getInstance().findAllByExample(dapLaudoAtuaFilter);
		return vector.size() == 0;

	}

	public void deleteLaudosAtuaNaoEnviados(DapLaudoAtua dapLaudo) throws SQLException {
		try {
			dapLaudo.isDeleteLaudosAtuaNaoEnviados = true;
			deleteAllByExample(dapLaudo);
		} catch (Throwable e) {
			throw e;
		} finally {
			dapLaudo.isDeleteLaudosAtuaNaoEnviados = false;
		}
	}
}
