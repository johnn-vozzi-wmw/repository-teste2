package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoGrade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoProdutoGradeDbxDao;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.sql.Types;
import totalcross.sys.Convert;
import totalcross.sys.Time;
import totalcross.util.Vector;

public class FotoProdutoGradeService extends CrudService {

	private static FotoProdutoGradeService instance;

	public static FotoProdutoGradeService getInstance() {
		if (instance == null) {
			instance = new FotoProdutoGradeService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain baseDomain) throws SQLException { }

	@Override
	protected CrudDao getCrudDao() {
		return FotoProdutoGradeDbxDao.getInstance();
	}

	public boolean possuiFotoGeradaCargaInicial() throws SQLException {
		FotoProdutoGrade fotoProdutoFilter = new FotoProdutoGrade();
		fotoProdutoFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		return countByExample(fotoProdutoFilter) > 0;
	}

	public void updateResetReceberFotosNovamente() throws SQLException {
		((FotoProdutoGradeDbxDao)getCrudDao()).updateResetReceberFotosNovamente();
	}

	public int countFotoCargaInicial() throws SQLException {
		return ((FotoProdutoGradeDbxDao)getCrudDao()).countFotoCargaInicial();
	}

	public void updateAllFlTipoAlteracaoInserido() throws SQLException {
		((FotoProdutoGradeDbxDao)getCrudDao()).updateAllFlTipoAlteracaoInserido();
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		((FotoProdutoGradeDbxDao)getCrudDao()).updateReceberFotosAoFalharRecebimentoCargaInicial();
	}

	public Vector findAllNaoAlterados() throws java.sql.SQLException {
		return ((FotoProdutoGradeDbxDao)getCrudDao()).findAllNaoAlterados();
	}
	
	public int countAllNaoAlterados() throws java.sql.SQLException {
		return ((FotoProdutoGradeDbxDao)getCrudDao()).countAllNaoAlterados();
	}

	public void atualizaNaoReceberFotosBaixadasNovamente() throws SQLException {
		List<String> rowKeyList = verificaFotosQueJaForamBaixadas();
		updateFotosQueJaForamBaixadas(rowKeyList);
	}

	private List<String> verificaFotosQueJaForamBaixadas() {
		String pathImg = FotoProdutoGrade.getPathImg();
		File pathImgProduto = null;
		List<String> rowKeyList = new ArrayList<>();
		try {
			pathImgProduto = new File(pathImg);
			if (! pathImgProduto.exists()) {
				return rowKeyList;
			}
			HashSet<String> filesMap = getFilesList(pathImgProduto);
			if (filesMap.isEmpty()) {
				return rowKeyList;
			}
			Vector fotoList = findAllNaoAlterados();
			int size = fotoList.size();
			for (int i = 0; i < size; i++) {
				FotoProdutoGrade foto = (FotoProdutoGrade) fotoList.items[i];
				if (! filesMap.contains(foto.nmFoto)) {
					continue;
				}
				verificaFotoJaBaixada(pathImg, rowKeyList, foto);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		} finally {
			FileUtil.closeFile(pathImgProduto);
		}
		return rowKeyList;
	}

	private void verificaFotoJaBaixada(String pathImg, List<String> rowKeyList, FotoProdutoGrade foto) {
		try {
			String path = Convert.appendPath(pathImg, foto.nmFoto);
			File fotoFile = null;
			try {
				fotoFile = new File(path, File.READ_ONLY);
				Time time = fotoFile.getTime(File.TIME_MODIFIED);
				String hrModificacao = ValueUtil.isNotEmpty(foto.hrModificacao)  ? foto.hrModificacao : "00:00:00";
				Time timeFotoDatabase = foto.dtModificacao != null ? TimeUtil.toTime(foto.dtModificacao + " " + hrModificacao) : null;
				if (time != null && timeFotoDatabase != null && time.getTimeLong() > timeFotoDatabase.getTimeLong()) {
					rowKeyList.add(foto.getRowKey());
				}
			} finally {
				FileUtil.closeFile(fotoFile);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}
	
	private HashSet<String> getFilesList(File pathImgProduto) throws IOException {
		String[] listFiles = pathImgProduto.listFiles();
		HashSet<String> filesMap = new HashSet<>();
		if (listFiles != null) {
			for (String filename : listFiles) {
				if (!filename.endsWith(FotoUtil.NO_MEDIA_FILE)) {
					filesMap.add(filename);
				}
			}
		}
		return filesMap;
	}

	private void updateFotosQueJaForamBaixadas(List<String> rowKeyList) throws SQLException {
		if (ValueUtil.isEmpty(rowKeyList)) {
			return;
		}
		CrudDbxDao.getCurrentDriver().startTransaction();
		try {
			int count = 0;
			for (String rowKey: rowKeyList) {
				updateColumn(rowKey, BaseDomain.NMCAMPOTIPOALTERACAO, BaseDomain.FLTIPOALTERACAO_ALTERADO, Types.VARCHAR);
				count++;
				if (count > 200) {
					CrudDbxDao.getCurrentDriver().commit();
					count = 0;
				}
			}
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Exception e) {
			CrudDbxDao.getCurrentDriver().rollback();
			ExceptionUtil.handle(e);
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
		}
	}

}
