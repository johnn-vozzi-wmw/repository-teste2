package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FotoPesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercado;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoPesquisaMercadoDbxDao;
import totalcross.io.File;
import totalcross.util.Vector;

public class FotoPesquisaMercadoService extends CrudService {

	private static FotoPesquisaMercadoService instance = null;

	private FotoPesquisaMercadoService() {
		// --
	}

	public static FotoPesquisaMercadoService getInstance() {
		if (instance == null) {
			instance = new FotoPesquisaMercadoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) {
	}

	@Override
	protected CrudDao getCrudDao() {
		return FotoPesquisaMercadoDbxDao.getInstance();
	}

	public int getSequencialCdFotoPesquisaMercado(PesquisaMercado pesquisaMercado) throws SQLException {
		FotoPesquisaMercado fotoPesquisaMercado = getDefaultFotoPesquisaMercadoFilter(pesquisaMercado);
		int cdBanco = ValueUtil.getIntegerValue(maxByExample(fotoPesquisaMercado, "cdFoto"));
		int cdCache = getMaxCdFotoPesquisaMercado(pesquisaMercado);
		return cdCache > cdBanco ? cdCache : cdBanco;
	}

	private int getMaxCdFotoPesquisaMercado(PesquisaMercado pesquisaMercado) {
		int size = pesquisaMercado.fotoList.size();
		int max = 0;
		for (int i = 0; i < size; i++) {
			int cd = ((FotoPesquisaMercado) pesquisaMercado.fotoList.items[i]).cdFoto;
			max = cd > max ? cd : max;
		}
		return max;
	}

	public FotoPesquisaMercado getDefaultFotoPesquisaMercadoFilter(PesquisaMercado pesquisaMercado) {
		FotoPesquisaMercado fotoPesquisaMercado = new FotoPesquisaMercado();
		fotoPesquisaMercado.cdEmpresa = pesquisaMercado.cdEmpresa;
		fotoPesquisaMercado.cdUsuario = pesquisaMercado.cdUsuario;
		fotoPesquisaMercado.cdRepresentante = pesquisaMercado.cdRepresentante;
		fotoPesquisaMercado.cdPesquisaMercado = pesquisaMercado.cdPesquisaMercado;
		fotoPesquisaMercado.flOrigemPesquisaMercado = pesquisaMercado.flOrigemPesquisaMercado;
		return fotoPesquisaMercado;
	}

	public FotoPesquisaMercado getFotoPesquisaMercado(PesquisaMercado pesquisaMercado, String nmFoto, int cdFoto) {
		FotoPesquisaMercado fotoPesquisaMercado = getDefaultFotoPesquisaMercadoFilter(pesquisaMercado);
		fotoPesquisaMercado.nmFoto = nmFoto;
		fotoPesquisaMercado.cdPesquisaMercado = pesquisaMercado.cdPesquisaMercado;
		fotoPesquisaMercado.nuTamanho = getTamanhoFoto(nmFoto);
		fotoPesquisaMercado.cdFoto = cdFoto;
		return fotoPesquisaMercado;
	}

	private int getTamanhoFoto(String nmFoto) {
		File file = null;
		try {
			file = FileUtil.openFile(FotoPesquisaMercado.getPathImg() + "/" + nmFoto, File.READ_ONLY);
			return file.getSize();
		} catch (Throwable e) {
			return 0;
		} finally  {
			FileUtil.closeFile(file);
		}
	}

	public void excluiFotoPesquisaMercado(PesquisaMercado pesquisaMercado, String nmFoto, boolean editing) throws SQLException {
		int size = pesquisaMercado.fotoList.size();
		if (!editing) {
			for (int i = 0; i < size; i++) {
				FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) pesquisaMercado.fotoList.items[i];
				if (ValueUtil.valueEquals(fotoPesquisaMercado.nmFoto, nmFoto)) {
					pesquisaMercado.fotoList.removeElementAt(i);
					FileUtil.deleteFile(FotoPesquisaMercado.getPathImg() + "/" + nmFoto);
					return;
				}
			}
		}
		FotoPesquisaMercado fotoPesquisaMercado = getDefaultFotoPesquisaMercadoFilter(pesquisaMercado);
		fotoPesquisaMercado.nmFoto = nmFoto;
		delete(fotoPesquisaMercado);
		FileUtil.deleteFile(FotoPesquisaMercado.getPathImg() + "/" + nmFoto);
	}

