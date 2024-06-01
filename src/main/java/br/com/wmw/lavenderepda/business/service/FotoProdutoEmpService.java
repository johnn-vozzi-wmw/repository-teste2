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
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoEmp;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoProdutoEmpDao;
import totalcross.io.File;
import totalcross.io.FileNotFoundException;
import totalcross.io.IOException;
import totalcross.io.IllegalArgumentIOException;
import totalcross.sql.Types;
import totalcross.sys.Convert;
import totalcross.sys.Time;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class FotoProdutoEmpService extends CrudService {
	
	private static FotoProdutoEmpService instance;
	
	public static FotoProdutoEmpService getInstance() {
		if (instance == null) {
			instance = new FotoProdutoEmpService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return FotoProdutoEmpDao.getInstance();
	}
	
	public void createDirsFotoProdutoEmp() throws SQLException {
		Vector cdEmpresaList = FotoProdutoEmpDao.getInstance().findCdEmpresaList();
		int size = cdEmpresaList.size();
		String produtoPath = Produto.getPathImg();
		for (int i = 0; i < size; i++) {
			FileUtil.createDirIfNecessaryQuietly(produtoPath + "/" + cdEmpresaList.items[i] + "/");
		}
	}
	
	public Vector findAllNaoAlterados() throws SQLException {
		return FotoProdutoEmpDao.getInstance().findAllNaoAlterados();
	}
	
	public Vector findAllNaoAlterados(String cdEmpresa) throws SQLException {
		return FotoProdutoEmpDao.getInstance().findAllNaoAlterados(cdEmpresa);
	}
	
	public Hashtable findAllFotoProdutoEmp() throws SQLException {
		return FotoProdutoEmpDao.getInstance().findAllFotoProdutoEmp();
	}
	
	public int countFotoCargaInicial() throws SQLException {
		return ((FotoProdutoEmpDao)getCrudDao()).countFotoCargaInicial();
	}

	public void updateAllFlTipoAlteracaoInserido() throws SQLException {
		((FotoProdutoEmpDao)getCrudDao()).updateAllFlTipoAlteracaoInserido();
	}

	public void updateResetReceberFotosNovamente() throws SQLException {
		((FotoProdutoEmpDao)getCrudDao()).updateResetReceberFotosNovamente();
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		((FotoProdutoEmpDao)getCrudDao()).updateReceberFotosAoFalharRecebimentoCargaInicial();
	}
	
	public boolean possuiFotoGeradaCargaInicial() throws SQLException {
		FotoProdutoEmp fotoProdutoFilter = new FotoProdutoEmp();
		fotoProdutoFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		return countByExample(fotoProdutoFilter) > 0;
	}
	
	public Vector findAllFotoProdutoByAgrupadorGrade(FotoProdutoEmp filter) throws SQLException {
		return FotoProdutoEmpDao.getInstance().findAllFotoProdutoByAgrupadorGrade(filter);
	}
	
	public int countFotoProdutoByAgrupadorGrade(FotoProdutoEmp filter) throws SQLException {
		return FotoProdutoEmpDao.getInstance().countFotoProdutoByAgrupadorGrade(filter);
	}

	public int updateNaoReceberFotosBaixadasNovamente() throws SQLException {
		return updateNaoReceberFotosBaixadasNovamente(countAllNaoAlterados());
	}

	public int updateNaoReceberFotosBaixadasNovamente(int qtFotosPendentes) throws SQLException {
		if (isVerificacaoPendenciaEmLoteInviavel(qtFotosPendentes)) {
			return 0;
		}
		List<String> rowKeyList = getFotosQueJaForamBaixadas();
		return updateFotosQueJaForamBaixadas(rowKeyList);
	}
	
	private List<String> getFotosQueJaForamBaixadas() {
		String pathImg = FotoUtil.getPathImg(Produto.class);
		File pathImgProduto = null;
		List<String> rowKeyList = new ArrayList<>();
		try {
			pathImgProduto = new File(pathImg, File.DONT_OPEN);
			if (!pathImgProduto.exists()) {
				return rowKeyList;
			}
			HashSet<String> foldersList = getFoldersList(pathImgProduto);
			for (String dir : foldersList) {
				getFotosEmpresaQueJaForamBaixadas(pathImg, rowKeyList, dir);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		} finally {
			FileUtil.closeFile(pathImgProduto);
		}
		return rowKeyList;
	}

	private void getFotosEmpresaQueJaForamBaixadas(String pathImg, List<String> rowKeyList, String dir) throws IOException, IllegalArgumentIOException, FileNotFoundException, SQLException {
		String pathImgEmpresa = Convert.appendPath(pathImg, dir);
		File dirEmpresa = new File(pathImgEmpresa, File.DONT_OPEN);
		HashSet<String> filesMap = getFilesList(dirEmpresa, ValueUtil.VALOR_NI);
		if (filesMap.isEmpty()) {
			return;
		}
		String cdEmpresaDir = dir.substring(0, dir.length() - 1);
		Vector fotoList = findAllNaoAlterados(cdEmpresaDir);
		int size = fotoList.size();
		for (int i = 0; i < size; i++) {
			FotoProdutoEmp foto = (FotoProdutoEmp) fotoList.items[i];
			if (! filesMap.contains(foto.nmFoto)) {
				continue;
			}
			verificaFotoJaBaixada(pathImgEmpresa, rowKeyList, foto);
		}
	}

	private void verificaFotoJaBaixada(String pathImg, List<String> rowKeyList, FotoProdutoEmp foto) {
		if (isFotoFisicaLocalAtualizada(pathImg, foto)) {
			rowKeyList.add(foto.getRowKey());
		}
	}

	private boolean isFotoFisicaLocalAtualizada(String pathImg, FotoProduto foto) {
		try {
			String path = Convert.appendPath(pathImg, foto.nmFoto);
			File fotoFile = null;
			try {
				fotoFile = new File(path, File.READ_ONLY);
				Time timeArquivoFisico = fotoFile.getTime(File.TIME_MODIFIED);
				String hrModificacao = ValueUtil.isNotEmpty(foto.hrModificacao)  ? foto.hrModificacao : "00:00:00";
				Time timeFotoDatabase = foto.dtModificacao != null ? TimeUtil.toTime(foto.dtModificacao + " " + hrModificacao) : null;
				if (timeArquivoFisico != null && timeFotoDatabase != null && timeArquivoFisico.getTimeLong() > timeFotoDatabase.getTimeLong()) {
					return true;
				}
			} finally {
				FileUtil.closeFile(fotoFile);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return false;
	}
	
	private HashSet<String> getFoldersList(File pathImgProduto) throws IOException {
		String[] listFiles = pathImgProduto.listFiles();
		HashSet<String> filesMap = new HashSet<>();
		if (listFiles != null) {
			for (String filename : listFiles) {
				if (filename.endsWith("/")) {
					filesMap.add(filename);
				}
			}
		}
		return filesMap;
	}

	private HashSet<String> getFilesList(File dir, String dirEmpresaPrefix) throws IOException {
		String[] listFiles = dir.listFiles();
		HashSet<String> filesMap = new HashSet<>();
		if (listFiles != null) {
			for (String filename : listFiles) {
				filesMap.add(dirEmpresaPrefix + filename);
			}
		}
		filesMap.remove(dirEmpresaPrefix + FotoUtil.NO_MEDIA_FILE);
		return filesMap;
	}
	
	public HashSet<String> getFilesListWithEmpresaPrefix() throws IOException {
		String pathImg = FotoUtil.getPathImg(Produto.class);
		File pathImgProduto = null;
		try {
			pathImgProduto = new File(pathImg, File.DONT_OPEN);
			if (!pathImgProduto.exists()) {
				return new HashSet<String>(0);
			}
			HashSet<String> filesMap = new HashSet<>();
			HashSet<String> foldersList = getFoldersList(pathImgProduto);
			for (String dirEmpresa : foldersList) {
				String path = Convert.appendPath(pathImg, dirEmpresa);
				File dir = new File(path);
				try {
					filesMap.addAll(getFilesList(dir, dirEmpresa));
				} finally {
					FileUtil.closeFile(dir);
				}
			}
			return filesMap;
		} finally {
			FileUtil.closeFile(pathImgProduto);
		}
	}

	private int updateFotosQueJaForamBaixadas(List<String> rowKeyList) throws SQLException {
		if (ValueUtil.isEmpty(rowKeyList)) {
			return 0;
		}
		CrudDbxDao.getCurrentDriver().startTransaction();
		int qtUpdateCommited = 0;
		try {
			int count = 0;
			for (String rowKey: rowKeyList) {
				updateColumn(rowKey, BaseDomain.NMCAMPOTIPOALTERACAO, BaseDomain.FLTIPOALTERACAO_ALTERADO, Types.VARCHAR);
				count++;
				if (count > 200) {
					CrudDbxDao.getCurrentDriver().commit();
					qtUpdateCommited += count;
					count = 0; 
				}
			}
			CrudDbxDao.getCurrentDriver().commit();
			qtUpdateCommited += count;
		} catch (Exception e) {
			CrudDbxDao.getCurrentDriver().rollback();
			ExceptionUtil.handle(e);
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
		}
		return qtUpdateCommited;
	}

	public boolean isVerificacaoPendenciaEmLoteInviavel(int qtAlterados) {
		return qtAlterados > LavenderePdaConfig.LIMITE_MAXIMO_VERIFICAO_FOTO_LOCAL_EXISTE;
	}

}
