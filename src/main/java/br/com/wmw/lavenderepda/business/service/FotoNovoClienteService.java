package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FotoNovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoNovoClienteDbxDao;
import totalcross.io.File;
import totalcross.util.Vector;

public class FotoNovoClienteService extends CrudService {

	private static FotoNovoClienteService instance;

	private FotoNovoClienteService() {
		//--
	}

	public static FotoNovoClienteService getInstance() {
		if (instance == null) {
			instance = new FotoNovoClienteService();
		}
		return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return FotoNovoClienteDbxDao.getInstance();
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public int getSequencialCdFotoNovoCliente(NovoCliente novoCliente) throws SQLException {
		FotoNovoCliente fotoNovoClienteFilter = createFilter(novoCliente);
		return countByExample(fotoNovoClienteFilter);
	}
	
	public void cancelaAlteracoesFotosFisicamente(NovoCliente novoCliente) throws SQLException {
		if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
			Vector fotoNovoClienteList = findAllFotoNovoClienteByNovoCliente(novoCliente);
			deleteArquivoCancelar(novoCliente.getFotoNovoClienteExcluidasList(), fotoNovoClienteList);
			deleteArquivoCancelar(novoCliente.getFotoNovoClienteList(), fotoNovoClienteList);
		}
	}
	
	private void deleteArquivoCancelar(Vector fotoNovoClienteList, Vector fotoNovoClienteDatabaseList) {
		for (int i = 0; i < fotoNovoClienteList.size(); i++) {
			FotoNovoCliente fotoNovoCliente = (FotoNovoCliente) fotoNovoClienteList.items[i];
			if (ValueUtil.isEmpty(fotoNovoClienteDatabaseList) || !fotoNovoClienteDatabaseList.contains(fotoNovoCliente)) {
				String fileName = NovoCliente.getPathImg() + fotoNovoCliente.nmFoto;
				FileUtil.deleteFile(fileName);
			}
		}
		fotoNovoClienteList.removeAllElements();
	}
	
	public void excluiFotosNovoCliente(NovoCliente novoCliente) throws SQLException {
		FotoNovoCliente fotoNovoClienteFilter = createFilter(novoCliente);
		Vector fotoList = findAllByExample(fotoNovoClienteFilter);
		int size = fotoList.size();
		for (int i = 0; i < size; i++) {
			FotoNovoCliente fotoNovoCliente = (FotoNovoCliente) fotoList.items[i];
			delete(fotoNovoCliente);
			FileUtil.deleteFile(NovoCliente.getPathImg() + "/" + fotoNovoCliente.nmFoto);
		}
	}

	public void excluiFotoNovoCliente(NovoCliente novoCliente, String nmFoto) {
		FotoNovoCliente fotoNovoCliente = createFilter(novoCliente);
		fotoNovoCliente.nmFoto = nmFoto;
		novoCliente.getFotoNovoClienteList().removeElement(fotoNovoCliente);
		novoCliente.getFotoNovoClienteExcluidasList().addElement(fotoNovoCliente);
	}

	public void insereFotoNovoClienteNaMemoria(NovoCliente novoCliente, String nmFoto) {
		FotoNovoCliente fotoNovoCliente = createFilter(novoCliente);
		fotoNovoCliente.nmFoto = nmFoto;
		fotoNovoCliente.nuTamanho = getTamanhoFoto(nmFoto);
		fotoNovoCliente.dtModificacao = DateUtil.getCurrentDate();
		novoCliente.getFotoNovoClienteList().addElement(fotoNovoCliente);
	}
	
	public void insereFotoNovoClienteNoBanco(NovoCliente novoCliente) throws SQLException {
		for (int i = 0; i < novoCliente.getFotoNovoClienteExcluidasList().size(); i++) {
			FotoNovoCliente fotoNovoClienteExcluida = (FotoNovoCliente) novoCliente.getFotoNovoClienteExcluidasList().items[i];
			deleteAllByExample(fotoNovoClienteExcluida);
			FileUtil.deleteFile(NovoCliente.getPathImg() + "/" + fotoNovoClienteExcluida.nmFoto);
		}
		for (int i = 0; i < novoCliente.getFotoNovoClienteList().size(); i++) {
			FotoNovoCliente fotoNovoCliente = (FotoNovoCliente) novoCliente.getFotoNovoClienteList().items[i];
			deleteAllByExample(fotoNovoCliente);
			insert(fotoNovoCliente);
		}
	}

	private int getTamanhoFoto(String nmFoto) {
		try {
			File file = FileUtil.openFile(NovoCliente.getPathImg() + "/" + nmFoto, File.READ_ONLY);
			return file.getSize();
		} catch (Throwable e) {
			return 0;
		}
	}


	public boolean isPermiteExcluirFoto(NovoCliente novoCliente, String nmFoto) throws SQLException {
		FotoNovoCliente fotoClienteFilter = createFilter(novoCliente);
		fotoClienteFilter.nmFoto = nmFoto;
		Vector fotoClienteList = findAllByExample(fotoClienteFilter);
		if (ValueUtil.isNotEmpty(fotoClienteList)) {
			return BaseDomain.FLTIPOALTERACAO_ALTERADO.equals(((FotoNovoCliente) fotoClienteList.items[0]).flTipoAlteracao);
		}
		return true;
	}

	public Vector getImageListToSync() throws SQLException {
		Vector imageList = new Vector();
		Vector fotoNovoClienteList = FotoNovoClienteDbxDao.getInstance().findAllAlterados();
		for (int i = 0; i < fotoNovoClienteList.size(); i++) {
			FotoNovoCliente fotoNovoCliente = (FotoNovoCliente) fotoNovoClienteList.items[i];
			imageList.addElement(fotoNovoCliente.nmFoto);
		}
		return imageList;
	}

	public boolean hasFoto(NovoCliente novoCliente) throws SQLException {
		return qtdFotoNovoCliente(novoCliente) > 0 ? true : false;
	}

	public int qtdFotoNovoCliente(NovoCliente novoCliente) throws SQLException {
		FotoNovoCliente fotoNovoClienteFilter = createFilter(novoCliente);
		return countByExample(fotoNovoClienteFilter);
	}

	public Vector findAllFotoNovoClienteByNovoCliente(NovoCliente novoCliente) throws SQLException {
		FotoNovoCliente fotoNovoClienteFilter = createFilter(novoCliente);
		return findAllByExample(fotoNovoClienteFilter);
	}

	private FotoNovoCliente createFilter(NovoCliente novoCliente) {
		FotoNovoCliente fotoNovoClienteFilter = new FotoNovoCliente();
		fotoNovoClienteFilter.cdEmpresa = novoCliente.cdEmpresa;
		fotoNovoClienteFilter.cdRepresentante = novoCliente.cdRepresentante;
		fotoNovoClienteFilter.cdCliente = novoCliente.cdNovoCliente;
		return fotoNovoClienteFilter;
	}

}
