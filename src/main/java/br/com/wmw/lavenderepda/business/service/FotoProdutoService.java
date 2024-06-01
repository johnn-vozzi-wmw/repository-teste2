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
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoEmp;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoProdutoDbxDao;
import totalcross.io.File;
import totalcross.io.FileNotFoundException;
import totalcross.io.IOException;
import totalcross.io.IllegalArgumentIOException;
import totalcross.sql.Types;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Time;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class FotoProdutoService extends CrudService {

    private static FotoProdutoService instance;

    private FotoProdutoService() {
        //--
    }

    public static FotoProdutoService getInstance() {
        if (instance == null) {
            instance = new FotoProdutoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FotoProdutoDbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }

    public Vector findAllNaoAlterados() throws java.sql.SQLException {
    	return ((FotoProdutoDbxDao)getCrudDao()).findAllNaoAlterados();
    }
    
    public Vector geraListaFotoProdutoGradeToCarrousel(ProdutoBase produto) throws SQLException {
    	FotoProdutoGrade filter = new FotoProdutoGrade();
		filter.dsAgrupadorGrade = produto.getDsAgrupadorGrade();
		Vector fotoProdutoList = FotoProdutoGradeService.getInstance().findAllByExample(filter);
		return populateImageList(fotoProdutoList, produto);
    }
    
    private Vector populateImageList(Vector fotoList, ProdutoBase produto) {
    	Vector imgList = new Vector();
    	String nmFotoComExtensao = ValueUtil.VALOR_NI;
    	String nmFoto = ValueUtil.VALOR_NI;
    	String nmExtensao = ValueUtil.VALOR_NI;
    	if (ValueUtil.isEmpty(fotoList)) {
			imgList.addElement(new String[] { ValueUtil.VALOR_NI, ValueUtil.VALOR_NI });
		}
    	for (int i = 0; i < fotoList.size(); i++) {
    		Object object = fotoList.elementAt(i);
    		if (object != null) {
	    		if (object instanceof FotoProduto) {
	    			nmFotoComExtensao = ((FotoProduto)object).nmFoto;
	    		} else if (object instanceof FotoProdutoGrade) {
	    			nmFotoComExtensao = ((FotoProdutoGrade)object).nmFoto;
	    		}
	    		if (ValueUtil.isNotEmpty(nmFotoComExtensao) && nmFotoComExtensao.lastIndexOf(".") != -1) {
	    			nmFoto = nmFotoComExtensao.substring(0, nmFotoComExtensao.lastIndexOf("."));
					nmExtensao = nmFotoComExtensao.substring(nmFotoComExtensao.lastIndexOf("."), nmFotoComExtensao.length());
	    		}
    		}
    		imgList.addElement(new String[] {nmFoto, produto.getDsAgrupadorGrade(), nmExtensao});
    	}
    	return imgList;
    }
    
    public Vector geraListaFotoProdutoAgrupadorGradeToCarrousel(ProdutoBase produto, String cdItemGrade1) throws SQLException {
		Vector fotoProdutoList = null;
		if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			FotoProdutoEmp filter = new FotoProdutoEmp();
			filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			filter.dsAgrupadorGrade = produto.getDsAgrupadorGrade();
			filter.cdItemGrade1 = cdItemGrade1;
			fotoProdutoList = FotoProdutoEmpService.getInstance().findAllFotoProdutoByAgrupadorGrade(filter);
		} else {
			FotoProduto fotoProdutoFilter = new FotoProduto();
			fotoProdutoFilter.dsAgrupadorGrade = produto.getDsAgrupadorGrade();
			fotoProdutoFilter.cdItemGrade1 = cdItemGrade1;
			fotoProdutoList = FotoProdutoService.getInstance().findAllFotoProdutoByAgrupadorGrade(fotoProdutoFilter);
		}
		return populateImageList(fotoProdutoList, produto);
	}
    
    public Vector geraListaFotoProdutoToCarroussel(ProdutoBase produto) throws SQLException {
		Vector imgList = new Vector();
		Vector fotoProdutoList = null;
		if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			FotoProdutoEmp filter = new FotoProdutoEmp();
			filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			filter.cdProduto = produto.cdProduto;
			fotoProdutoList = FotoProdutoEmpService.getInstance().findAllByExample(filter);
		} else {
			FotoProduto fotoProdutoFilter = new FotoProduto();
			fotoProdutoFilter.cdProduto = produto.cdProduto;
			fotoProdutoList = findAllByExample(fotoProdutoFilter);
		}
		if (ValueUtil.isEmpty(fotoProdutoList)) {
			imgList.addElement(new String[]{"images/nophoto", ProdutoService.getInstance().getDsProdutoSlide(produto), ".jpg"});
		}
		int size = fotoProdutoList.size();
		for (int i = 0; i < size; i++) {
			FotoProduto fotoProduto = (FotoProduto) fotoProdutoList.items[i];
			addFotoProdutoToList(produto, imgList, (fotoProduto != null) ? fotoProduto.nmFoto : ValueUtil.VALOR_NI, getPreviousFolderNameFotoProduto(fotoProduto));
		}
		return imgList;
	}
    
    public Vector geraListaImagemCarrousel(Vector produtoList) throws SQLException {
		Vector imgList = new Vector();
		if (produtoList.size() > 0) {
			Hashtable hash;
			if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
				hash = FotoProdutoEmpService.getInstance().findAllFotoProdutoEmp();
			} else {
				hash = FotoProdutoService.getInstance().findAllFotoProduto();
			}
			int size = produtoList.size();
			for (int i = 0; i < size ; i++) {
				ProdutoBase produto = (ProdutoBase) produtoList.items[i];
				String nmFoto = (String) hash.get(produto.cdProduto);
				addFotoProdutoToList(produto, imgList, nmFoto, getPreviousFolderNameFotoProduto(produto.fotoProduto));
			}
		}
		return imgList;
	}
	
    private void addFotoProdutoToList(ProdutoBase produto, Vector imgList, String nomeFotoProduto, String previousFolderName) {
		if (ValueUtil.isNotEmpty(nomeFotoProduto) && nomeFotoProduto.lastIndexOf(".") != -1) {
			String nmFoto = nomeFotoProduto.substring(0, nomeFotoProduto.lastIndexOf("."));
			String nmExntesao = nomeFotoProduto.substring(nomeFotoProduto.lastIndexOf("."), nomeFotoProduto.length());
			imgList.addElement(new String[]{previousFolderName + nmFoto, ProdutoService.getInstance().getDsProdutoSlide(produto), nmExntesao});
		} else {
			imgList.addElement(new String[]{"images/nophoto", ProdutoService.getInstance().getDsProdutoSlide(produto), ".jpg"});
		}
	}
    
    public Hashtable findAllFotoProduto() throws SQLException {
    	return ((FotoProdutoDbxDao)getCrudDao()).findAllFotoProduto();
    }

	public String getNmExtensaoFotoProduto(String cdProduto) throws SQLException {
		FotoProduto fotoProdutoFilter = new FotoProduto();
		fotoProdutoFilter.cdProduto = cdProduto;
		fotoProdutoFilter.sortAtributte = FotoProduto.NM_COLUNA_NMFOTO;
		fotoProdutoFilter.sortAsc = ValueUtil.VALOR_SIM;
		Vector fotoList = findAllByExample(fotoProdutoFilter);
		if (ValueUtil.isNotEmpty(fotoList)) {
			fotoProdutoFilter = (FotoProduto) fotoList.items[0];
			int lastIndex = fotoProdutoFilter.nmFoto.lastIndexOf(".");
			if (lastIndex != -1) {
				return fotoProdutoFilter.nmFoto.substring(lastIndex, fotoProdutoFilter.nmFoto.length());
			}
		}
		return "";
	}

	public boolean possuiFotoGeradaCargaInicial() throws SQLException {
		FotoProduto fotoProdutoFilter = new FotoProduto();
		fotoProdutoFilter.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_INSERIDO;
		return countByExample(fotoProdutoFilter) > 0;
	}
	
	public void updateResetReceberFotosNovamente() throws SQLException {
		((FotoProdutoDbxDao)getCrudDao()).updateResetReceberFotosNovamente();
	}
	
	public void updateAllFlTipoAlteracaoInserido() throws SQLException {
		((FotoProdutoDbxDao)getCrudDao()).updateAllFlTipoAlteracaoInserido();
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		((FotoProdutoDbxDao)getCrudDao()).updateReceberFotosAoFalharRecebimentoCargaInicial();
	}

	public int countFotoCargaInicial() throws SQLException {
		return ((FotoProdutoDbxDao)getCrudDao()).countFotoCargaInicial();
	}

	public int updateNaoReceberFotosBaixadasNovamente() throws SQLException {
		return updateNaoReceberFotosBaixadasNovamente(countAllNaoAlterados());
	}

	public int updateNaoReceberFotosBaixadasNovamente(int qtFotosPendentes) throws SQLException {
		if (isVerificacaoPendenciaEmLoteInviavel(qtFotosPendentes)) {
			return 0;
		}
		List<String> rowKeyList = verificaFotosQueJaForamBaixadas();
		return updateFotosQueJaForamBaixadas(rowKeyList);
	}
	
	private List<String> verificaFotosQueJaForamBaixadas() {
		String pathImg = FotoUtil.getPathImg(Produto.class);
		List<String> rowKeyList = new ArrayList<>();
		try {
			HashSet<String> filesMap = getFilesList();
			if (filesMap.isEmpty()) {
				return rowKeyList;
			}
			Vector fotoList = findAllNaoAlterados();
			int size = fotoList.size();
			for (int i = 0; i < size; i++) {
				FotoProduto foto = (FotoProduto) fotoList.items[i];
				if (! filesMap.contains(foto.nmFoto)) {
					continue;
				}
				verificaFotoJaBaixada(pathImg, rowKeyList, foto);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return rowKeyList;
	}

	private void verificaFotoJaBaixada(String pathImg, List<String> rowKeyList, FotoProduto foto) throws IllegalArgumentIOException, FileNotFoundException, IOException, InvalidNumberException {
		if (isFotoFisicaLocalAtualizada(pathImg, foto, foto.getKeyHashFotoProduto())) {
			rowKeyList.add(foto.getRowKey());
		}
	}

	public boolean isFotoFisicaLocalAtualizada(String pathImg, FotoProduto foto, String fotoKeyHash) {
		try {	
			String path = Convert.appendPath(pathImg, fotoKeyHash);
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
	

	public HashSet<String> getFilesList() throws IOException {
		String pathImg = FotoUtil.getPathImg(Produto.class);
		File pathImgProduto = null;
		try {
			pathImgProduto = new File(pathImg);
			if (!pathImgProduto.exists()) {
				return new HashSet<String>(0);
			}
			return getFilesList(pathImgProduto);
		} finally {
			FileUtil.closeFile(pathImgProduto);
		}
	}

	private HashSet<String> getFilesList(File pathImgProduto) throws IOException {
		String[] listFiles = pathImgProduto.listFiles();
		HashSet<String> filesMap = new HashSet<>();
		if (listFiles != null) {
			for (String filename : listFiles) {
				filesMap.add(filename);
			}
		}
		filesMap.remove(FotoUtil.NO_MEDIA_FILE);
		return filesMap;
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
	
	public Vector findAllFotoProdutoByAgrupadorGrade(FotoProduto filter) throws SQLException {
		return FotoProdutoDbxDao.getInstance().findAllFotoProdutoByAgrupadorGrade(filter);
	}
	
	public int countFotoProdutoByAgrupadorGrade(FotoProduto filter) throws SQLException {
		return FotoProdutoDbxDao.getInstance().countFotoProdutoByAgrupadorGrade(filter);
	}
	
	private String getPreviousFolderNameFotoProduto(FotoProduto fotoProduto) {
		if (fotoProduto instanceof FotoProdutoEmp) {
			return ((FotoProdutoEmp)fotoProduto).cdEmpresa + "/";
		}
		return ValueUtil.VALOR_NI;
	}

}