	public boolean isPermiteExcluirFoto(PesquisaMercado pesquisaMercado, String nmFoto, boolean editing) throws SQLException {
		int size = pesquisaMercado.fotoList.size();
		if (!editing) {
			for (int i = 0; i < size; i++) {
				FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) pesquisaMercado.fotoList.items[i];
				if (ValueUtil.valueEquals(fotoPesquisaMercado.nmFoto, nmFoto)) {
					return true;
				}
			}
		}
		FotoPesquisaMercado fotoPesquisaMercadoFilter = getDefaultFotoPesquisaMercadoFilter(pesquisaMercado);
		fotoPesquisaMercadoFilter.nmFoto = nmFoto;
		FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) findByRowKey(fotoPesquisaMercadoFilter.getRowKey());
		return BaseDomain.FLTIPOALTERACAO_ALTERADO.equals(fotoPesquisaMercado.flTipoAlteracao);
	}

	public void excluiFotoPesquisaMercadoArquivo(PesquisaMercado pesquisaMercado) {
		int size = pesquisaMercado.fotoList.size();
		for (int i = 0; i < size; i++) {
			FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) pesquisaMercado.fotoList.items[i];
			FileUtil.deleteFile(FotoPesquisaMercado.getPathImg() + "/" + fotoPesquisaMercado.nmFoto);
		}
	}

	public void insertFotosPesquisaMercado(PesquisaMercado pesquisaMercado) throws SQLException {
		int size = pesquisaMercado.fotoList.size();
		for (int i = 0; i < size; i++) {
			FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) pesquisaMercado.fotoList.items[i];
			insert(fotoPesquisaMercado);
		}
	}

	public void excluiFotosJuntoComPesquisaMercado(PesquisaMercado pesquisaMercado) throws SQLException {
		FotoPesquisaMercado fotoPesquisaMercadoFilter = getDefaultFotoPesquisaMercadoFilter(pesquisaMercado);
		Vector fotoList = findAllByExample(fotoPesquisaMercadoFilter);
		if (fotoList != null) {
			int size = fotoList.size();
			for (int i = 0; i < size; i++) {
				FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) fotoList.items[i];
				FileUtil.deleteFile(FotoPesquisaMercado.getPathImg() + "/" + fotoPesquisaMercado.nmFoto);
			}
		}
		deleteAllByExample(fotoPesquisaMercadoFilter);
	}

	public void atualizaFotoAposEnvio(Vector nmFotoList, String flEnviadoServidor) throws SQLException {
		int size = nmFotoList.size();
		for (int i = 0; i < size; i++) {
			String nmFoto = (String) nmFotoList.items[i];
			FotoPesquisaMercado fotoPesquisaMercadoFilter = new FotoPesquisaMercado();
			fotoPesquisaMercadoFilter.nmFoto = nmFoto;
			Vector fotoPesquisaMercadoList = findAllByExample(fotoPesquisaMercadoFilter);
			if (ValueUtil.isNotEmpty(fotoPesquisaMercadoList)) {
				FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) fotoPesquisaMercadoList.items[0];
				fotoPesquisaMercado.flEnviadoServidor = flEnviadoServidor;
				try {
					update(fotoPesquisaMercado);
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}

	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector fotoPesquisaMercadoList = FotoPesquisaMercadoDbxDao.getInstance().findAllAlterados();
		int size = fotoPesquisaMercadoList.size();
		for (int i = 0; i < size; i++) {
			FotoPesquisaMercado fotoPesquisaMercado = (FotoPesquisaMercado) fotoPesquisaMercadoList.items[i];
			if (!fotoPesquisaMercado.isFotoPesquisaMercadoEnviadaServidor()) {
				imageList.addElement(fotoPesquisaMercado.nmFoto);
			}
		}
		return imageList;		
	}

}
